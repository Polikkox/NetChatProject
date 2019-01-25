package com.codecool.networking.modes;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public void runServer(int portNumber){
        System.out.println("The server is running.");
        int clientNumber = 1;
        try (ServerSocket listener = new ServerSocket(portNumber)) {
            while (true) {

                new ServerThread(listener.accept(), clientNumber++).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
