package com.codecool.networking.modes;

import com.codecool.networking.view.BuiltMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket socket;
    private int clientNumber;
    private Scanner scanner = new Scanner(System.in);
    private Server server;
    private String name;


    public ServerThread(Socket socket, int clientNumber, Server server) {
        this.server = server;
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
            String input = null;
            if(incomeMessage.ready()){
                input = incomeMessage.readLine();

            }
//            outcomeMessage.println(getUserInput() +" by server");
            if(input != null){
                getAdrressMessage().println(input +" by server");

            }
        }
    }

    private void setUserName(BufferedReader incomeMessage, PrintWriter outcomeMessage) throws IOException {
        this.name = null;
        while (name == null){
            outcomeMessage.println("Hello, you are client #" + clientNumber);

            name = incomeMessage.readLine();
            if(name != null){
                server.addClientToMap(this.name);
            }
//            outcomeMessage.println(getOnlineUsers());
            sendOnlineUsers();
            BuiltMessages.setUserNameMessage(clientNumber, name);
        }
    }
    private String getUserInput() {
        System.out.println("\nServer type meesage: ");
        return scanner.nextLine();

    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getAdrressMessage() throws IOException {
        PrintWriter out = new PrintWriter(server.getSocket("dupa").getOutputStream(), true);
        return out;
    }
    public String getOnlineUsers(){
        return this.server.getOnlineUsers();
    }
    public String getClientName(){
        return this.name;
    }

    public void sendOnlineUsers(){

       this.server.getMap().entrySet()
               .forEach(user -> {
                   PrintWriter printWriter = null;
                   try {
                       printWriter = new PrintWriter(user.getValue().getSocket().getOutputStream(), true);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   printWriter.println(getOnlineUsers());
               });
    }
}