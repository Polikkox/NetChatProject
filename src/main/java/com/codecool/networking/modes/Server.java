package com.codecool.networking.modes;

import com.codecool.networking.handler.ServerClientHandler;
import com.codecool.networking.view.SystemMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private Map<String, ServerClientHandler> map = new HashMap<>();
    private ServerClientHandler serverClientHandler;

    public void runServer(int portNumber){
        SystemMessage.printMessage("The server is running.");
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

    public void removeUser(String name){
        this.map.remove(name);
    }

    public ServerClientHandler getServerThread(String client){
        return map.get(client);
    }

    public String getOnlineUsers(){
        StringBuilder sb = new StringBuilder("Users online:\n");

        this.map.forEach((key1, value) -> sb.append(key1).append("\n"));
        return sb.toString();
    }

    public void addClientToMap(String name){
        this.map.put(name, serverClientHandler);
    }

    public Map<String, ServerClientHandler> getMap(){
        return this.map;
    }
}
