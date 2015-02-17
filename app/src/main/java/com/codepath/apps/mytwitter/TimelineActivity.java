package com.codepath.apps.mytwitter;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mytwitter.fragments.HomeTimeLineFragment;
import com.codepath.apps.mytwitter.fragments.MentionsTimeLineFragment;

public class TimelineActivity extends ActionBarActivity {

    private int REQUEST_CODE = 1;


    private void setActionbar() {
        // the three necessary step to setup the icon.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // setup title
        getSupportActionBar().setTitle("   Home");
        // Set background Color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF3F9FE0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setActionbar();

        ViewPager vpager = (ViewPager) findViewById(R.id.viewpager);
        vpager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(vpager);

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
            //Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
            //intent.putExtra("Myself", currentUser);
            //startActivityForResult(intent, REQUEST_CODE);
            return true;
        } else if(id == R.id.action_profile) {
            onProfileView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onProfileView() {
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(i);
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            //Tweet composed_tweet = (Tweet) data.getSerializableExtra("Tweet");
            //tweetsArrayAdapter.insert(composed_tweet, 0);
            //tweetsArrayAdapter.notifyDataSetChanged();
        }
    }


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if(position == 0) {
                return new HomeTimeLineFragment();
            } else if(position == 1) {
                return new MentionsTimeLineFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
