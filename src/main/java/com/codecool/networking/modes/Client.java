package com.codecool.networking.modes;

import com.codecool.networking.handler.InputHandler;
import com.codecool.networking.data.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Message message;
    private InputHandler inputHandler;
    private String name;

    public Client(){
        this.inputHandler = new InputHandler();
        this.name = getName();
    }

    public void runClientSocket(String hostname, int portNumber) throws IOException, ClassNotFoundException {

        ObjectOutputStream outcomeMessage = null;
        ObjectInputStream incomeMessage = null;

        try {
            Socket socket = new Socket(hostname, portNumber);
            incomeMessage = new ObjectInputStream(socket.getInputStream());
            outcomeMessage = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sentNameOfClientToServer(outcomeMessage, name);

        while (true) {
            if(incomeMessage.available() > 0){
                incomeMessageHandler(incomeMessage);
            }
            handleOutcomeMessage(outcomeMessage);
        }
    }

    private void handleOutcomeMessage(ObjectOutputStream outcomeMessage) throws IOException {
        prepareMessage();

        if(this.message != null){
            message.setAuthor(name);
            outcomeMessage.writeInt(0);
            outcomeMessage.writeObject(message);
            message = null;
        }
    }

    private void sentNameOfClientToServer(ObjectOutputStream outcomeMessage, String name) throws IOException {
        outcomeMessage.writeObject(new Message("content", name));
    }

    private void incomeMessageHandler(ObjectInputStream incomeMessage) throws IOException, ClassNotFoundException {
        incomeMessage.readInt();
        Message inputMessage = (Message) incomeMessage.readObject();
        System.out.println(inputMessage.getCreatedAt() + "\n" + inputMessage.getAuthor() + ": " + inputMessage.getContent());
    }

    private void prepareMessage() {
        this.message =  inputHandler.getInput();
    }

    private String getName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello user enter your name: ");
        return scanner.nextLine();
    }
}