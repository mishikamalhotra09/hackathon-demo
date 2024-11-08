package com.marketaware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

public class TwitterAPICaller {
    
    // Bearer Token - allows the program to access Twitter APIs
    final String bearerToken = "AAAAAAAAAAAAAAAAAAAAAO3OuAEAAAAAdmd7wHB1kYisOZxCaNaWIryZhAA%3DPrZJy6hlGKkiIltJ6IZHYShXfiCQ7Tu2Kj0Bo4SLZWbNwm7jG0";
    
    // callGetUserAPI takes in username and accesses a twitter API to retrieve that username's user ID
    public long callGetUserAPI (String username) {
        long userID = 0;
        if (bearerToken != null){
            // Try/catch accesses the twitter API with it's url
            try{
                String apiUrl = "https://api.twitter.com/2/users/by/username/"+username;
                System.out.println(apiUrl);
                // Uses getUserID which takes in the bearer token and username and returns the userID
                userID = getUserID(username, bearerToken);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } 
        // Returns userID
        return userID;
    }
    // getLikesAPI calls a twitter API to get maxResults number of tweets liked by the userID
    public List<String> getLikesAPI(long userID, int maxResults) {
        String response = "";
        if (bearerToken != null){
            // This puts the likedTweets into response
            try{
                response = getLikedTweets(userID, maxResults, bearerToken);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        // tweets list will be filled with the likedTweets
        List<String> tweets = new ArrayList<>();
        try {
            // Parse JSON response
            JSONObject jsonResponseObj = new JSONObject(response);
            JSONArray tweetsArray = jsonResponseObj.getJSONArray("data");

            // Iterate through each tweet
            for (int i = 0; i < tweetsArray.length(); i++) {
                JSONObject tweet = tweetsArray.getJSONObject(i);
                String text = tweet.getString("text");

                tweets.add("Tweet " + (i + 1) + ": " + text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Returns a list of liked tweets
        return tweets;
    }
    // callTimelineAPI will take in userID and number of results and then return the tweets posted by that ID
    public List<String> callTimelineAPI(long userId, int maxResults) {
        String response = "";
        if (bearerToken != null) {
            // Similarly to getLikedTweets, getTimelineTweets gets the recent tweets in chronological order
            try {
                response = getTimelineTweets(userId, maxResults, bearerToken);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        // Makes a list of recent tweets
        List<String> tweets = new ArrayList<>();
        try {
            // Parse JSON response
            JSONObject jsonResponseObj = new JSONObject(response);
            JSONArray tweetsArray = jsonResponseObj.getJSONArray("data");

            // Iterate through each tweet
            for (int i = 0; i < tweetsArray.length(); i++) {
                JSONObject tweet = tweetsArray.getJSONObject(i);
                String text = tweet.getString("text");

                tweets.add(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returns a list of recent tweets
        return tweets;
    }
    // Used in calTimelineAPI - explained above
    private String getTimelineTweets(long userId, int maxResults, String bearerToken) throws IOException, URISyntaxException {
        String tweetResponse = null;
        // Making httpClient
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        // Calling twitter timeline API
        String apiUrl = "https://api.twitter.com/2/users/" + userId + "/tweets?max_results=" + maxResults;
        System.out.println(apiUrl);

        URIBuilder uriBuilder = new URIBuilder(apiUrl);
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
        uriBuilder.addParameters(queryParameters);
        // authorizing API
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");
        // Sets the tweets to tweetResponse
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            tweetResponse = EntityUtils.toString(entity, "UTF-8");
        }
        // Returns the tweets
        return tweetResponse;
    }
    // Used in getLikesAPI
    private String getLikedTweets(long userId, int maxResults, String bearerToken) throws IOException, URISyntaxException {
        String tweetResponse = null;    
        // Making httpClient
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        //Calling twitter API
        String apiUrl = "https://api.twitter.com/2/users/" + userId + "/liked_tweets?max_results=" + maxResults;
        System.out.println(apiUrl);

        URIBuilder uriBuilder = new URIBuilder(apiUrl);
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
        uriBuilder.addParameters(queryParameters);
        // Authorizes API
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");
        // Sets tweets to tweetResponse
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            tweetResponse = EntityUtils.toString(entity, "UTF-8");
        }
        // returns tweets
        return tweetResponse;
    }
    // Used in callGetUserAPI
    private long getUserID(String username, String bearerToken) throws IOException, URISyntaxException {
        long userID = 1;
        // sets httpClient
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        // Calls twitter API
        String apiUrl = "https://api.twitter.com/2/users/by/username/" + username;
        System.out.println(apiUrl);

        URIBuilder uriBuilder = new URIBuilder(apiUrl);
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
        uriBuilder.addParameters(queryParameters);
        // autorizes API
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");
        // sets userID to userResponse
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String userResponse = "";
        if (null != entity) {
            userResponse = EntityUtils.toString(entity, "UTF-8");
            // Parse JSON string
            JSONObject jsonResponse = new JSONObject(userResponse);

            // Extract id from JSON
            JSONObject data = jsonResponse.getJSONObject("data");
            userID = data.getLong("id");
        }
        // returns userID
        return userID;
    }
}
