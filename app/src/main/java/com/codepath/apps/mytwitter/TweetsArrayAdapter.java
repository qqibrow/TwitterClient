package com.codepath.apps.mytwitter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by lniu on 2/4/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private ImageView ivProfile;
    private TextView tvUsername;
    private TextView tvBody;
    private TextView tvTimestamp;
    private TextView screenName;
    private TextView tvStar, tvRetweet;


    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet, parent, false);
        }

        ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvBody = (TextView)convertView.findViewById(R.id.tvBody);
        tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
        screenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        tvStar = (TextView) convertView.findViewById(R.id.tvStar);
        tvRetweet = (TextView) convertView.findViewById(R.id.tvRetweet);

        tvRetweet.setText("");
        long retweet_count = tweet.getRetweetCount();
        if(retweet_count != 0 && !tweet.getUser().getScreenName().equals("lu_niu") )
            tvRetweet.setText(String.valueOf(retweet_count));

        tvStar.setText("");
        long star_count = tweet.getFavouritesCount();
        if(star_count != 0)
            tvStar.setText(String.valueOf(star_count));

        tvUsername.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        screenName.setText("@" + tweet.getUser().getScreenName());
        ivProfile.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TweetsArrayAdapter.this.getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                i.putExtra("user", tweet.getUser());
                TweetsArrayAdapter.this.getContext().startActivity(i);
            }
        });

        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            String original = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            String pattern = "(\\d+)(\\s+)([a-z]).*";
            relativeDate = original.replaceAll(pattern, "$1$3");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
