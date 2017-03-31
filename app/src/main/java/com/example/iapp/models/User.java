package com.example.iapp.models;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by rahul on 31/03/17.
 */
@IgnoreExtraProperties
public class User {

    public String displayName;
    public String photoUrl;
    public String email;

    public User() {
    }

    public User(String displayName, String photoUrl, String email) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}
