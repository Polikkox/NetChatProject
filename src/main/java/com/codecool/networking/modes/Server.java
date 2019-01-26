package com.codecool.networking.modes;

import javax.xml.soap.SAAJResult;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private Map<String, ServerThread> map = new HashMap<>();
    private ServerThread serverThread;

    public void runServer(int portNumber){
        System.out.println("The server is running.");
        int clientNumber = 1;
        try (ServerSocket listener = new ServerSocket(portNumber)) {
            while (true) {

                serverThread = new ServerThread(listener.accept(), clientNumber++, this);
                serverThread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket(String clientID){
        return map.get(clientID).getSocket();
    }
    public String getOnlineUsers(){
        StringBuilder sb = new StringBuilder("Users online:\n");

        this.map.entrySet().stream()
                .forEach(key -> {
                    sb.append(key.getKey()).append("\n");
                });
        return sb.toString();
    }
    public void addClientToMap(String name){
        this.map.put(name, serverThread);
    }
    public Map<String, ServerThread> getMap(){
        return this.map;
    }
}
