package com.codepath.apps.mytwitter;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {
    ImageView myProfile;
    TextView myName, myUserId;
    TwitterClient client;
    TextView tvFollowers;
    TextView tvFollowings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();
        myProfile = (ImageView) findViewById(R.id.ivProfileImage);
        myName = (TextView)findViewById(R.id.tvProfileName);
        myUserId = (TextView) findViewById(R.id.tvProfileScreenName);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowings = (TextView) findViewById(R.id.tvFollowings);


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
        client.getAccount(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                populateProfileUser(User.fromJson(jsonObject));
            }
        });
    }

    private void populateProfileUser(User user) {
        myProfile.setImageResource(0);
        Picasso.with(this).load(user.getProfileImageUrl()).into(myProfile);
        myName.setText(user.getName());
        myUserId.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowings.setText(user.getFollowingCount() + " Following");
    }
}
