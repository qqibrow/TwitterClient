package com.codepath.apps.mytwitter.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by lniu on 2/4/15.
 */

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {
    private long retweetCount;
    private long favouritesCount;

    public Tweet() {
        super();
    }

    @Column(name = "body")
    private String body;

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "createdAtLong")
    private  long createAtLong;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
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
            // Log.d("Tweet", jsonObject.toString());
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.createAtLong = getRelativeTimeAgo(tweet.createdAt);
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getLong("retweet_count");
            if(jsonObject.has("favorite_count"))
                tweet.favouritesCount = jsonObject.getLong("favorite_count");
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

    public static List<Tweet> fromDatabase() {
        return new Select()
                .from(Tweet.class)
                .orderBy("createdAtLong DESC")
                .execute();

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static long getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        long dateMillis = 0;
        try {
            dateMillis = sf.parse(rawJsonDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateMillis;
    }

    public User getUser() {
        return user;
    }

    public long getRetweetCount() {
        return retweetCount;
    }

    public long getFavouritesCount() {
        return favouritesCount;
    }
}
