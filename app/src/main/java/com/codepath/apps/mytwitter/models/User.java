package com.codepath.apps.mytwitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lniu on 2/4/15.
 */

@Table(name = "Users")
public class User extends Model implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "scree_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public User() {
        super();
    }

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            String image_url = jsonObject.getString("profile_image_url");
            String biggerImageUrl = image_url.replace("_normal", "_bigger");
            user.profileImageUrl = biggerImageUrl;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public String toString() {
        return getName() + getUid() + getScreenName();
    }
}
