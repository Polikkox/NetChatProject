package com.codecool.networking.modes;

import com.codecool.networking.handler.ServerClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private Map<String, ServerClientHandler> map = new HashMap<>();
    private ServerClientHandler serverClientHandler;

    public void runServer(int portNumber){
        System.out.println("The server is running.");
        int clientNumber = 1;

        try (ServerSocket listener = new ServerSocket(portNumber)) {
            while (true) {
                serverClientHandler = new ServerClientHandler(listener.accept(), clientNumber++, this);
                serverClientHandler.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(String clientID){

        return map.get(clientID).getSocket();
    }

    public ServerClientHandler getServerThread(String client){
        return map.get(client);
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
        this.map.put(name, serverClientHandler);
    }

    public Map<String, ServerClientHandler> getMap(){
        return this.map;
    }
}
