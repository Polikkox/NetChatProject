package com.codecool.networking.data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable{
    private String content;
    private String author;
    private String address;
    private LocalDateTime createdAt;
    private boolean isAddressCorrect = true;

    public Message(String content, String author) {
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
    public Message(String content, String author, String address) {
        this.content = content;
        this.author = author;
        this.address = address;
        this.createdAt = LocalDateTime.now();
    }
    public Message(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        int year = createdAt.getYear();
        int month = createdAt.getMonthValue();
        int day = createdAt.getDayOfMonth();
        int hour = createdAt.getHour();
        int minute = createdAt.getMinute();
        int second = createdAt.getSecond();
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-").append(month).append("-").append(day).append(" ")
                .append(hour).append(":").append(minute).append(":").append(second);
        return sb.toString();
    }

    public boolean isAddressCorrect() {
        return isAddressCorrect;
    }

    public void setAddressCorrect(boolean addressCorrect) {
        this.isAddressCorrect = addressCorrect;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
