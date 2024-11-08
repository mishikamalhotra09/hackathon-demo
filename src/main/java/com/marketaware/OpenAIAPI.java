package com.marketaware;

import java.util.List;

import com.google.gson.Gson; // Import Gson library
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OpenAIAPI {
    // Used to rewite given tweet to improve engagement - uses openAI
    public String improveTweet(String rawTweet, String context) {

        // API endpoint URL
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // OpenAI API key
        String apiKey = "sk-proj-uCJWF2O95wxfzbtIHHgaT3BlbkFJm2WSo3X7SklwpS6G841h";

        // Prompt for AI
        String prompt = "Rewrite this Tweet " + rawTweet + " using conctual information from " + context;
        
        System.out.println("prompt is " + prompt);
        String responseBody = "";

        try {
            // Create HttpClient instance
            HttpClient httpClient = HttpClients.createDefault();

            // Create a Gson object
            Gson gson = new Gson();

            // defines the role of the system and its job
            String requestBody = gson.toJson(new RequestPayload("gpt-4o", "You are a highly skilled twitter marketing agent. Your job is to take in a raw tweet and rewrite it to get the most engagement.  To do this, you will take in context from previous tweets by targeted users and using context information.", prompt));

            System.out.println("requestBody is " + requestBody);

            // Set request body
            StringEntity requestEntity = new StringEntity(requestBody);

            // Create HTTP POST request
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setEntity(requestEntity);

            // Set headers (authorizaton)
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + apiKey);

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            // Get response entity
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Convert response entity to string
                responseBody = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // returns improved tweet
        return responseBody;
    }
}
