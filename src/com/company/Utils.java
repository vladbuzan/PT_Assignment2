package com.company;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Utils {
    public static ArrayList<Person> getPersons(int[] arrivalBounds, int[] waitBounds, int nrOfClients) {
        ArrayList<Person> personArrayList = new ArrayList<>();
        for(int ID = 1; ID <= nrOfClients; ID ++) {
            personArrayList.add(Person.generatePerson(arrivalBounds, waitBounds, ID));
        }
        return personArrayList;
    }

    public static Queue getFastestQueue(List<Queue> queues) {
        Queue min = queues.get(0);
        for(Queue queue : queues) {
            if(queue.getWaitingPeriod() < min.getWaitingPeriod()) {
                min = queue;
            }
        }
        return min;
    }

    public static void writeQueueStatus(ArrayList<Person> persons, List<Queue> queues, BufferedWriter writer, int time) throws IOException {
        int queueIndex = 1;
        writer.flush();
        writer.write("Time " + Integer.toString(time) + "\n");
        writer.write("Waiting clients: ");
        persons.forEach((person -> {
            if(person.getArrivalTime() > time) {
                try {
                    writer.write(person.toString());
                } catch (IOException e) {
                    System.out.println("Error while writing: " + e.getMessage());
                }
            }
        }));
        writer.write('\n');
        for(int i = 0; i < queues.size(); i++) {
            Queue queue = queues.get(i);
            writer.write("Queue " + (i + 1) + ": ");
            if(queue.isEmpty()) {
                writer.write("closed\n");
            } else {
                BlockingQueue<Person> personBlockingQueue = queue.getQueue();
                personBlockingQueue.forEach(person -> {
                    try {
                        writer.write(person.toString());
                    } catch (IOException e) {
                        System.out.println("Error while writing: " + e.getMessage());
                    }
                });
                writer.write('\n');
            }
        }
        writer.write('\n');
    }

    public static double computeAverageWaiting(ArrayList<Person> persons ) {
        double result;
        int sum = 0;
        for(Person person : persons) {
            sum += person.getWaited();
        }
        result = sum / (double)persons.size();
        return result;
    }

}
