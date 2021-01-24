package com.company;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
    private int timer;
    private int maxSimulationTime;
    private int numberOfQueues;
    private int openQueues;
    private int personIndex;
    ArrayList<Person> persons;
    private List<Queue> queues;
    private List<Thread> threads;
    private BufferedWriter writer;
    private PersonsReader reader;

    public Scheduler(PersonsReader reader, BufferedWriter writer) {
        this.reader = reader;
        numberOfQueues = reader.getNrOfQueues();
        timer = 0;
        openQueues = 0;
        maxSimulationTime = reader.getMaxSimulationTime();
        personIndex = 0;
        queues = new ArrayList<>();
        threads = new ArrayList<>();
        persons = Utils.getPersons(reader.getArrivalBounds(), reader.getWaitBounds(), reader.getNrOfClients());
        persons.sort(Person::compareTo);
        this.writer = writer;
    }
    private void elapseTime() {
        Queue queue;
        Thread thread;
        for(int i = 0; i < numberOfQueues; i ++) {
            queue = queues.get(i);
            thread = threads.get(i);
            if(queue.isRunning()) {
                if(thread.getState() == Thread.State.WAITING) {
                    synchronized (queue) {
                        queue.notify();
                    }
                } else {
                    threads.remove(i);
                    thread = new Thread(queue);
                    threads.add(i, thread);
                    thread.start();
                }
            }
        }
    }
    private void dispatchTask(Person person) {
        if(openQueues < numberOfQueues) {
            Queue queue = getAvailableQueue();
            queue.addPerson(person);
            openQueues ++;
        } else {
            Queue queue = Utils.getFastestQueue(queues);
            queue.addPerson(person);
        }
    }
    private void dispatchTasks() {
        while((personIndex < persons.size()) && (persons.get(personIndex).getArrivalTime() == timer)) {
            dispatchTask(persons.get(personIndex));
            personIndex ++;
        }
    }
    private void updateOpenQueues() {
        int count = 0;
        for(Queue queue : queues) {
            if(queue.isRunning()) {
                count ++;
            }
        }
        openQueues = count;
    }
    private void killThreads() {
        for(int i = 0; i < queues.size(); i++) {
            Queue queue = queues.get(i);
            queue.abortRunning();
            Thread thread = threads.get(i);
            if(thread.getState() == Thread.State.WAITING) {
                synchronized (queue) {
                    queue.notify();
                }
            }
        }
    }
    private Queue getAvailableQueue() {
        for(Queue queue : this.queues) {
            if(queue.isEmpty()) return queue;
        }
        return null;
    }

    private boolean threadsDone() {
        for(Thread thread : threads) {
            if(!((thread.getState() == Thread.State.WAITING) || (thread.getState() == Thread.State.NEW)
                    || (thread.getState() == Thread.State.TERMINATED))) return false;
        }
        return true;
    }
    private boolean emptyQueues() {
        for(Queue queue : queues) {
            if(!queue.isEmpty()) return false;
        }
        return true;
    }
    @Override
    public void run() {
        for (int i = 0; i < numberOfQueues; i++) {
            Queue q = new Queue(reader.getNrOfClients());
            queues.add(q);
            Thread thread = new Thread(q);
            threads.add(thread);
        }
        while (timer <= maxSimulationTime) {
            if (personIndex < persons.size()) {
                dispatchTasks();
            }
            try {
                Utils.writeQueueStatus(persons, queues, writer, timer);
            } catch (IOException e) {
                System.out.println("Error while writing: " + e.getMessage());
            }
            if ((personIndex == persons.size()) && emptyQueues()) {
                break;
            }
            elapseTime();
            timer++;
            while (!threadsDone()) {
            }
            updateOpenQueues();
        }
        try {
            DecimalFormat df = new DecimalFormat("####0.00");
            System.out.println(df.format(Utils.computeAverageWaiting(persons)));
            writer.write("Average waiting time: " + df.format(Utils.computeAverageWaiting(persons)));
        } catch (IOException e) {
            System.out.println("Error while closing output file: " + e.getMessage());
        } finally {
            try {
                writer.close();
                killThreads();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
