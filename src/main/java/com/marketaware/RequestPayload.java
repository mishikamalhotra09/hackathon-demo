package com.marketaware;

// Define a class to represent the structure of the JSON payload
public class RequestPayload {
    String model;
    Message[] messages;

    RequestPayload(String model, String systemContent, String userContent) {
        this.model = model;
        this.messages = new Message[] { new Message("system", systemContent), new Message("user", userContent) };
    }
}
