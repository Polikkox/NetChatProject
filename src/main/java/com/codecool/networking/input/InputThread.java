package com.codecool.networking.input;

import com.codecool.networking.data.Message;

import java.util.Scanner;

public class InputThread implements Runnable {

    private boolean lock = true;
    private Scanner scanner = new Scanner(System.in);
    private String address;
    private String content;

    @Override
    public void run() {

        System.out.println("Type name to send your message:\n");

        while (lock){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            address = scanner.nextLine();
            System.out.println("Type message:\n");
            content = scanner.nextLine();

            if(address != null && content != null){
                lock = false;
            }
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
