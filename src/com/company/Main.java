package com.company;


public class Main {

    public static void main(String[] args) {
        Server server = new Server(args[0], args[1]);
        server.start();
    }

    //this code really needs to be redone
    //try to avoid the busy-waiting thingy
    //idk what else. cya

}
