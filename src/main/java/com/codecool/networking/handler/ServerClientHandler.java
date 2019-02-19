package com.codecool.networking.handler;

import com.codecool.networking.data.Message;
import com.codecool.networking.modes.Server;
import com.codecool.networking.view.SystemMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientHandler extends Thread {
    private Socket socket;
    private int clientNumber;
    private String clientName;
    private Server server;
    private ObjectOutputStream outcomeMessage;


    public ServerClientHandler(Socket socket, int clientNumber, Server server) {
        this.server = server;
        this.socket = socket;
        this.clientNumber = clientNumber;
        SystemMessage.newUserLogMessage(socket, clientNumber);
    }

    public void run() {
        try {
            outcomeMessage = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream incomeMessage = new ObjectInputStream(socket.getInputStream());

            setUserName(incomeMessage);
            sendWelcomeMessage(outcomeMessage);

            sendOnlineUsers();
            listenOnServer(incomeMessage);
        }
        catch (IOException | ClassNotFoundException e) {
            SystemMessage.printMessage("Error handling client #" + clientNumber);
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
                if(checkIfUserWantToDisconnect(input)){
                    break;
                }
            }

            if(input != null){
                ServerClientHandler address = getAddressMessage(input);
                sendMessage(address, input);
            }
        }
    }

    private boolean checkIfUserWantToDisconnect(Message input) throws IOException {
        if(input.getContent().equals("exit") || input.getAddress().equals("exit")){
            sendMessage();
            this.server.removeUser(this.clientName);
            sendOnlineUsers();
            return true;
        }
        return false;
    }

    private void setUserName(ObjectInputStream incomeMessage) throws IOException, ClassNotFoundException {
        Message message = (Message) incomeMessage.readObject();
        String name = message.getAuthor();
        this.clientName = name;
        this.server.addClientToMap(name);
        SystemMessage.setUserNameMessage(clientNumber, name);
    }

    public ServerClientHandler getAddressMessage(Message message) {
        if(server.getServerThread(message.getAddress()) == null){
            if(!message.getAddress().equals("all")){
                message.setAddressCorrect(false);
                return server.getServerThread(message.getAuthor());
            }
        }

        return server.getServerThread(message.getAddress());
    }

    public void sendMessage(ServerClientHandler address, Message message) throws IOException {

        if(message.isAddressCorrect()){
            if(message.getAddress().equals("all")){
                sendToAllUsers(message);
            }
            else{
                address.outcomeMessage.writeInt(0);
                address.outcomeMessage.writeObject(new Message(message.getContent(), message.getAuthor()));
            }

        }
        else{
            address.outcomeMessage.writeInt(0);
            address.outcomeMessage.writeObject(new Message(
                    "Error there is no such user online", message.getAuthor()));
        }
    }

    public void sendMessage() throws IOException {
        this.outcomeMessage.writeInt(0);
        Message message = new Message("Thank you for chatting!\nYou have been disconnected",
                "server");
        message.setAddressCorrect(false);
        this.outcomeMessage.writeObject(message);
    }

    private void sendWelcomeMessage(ObjectOutputStream outcomeMessage) throws IOException {
        outcomeMessage.writeInt(0);
        outcomeMessage.writeObject(new Message("","Hello, you are client #" + clientNumber + "\n" +
                "\n1.To send message to all users type [all]\n" +
                "2.To disconnect type [exit]\n" +
                "3.To send your message to particular user, first type the clientName and then type your message"));
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
               e.getMessage();
           }
       });
    }

    public void sendToAllUsers(Message message){
        this.server.getMap().forEach((key, client) -> {
            try {
                client.outcomeMessage.writeInt(0);
                client.outcomeMessage.writeObject(new Message(message.getContent(),
                        ("Message to all:\n" + message.getAuthor())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void closeSocket() {
        try {
            socket.close();
        }
        catch (IOException e) {
            SystemMessage.printMessage("Error closing socket ");
        }
        SystemMessage.printMessage("Connection with client # " + clientNumber + " closed");
    }
}