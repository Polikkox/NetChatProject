package com.codecool.networking.modes;

import com.codecool.networking.ScannerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner scanner = new Scanner(System.in);
    private ScannerThread scannerThread;
    private boolean lock = true;

    public void runClientSocket(String hostname, int portNumber) throws IOException {


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

            if(userInput != null){
                outcomeMessage.println(name + ": " + userInput);

            }
            if(incomeMessage.ready()){
                System.out.println(incomeMessage.readLine());
            }
        }
    }

    private void sentNameOfClientToServer(PrintWriter outcomeMessage, String name){
        outcomeMessage.println(name);
    }

    private void displayIncomeMessage(BufferedReader incomeMessage) throws IOException {
        String inputMessage = incomeMessage.readLine();
        System.out.println(inputMessage);
    }

    private boolean checkIfUserWantToDisconnect(String message) {
        if (message == null || message.isEmpty()) {
            return true;
        }
        return false;
    }

    private String getUserInput() {

        //Run only one thread waiting for user input
        if(lock) {

            scannerThread = new ScannerThread();
            Thread thread = new Thread(scannerThread);
            thread.start();
            lock = false;
        }
        String input = scannerThread.getInput();

        //Enable take another one input after got input
        if(input != null){
            scannerThread.setInput(null);
            lock = true;
        }
        return input;
    }

    private String getName() {
        System.out.println("Hello user enter your name: ");
        return scanner.nextLine();
    }
}