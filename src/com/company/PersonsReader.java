package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PersonsReader {
    private String filename;
    private BufferedReader reader;
    private int nrOfClients;
    private int nrOfQueues;
    private int[] arrivalBounds;
    private int[] waitBounds;
    private int maxSimulationTime;

    public PersonsReader(String filename) throws IOException {
        String line;
        String[] lines;
        arrivalBounds = new int[2];
        waitBounds = new int[2];
        this.filename = filename;
        reader = new BufferedReader(new FileReader(filename));
        nrOfClients = Integer.parseInt(reader.readLine());
        nrOfQueues = Integer.parseInt(reader.readLine());
        maxSimulationTime = Integer.parseInt(reader.readLine());
        line = reader.readLine();
        lines = line.split(",");
        arrivalBounds[0] = Integer.parseInt(lines[0]);
        arrivalBounds[1] = Integer.parseInt(lines[1]);
        line = reader.readLine();
        lines = line.split(",");
        waitBounds[0] = Integer.parseInt(lines[0]);
        waitBounds[1] = Integer.parseInt(lines[1]);
        reader.close();
    }

    public int getNrOfClients() {
        return nrOfClients;
    }

    public int getNrOfQueues() {
        return nrOfQueues;
    }

    public int[] getArrivalBounds() {
        return arrivalBounds;
    }

    public int[] getWaitBounds() {
        return waitBounds;
    }

    public int getMaxSimulationTime() {
        return maxSimulationTime;
    }
}
