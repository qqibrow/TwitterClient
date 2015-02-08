package com.codepath.apps.mytwitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lniu on 2/4/15.
 */
public class Tweet {
    private String body;
    private long uid;
    private String createdAt;
    private User user;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> arrayList = new ArrayList<>();
        try {
        for(int i = 0; i < jsonArray.length(); ++i) {
                arrayList.add(fromJson(jsonArray.getJSONObject(i)));

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public User getUser() {
        return user;
    }
}
