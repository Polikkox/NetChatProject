package com.codecool.networking.handler;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Server;
import com.codecool.networking.view.BuiltMessages;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientHandler extends Thread {
    private Socket socket;
    private int clientNumber;
    private Scanner scanner = new Scanner(System.in);
    private Server server;
    private ObjectOutputStream outcomeMessage;


    public ServerClientHandler(Socket socket, int clientNumber, Server server) {
        this.server = server;
        this.socket = socket;
        this.clientNumber = clientNumber;
        BuiltMessages.newUserLogMessage(socket, clientNumber);

    }

    public void run() {

        try {
            outcomeMessage = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream incomeMessage = new ObjectInputStream(socket.getInputStream());

            sendWelcomeMessage(outcomeMessage);
            setUserName(incomeMessage);
            sendOnlineUsers();

            listenOnServer(incomeMessage);
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client #" + clientNumber);
        }
        finally {
            closeSocket();
        }
    }



    private void listenOnServer(ObjectInputStream incomeMessage) throws IOException, ClassNotFoundException {
        while (true) {
            Message input = null;
            if(incomeMessage.available() > 0){
                incomeMessage.readInt();
                input = (Message) incomeMessage.readObject();
            }
            if(input != null){
                ServerClientHandler address = getAddressMessage(input);
                sendMessage(address, input);
            }
        }
    }

    private void setUserName(ObjectInputStream incomeMessage) throws IOException, ClassNotFoundException {
        Message message = (Message) incomeMessage.readObject();
        String name = message.getAuthor();
        server.addClientToMap(name);
        BuiltMessages.setUserNameMessage(clientNumber, name);
    }

    private String getUserInput() {
        System.out.println("\nServer type meesage: ");
        return scanner.nextLine();

    }

    public Socket getSocket() {
        return socket;
    }

    public ServerClientHandler getAddressMessage(Message message) {

        if(server.getServerThread(message.getAddress()) == null){
            message.setAddressCorrect(false);
            return server.getServerThread(message.getAuthor());
        }

        return server.getServerThread(message.getAddress());
    }

    public void sendMessage(ServerClientHandler address, Message message) throws IOException {
        address.outcomeMessage.writeInt(0);

        if(message.isAddressCorrect()){
            address.outcomeMessage.writeObject(new Message(message.getContent(), message.getAuthor()));
        }
        else{
            address.outcomeMessage.writeObject(new Message(
                    "Error there is no such user online", message.getAuthor()));
        }
    }

    private void sendWelcomeMessage(ObjectOutputStream outcomeMessage) throws IOException {
        outcomeMessage.writeInt(0);
        outcomeMessage.writeObject(new Message("server","Hello, you are client #" + clientNumber + "\n" +
                "To disconnect type [exit]"));
    }

    public String getOnlineUsers(){
        return this.server.getOnlineUsers();
    }


    public void sendOnlineUsers(){
       this.server.getMap().forEach((key, client) -> {
           try {
               client.outcomeMessage.writeInt(0);
               client.outcomeMessage.writeObject(new Message(getOnlineUsers(), "Message from server"));
           } catch (IOException e) {
               e.printStackTrace();
           }
       });
    }

    public ObjectOutputStream getOutcomeMessage() {
        return outcomeMessage;
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
}