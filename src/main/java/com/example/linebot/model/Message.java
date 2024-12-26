package com.example.linebot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Message {

    @Id
    private String userId;  // ทำให้ userId เป็น Primary Key ของ Message

    private String text;

    public Message() {
        // constructor ว่างสำหรับ JPA
    }

    public Message(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    // Getter และ Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{userId='" + userId + "', text='" + text + "'}";
    }
}
