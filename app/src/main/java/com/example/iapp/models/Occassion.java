package com.example.iapp.models;

/**
 * Created by rahul on 04/04/17.
 */

public class Occassion {

    public String date;
    public Boolean isFriendInvited;
    public String time;
    public String description;
    public String occassionName;


    public Occassion()
    {
    }

    public Occassion(String date, Boolean isFriendInvited, String time, String description) {
        this.date = date;
        this.isFriendInvited = isFriendInvited;
        this.time = time;
        this.description = description;
    }
}
