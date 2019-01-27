package com.codecool.networking.handler;

import com.codecool.networking.data.Message;
import com.codecool.networking.input.InputThread;

public class InputHandler {
    private boolean lock = true;
    private InputThread inputThread;

    public Message getInput() {
        //Run only one thread waiting for user input
        if(lock) {
            inputThread = new InputThread();
            Thread thread = new Thread(inputThread);
            thread.start();
            lock = false;
        }

        //Enable take another one input after got input
        if(inputThread.getContent() != null){
            lock = true;
            return messageHandler();
        }
        return null;
    }

    private Message messageHandler(){
        Message message = new Message();
        message.setContent(inputThread.getContent());
        message.setAddress(inputThread.getAddress());
        inputThread.setContent(null);
        inputThread.setAddress(null);
        return message;
    }
}
