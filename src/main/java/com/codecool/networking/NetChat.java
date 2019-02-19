package com.codecool.networking;

import com.codecool.networking.modes.Client;
import com.codecool.networking.modes.Server;
import com.codecool.networking.view.SystemMessage;

import java.io.IOException;

public class NetChat {
    final private static int MODE = 0;
    final private static int HOSTNAME = 1;
    final private static int PORTNUMBER = 2;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        if(args.length < 3){
            SystemMessage.failedArgsMessage();
            System.exit(1);
        }
        int port = Integer.valueOf(args[PORTNUMBER]);

        if(args[MODE].equals("server")){
            new Server().runServer(port);
        }
        else if(args[MODE].equals("client")){
            new Client().runClientSocket(args[HOSTNAME], port);
        }
        else{
            SystemMessage.failedModeArgsMessage();
            System.exit(1);
        }

    }
}
