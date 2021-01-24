package com.company;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Queue implements Runnable{
    private BlockingQueue<Person> queue;
    private volatile boolean running;
    private int waitingPeriod;

    public Queue(int size) {
        queue  = new ArrayBlockingQueue<>(size);
        running = false;
    }

    public void addPerson(Person person) {
        queue.add(person);
        waitingPeriod += person.getWaitTime();
        running = true;
    }

    public void elapseTime() {
        queue.forEach((p)-> p.incrementWaitedTime());
        queue.peek().decrementWaitingTime();
        if(queue.peek().wasServed()) queue.remove();
        waitingPeriod --;
    }

    public BlockingQueue<Person> getQueue() {
        return queue;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    public void abortRunning() {
        running = false;
    }
    public boolean isRunning() {
        return running;
    }
    public int getWaitingPeriod() {
        return waitingPeriod;
    }
    @Override
    public void run() {
        while(true) {
            elapseTime();
            if(isEmpty()) {
                running = false; //kill thread
                return;
            } else {
                try {
                    synchronized (this) {
                        wait();
                        if(!running) { // used for forcing the threads to terminate
                            return; //even though the queue is not empty
                        }
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Thread interrupted");
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
