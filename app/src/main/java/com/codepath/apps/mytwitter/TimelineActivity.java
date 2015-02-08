package com.codepath.apps.mytwitter;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private List<Tweet> tweetList;
    private TweetsArrayAdapter tweetsArrayAdapter;

    private ListView lvTweets;

    private long max_id = 0;
    private long since_id = 1;

    private User currentUser;

    private int REQUEST_CODE = 1;

    private SwipeRefreshLayout swipeContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(TimelineActivity.this, "update", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateHomeTimeline();
                // or customLoadMoreDataFromApi(totalItemsCount);
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

        client = TwitterApplication.getRestClient();

        tweetList = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(this, tweetList);
        lvTweets.setAdapter(tweetsArrayAdapter);


        populateHomeTimeline();

        SetCurrentUser();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(since_id, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                // find max_id and since_id
                max_id = tweets.get(tweets.size() - 1).getUid()-1;
                tweetsArrayAdapter.addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.i("timeline activity", "failed");
            }
        });
    }

    private void SetCurrentUser() {
        client.getAccount(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                currentUser = User.fromJson(jsonObject);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
            intent.putExtra("Myself", currentUser);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet composed_tweet = (Tweet) data.getSerializableExtra("Tweet");
            tweetsArrayAdapter.insert(composed_tweet, 0);
            tweetsArrayAdapter.notifyDataSetChanged();
        }
    }
}