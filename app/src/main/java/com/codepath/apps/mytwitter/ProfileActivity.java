package com.codepath.apps.mytwitter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {
    ImageView myProfile;
    TextView myName, tagline;
    RelativeLayout rlHeader;
    TwitterClient client;
    TextView tvFollowers,tvFollowings, tvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setActionbar();
        client = TwitterApplication.getRestClient();
        myProfile = (ImageView) findViewById(R.id.ivProfileImage);
        myName = (TextView)findViewById(R.id.tvProfileName);
        tagline = (TextView) findViewById(R.id.tvTagline);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowings = (TextView) findViewById(R.id.tvFollowings);
        tvTweets = (TextView) findViewById(R.id.tvTweets);
        rlHeader = (RelativeLayout)findViewById(R.id.rlUserHeader);

        SetCurrentUser();
        String screen_name = getIntent().getStringExtra("screen_name");

        if(savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screen_name);
            // display fragment into the container
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }

    }

    private void setActionbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("   Profile");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF3F9FE0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private void SetCurrentUser() {
        User user = (User)getIntent().getSerializableExtra("user");
        if(user == null) {
            client.getAccount(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    populateProfileUser(User.fromJson(jsonObject));
                }
            });
        } else {
            populateProfileUser(user);
        }

    }

    private void populateProfileUser(User user) {
        myProfile.setImageResource(0);
        Picasso.with(this).load(user.getProfileImageUrl()).into(myProfile);
        myName.setText(user.getName());
        tagline.setText(user.getScreenName());
        tvFollowers.setText(String.valueOf(user.getFollowersCount()));
        tvFollowings.setText(String.valueOf(user.getFollowingCount()));
        tvTweets.setText(String.valueOf(user.getTweetsCount()));

        /* not work at this time.
        Picasso.with(ProfileActivity.this).load(user.getBackgroundUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Toast.makeText(ProfileActivity.this, "loaded bitmap", Toast.LENGTH_LONG).show();
                ProfileActivity.this.rlHeader.setBackground(
                        new BitmapDrawable(ProfileActivity.this.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        */
    }
}
