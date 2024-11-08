package com.marketaware;

    // Defines a class to represent each message in the payload
    public class Message {
        String role;
        String content;

        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
