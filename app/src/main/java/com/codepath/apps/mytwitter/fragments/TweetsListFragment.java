package com.codepath.apps.mytwitter.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.EndlessScrollListener;
import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TweetDetailActivity;
import com.codepath.apps.mytwitter.TweetsArrayAdapter;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.models.Tweet;

import java.util.ArrayList;

/**
 * Created by lniu on 2/13/15.
 */
public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweetList;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetList = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweetList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsArrayAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownToUpdate(tweetsArrayAdapter);

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                scrollDownToUpdate(tweetsArrayAdapter);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (lvTweets == null || lvTweets.getChildCount() == 0) ?
                                0 : lvTweets.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TweetDetailActivity.class);

                Tweet result = tweetList.get(position);
                showToast(result.getUser().getProfileImageUrl());
                intent.putExtra("tweet", result);
                startActivity(intent);
            }
        });
        return view;
    }

    void pullDownToUpdate(TweetsArrayAdapter tweetList) {
        showToast("not implemented yet");
    }

    void scrollDownToUpdate(TweetsArrayAdapter tweetList) {
        showToast("not implemented yet");
    }

    void stopSwipeListProgressBar() {
        swipeContainer.setRefreshing(false);
    }


    TweetsArrayAdapter getArrayAdapter() {
        return this.tweetsArrayAdapter;
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
