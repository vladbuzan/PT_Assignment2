package com.company;

import java.util.Random;

public class Person {
    private int arrivalTime;
    private int waitTime;
    private int ID;
    private int waited;
    private static Random rand;

    public Person(int arrivalTime, int waitTime, int ID){
        this.arrivalTime = arrivalTime;
        this.waitTime = waitTime;
        this.ID = ID;
        this.waited = 0;
    }
    static {
        rand = new Random();
    }
    public static Person generatePerson(int[] arrivalBounds, int[] waitBounds, int ID) {
        return new Person(rand.nextInt(arrivalBounds[1] - arrivalBounds[0] + 1) + arrivalBounds[0],
                           rand.nextInt(waitBounds[1] - waitBounds[0] + 1) + waitBounds[0], ID);
    }
    //used to sort persons by arrival time
    //when arrival time is equal for two persons,
    //sort based on wait time
    public int compareTo(Person person) {
        if(this.arrivalTime != person.arrivalTime) {
            return this.arrivalTime - person.arrivalTime;
        } else {
            return this.waitTime - person.waitTime;
        }
    }

    @Override
    public String toString() {
        return "(" + ID + "," + arrivalTime + "," +
                waitTime  + ");";
    }

    public int getWaitTime() {
        return waitTime;
    }
    public int getWaited() {
        return waited;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void incrementWaitedTime() {
        waited ++;
    }
    public void decrementWaitingTime() {
        waitTime --;
    }
    public boolean wasServed() {
        return waitTime == 0;
    }
}
