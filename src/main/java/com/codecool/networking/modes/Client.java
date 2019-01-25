package com.codecool.networking.modes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner scanner = new Scanner(System.in);

    public void runClientSocket(String hostname, int portNumber){


        String name = getName();
        PrintWriter outcomeMessage = null;
        BufferedReader incomeMessage = null;
        try {
            Socket socket = new Socket(hostname, portNumber);
            outcomeMessage = new PrintWriter(socket.getOutputStream(), true);
            incomeMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sentNameOfClientToServer(outcomeMessage, name);
        displayIncomeMessage(incomeMessage);


        while (true) {

            String userInput = getUserInput();
            if(checkIfUserWantToDisconnect(userInput)){
                break;
            }


            outcomeMessage.println(name + ": " + userInput);
            displayIncomeMessage(incomeMessage);
        }
    }
    private void sentNameOfClientToServer(PrintWriter outcomeMessage, String name){
        outcomeMessage.println(name);
    }
    private void displayIncomeMessage(BufferedReader incomeMessage){
        try {
            String inputMessage = incomeMessage.readLine();
            System.out.println(inputMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfUserWantToDisconnect(String message) {
        if (message == null || message.isEmpty()) {
            return true;
        }
        return false;
    }

    private String getUserInput() {
        System.out.println("\nEnter a string to send to the server (empty to quit):");
        return scanner.nextLine();

    }

    private String getName() {
        System.out.println("Hello user enter your name: ");
        return scanner.nextLine();
    }
}