package com.codepath.apps.mytwitter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.TweetsArrayAdapter;
import com.codepath.apps.mytwitter.models.Tweet;

import java.util.ArrayList;

/**
 * Created by lniu on 2/13/15.
 */
public class TweetsListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        return view;
    }
}
