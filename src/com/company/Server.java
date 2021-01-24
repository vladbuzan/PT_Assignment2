package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Server {
    private Scheduler scheduler;

    public Server(String filein, String fileout) {
        try {
            PersonsReader reader = new PersonsReader(filein);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileout));
            scheduler = new Scheduler(reader, writer);
            
        } catch (IOException ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }

    public void start() {
        Thread serverThread = new Thread(scheduler);
        serverThread.start();
    }


}
