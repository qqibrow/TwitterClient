package com.codepath.apps.mytwitter;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.R;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {

    private TwitterClient client;
    private ImageView myProfile;
    private TextView myName;
    private TextView myUserId;
    private EditText input;

    private TextView tvCharLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();

        User myself = (User)getIntent().getSerializableExtra("Myself");
        Log.d("myself", myself.toString());

        myProfile = (ImageView) findViewById(R.id.ivMyProfile);
        myName = (TextView)findViewById(R.id.tvMyName);
        myUserId = (TextView) findViewById(R.id.tvMyId);
        input = (EditText) findViewById(R.id.etText);

        myProfile.setImageResource(0);
        Picasso.with(this).load(myself.getProfileImageUrl()).into(myProfile);
        myName.setText(myself.getName());
        myUserId.setText(myself.getScreenName());

        input.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                int len = s.length();
                if(tvCharLeft == null)
                    Log.d("ComposeActivity", "tvCharleft is null");
                else
                tvCharLeft.setText(String.valueOf(len) + " / " + String.valueOf(140));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }


    private void postTweet(String text) {
        client.postTweet(text, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("ComposeActivity", "post succeed.");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        MenuItem menuItem = menu.findItem(R.id.action_leftchars);
        tvCharLeft= (TextView)MenuItemCompat.getActionView(menuItem);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            String text = input.getText().toString();
            if(text.matches("")) {
                Toast.makeText(this, "Input should not be empty", Toast.LENGTH_SHORT).show();
                return true;
            }
            postTweet(text);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
