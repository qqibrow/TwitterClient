package com.codepath.apps.mytwitter;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class TweetDetailActivity extends ActionBarActivity {

    private TextView tvUsername;
    private TextView tvScreenName;
    private TextView tvText;
    private EditText etInput;
    private ImageView ivProfile;

    private Tweet tweet;

    private TwitterClient client;
    private void setActionbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("   Tweet");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF3F9FE0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        client = TwitterApplication.getRestClient();
        setActionbar();
        tvUsername = (TextView) findViewById(R.id.tvDetailTweetName);
        tvScreenName = (TextView) findViewById(R.id.tvDetailScreenName);

        etInput = (EditText) findViewById(R.id.etDetailInput);
        ivProfile = (ImageView) findViewById(R.id.ivDetailProfile);
        tvText = (TextView) findViewById(R.id.tvDetailText);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        tvUsername.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvText.setText(tweet.getBody());
        etInput.setHint("Reply to " + tweet.getUser().getName());
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfile);

        etInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("@" + tweet.getUser().getScreenName() + " ");
                etInput.setSelection(etInput.getText().length());
            }
        });


    }

    public void onClick(View view) {
        String input = etInput.getText().toString();
        if(input.isEmpty()) {
            showToast("Input cannot be empty");
            return;
        }
        ReTweet(input, String.valueOf(tweet.getUid()));
    }

    private void ReTweet(String text, String uid) {
        client.postReTweet(text, uid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showToast("Post succeeded.");
                etInput.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        TweetDetailActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                showToast("Post failed");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
