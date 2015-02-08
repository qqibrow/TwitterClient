package com.codepath.apps.mytwitter;

import android.content.Context;
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


    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet, parent, false);
        }

        ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvBody = (TextView)convertView.findViewById(R.id.tvBody);
        tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);

        tvUsername.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        ivProfile.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfile);

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
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
