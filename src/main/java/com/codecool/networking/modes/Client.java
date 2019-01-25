package com.codecool.networking.modes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String name;

    public void runClientSocket(String hostname, int portNumber){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello user enter your name: ");
        this.name = scanner.nextLine();
        Socket socket = null;
        PrintWriter out = null;
        String in = "";
        try {
            socket = new Socket(hostname, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(name);
        System.out.println(in);


        while (true) {
            System.out.println("\nEnter a string to send to the server (empty to quit):");
            String message = scanner.nextLine();
            if (message == null || message.isEmpty()) {
                break;
            }
            if(name == null){
                name = message;
            }
            out.println(name + ": " + message);
            System.out.println(in);
        }
    }
}