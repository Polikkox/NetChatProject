package com.codecool.networking;

import java.util.Scanner;

public class ScannerThread implements Runnable {

    private boolean lock = true;
    private String input = null;
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        if(lock == false){
            System.out.println("\nEnter a string to send to the server (empty to quit):");
        }
        while (lock){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            input = scanner.nextLine();
            if(input != null){
                System.out.println("\nEnter a string to send to the server (empty to quit):");
                lock = false;
            }
        }
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
