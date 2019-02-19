package com.codecool.networking.view;

import java.net.Socket;

public class SystemMessage {

    public static void failedArgsMessage(){
        System.out.println("Provide correct args to console\n" +
                "1. Mode = [client/server]\n" +
                "2. Hostname\n" +
                "3. Port number\n" +
                "Example: server localhost 8020");
    }

    public static void failedModeArgsMessage(){
        System.out.println("Incorrect mode type chose [server] or [client]");
    }
    public static void newUserLogMessage(Socket socket, int clientNumber){
        System.out.println("New client #" + clientNumber + " connected at " + socket);
    }
    public static void setUserNameMessage(int clientNumber, String name){
        System.out.println("User number: " + clientNumber + " set his nick name to: " + name);
    }

    public static void printMessage(String message){
        System.out.println(message);
    }
}
