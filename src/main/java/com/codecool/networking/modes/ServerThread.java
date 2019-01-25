package com.codecool.networking.modes;

import com.codecool.networking.view.BuiltMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private int clientNumber;

    public ServerThread(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        BuiltMessages.newUserLogMessage(socket, clientNumber);

    }
    public void run() {
        try {
            BufferedReader incomeMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outcomeMessage = new PrintWriter(socket.getOutputStream(), true);

            setUserName(incomeMessage, outcomeMessage);
            listenOnServer(incomeMessage, outcomeMessage);
        }
        catch (IOException e) {
            System.out.println("Error handling client #" + clientNumber);
        }
        finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("Error closing socket ");
        }
        System.out.println("Connection with client # " + clientNumber + " closed");
    }

    private void listenOnServer(BufferedReader incomeMessage, PrintWriter outcomeMessage) throws IOException {
        while (true) {
            String input = incomeMessage.readLine();
            if (input == null || input.isEmpty()) {
                break;
            }
            outcomeMessage.println(input +" by server");
            System.out.println(input);

        }
    }

    private void setUserName(BufferedReader incomeMessage, PrintWriter outcomeMessage) throws IOException {
        String name = null;
        while (name == null){
            outcomeMessage.println("Hello, you are client #" + clientNumber);
            name = incomeMessage.readLine();
            BuiltMessages.setUserNameMessage(clientNumber, name);
        }
    }
}