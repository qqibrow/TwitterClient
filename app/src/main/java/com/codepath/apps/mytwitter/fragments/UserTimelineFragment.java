package com.codepath.apps.mytwitter.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mytwitter.TweetsArrayAdapter;
import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lniu on 2/16/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        // below are network request.
        if(isNetworkAvailable()) {
            final long since_id = 1; // get tweets since 1.
            populateHomeTimeline(-1, -1, getArrayAdapter(), false);
            //SetCurrentUser();
        } else {
            showToast("Network not available");
            List<Tweet> list = Tweet.fromDatabase();
            showToast(String.valueOf(list.size()));
            getArrayAdapter().addAll(list);

        }
    }

    @Override
    void pullDownToUpdate(TweetsArrayAdapter tweetList) {
        Toast.makeText(getActivity(), "update", Toast.LENGTH_SHORT).show();

        // set current since_id(newest time)
        if (tweetList.isEmpty()) {
            showToast("Tweet List is empty");
            return;
        }
        long newestId = tweetList.getItem(0).getUid();
        //long lastId = tweetList.get(tweetList.size() - 1).getUid();
        //showToast(String.valueOf(newestId) + "  " + String.valueOf(lastId));
        populateHomeTimeline(newestId, -1, tweetList, true);

    }

    @Override
    void scrollDownToUpdate(TweetsArrayAdapter tweetList) {
        long max_id = tweetList.getItem(tweetList.getCount()-1).getUid() - 1;
        populateHomeTimeline(-1, max_id, tweetList, false);
    }

    private void populateHomeTimeline(long since_id, long max_id, final TweetsArrayAdapter tweetsArrayAdapter,
                                      final boolean recursive) {
        String scree_name = getArguments().getString("screen_name");
        client.getUserTimeline(scree_name, since_id, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                // find max_id and since_id
                if(tweets.isEmpty()) {
                    showToast("Up to date now");
                } else {
                    // persistent.
                    for(Tweet tweet: tweets) {
                        tweet.getUser().save();
                        tweet.save();
                    }
                    if(recursive) {
                        // From tail to front add to least
                        Collections.reverse(tweets);
                        for (Tweet tweet : tweets) {
                            tweetsArrayAdapter.insert(tweet, 0);
                        }
                        // Stop showing progress bar.
                    } else {
                        tweetsArrayAdapter.addAll(tweets);
                    }

                }
                if(recursive)
                    stopSwipeListProgressBar();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.i("timeline activity", "failed");
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }
}
