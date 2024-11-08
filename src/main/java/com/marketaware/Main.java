package com.marketaware;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // initializes twtterAPICaller (the API class)
        TwitterAPICaller apiCaller = new TwitterAPICaller();
        // initalizes openAiAPI (openAI class)
        OpenAIAPI openAI = new OpenAIAPI();
        List<String> responseTweets;
        List<String> responseTweetsLiked;
        List<String> responseTweetsCummulative = new ArrayList<>();
        long userID;

        // calls UserID API, sets the value to userID, and prints it
        userID = apiCaller.callGetUserAPI("vik425as");
        System.out.println(userID);

        // calls timeline tweets API, sets the value to responseTweets, and prints it
        responseTweets = apiCaller.callTimelineAPI(userID, 15);
        System.out.println(responseTweets);

        // calls liked tweets API, sets the value to responseTweetsLiked, and prints it
        responseTweetsLiked = apiCaller.getLikesAPI(userID, 15);
        System.out.println("getLikesAPI :" + responseTweetsLiked);

        // Puts all the tweets into a cummulative list
        for (int i = 0; i < responseTweets.size(); i++) {
            responseTweetsCummulative.add(responseTweets.get(i));
        }
        for (int i = 0; i < responseTweetsLiked.size(); i++) {
            responseTweetsCummulative.add(responseTweetsLiked.get(i));
        }

        // Format contextString (cummulative tweets) properly as a JSON array
        StringBuilder contextArrayBuilder = new StringBuilder();
        for (int i = 0; i < responseTweetsCummulative.size(); i++) {
            contextArrayBuilder.append(responseTweetsCummulative.get(i));
            if (i < responseTweetsCummulative.size() - 1) {
                contextArrayBuilder.append(",");
            }
        }

        // sets the context string for openAI
        String contextString = contextArrayBuilder.toString();

        // the tweet to be rewritten
        String rawTweet = "I am going out to eat Kababs at this new restaurant i found. ";

        // Calls openAI to rewrite the rawTweet using the contextString
        String improvedTweet = openAI.improveTweet(rawTweet, contextString);

        // Prints the improved tweet
        System.out.println(improvedTweet);

    }
}
