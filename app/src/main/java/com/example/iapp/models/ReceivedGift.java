package com.example.iapp.models;

/**
 * Created by rahul on 10/04/17.
 */

public class ReceivedGift {


    public String senderName;
    public String occassionDate;
    public String sentMoney;
    public String occassionName;

    public ReceivedGift(String senderName, String occassionDate, String sentMoney, String occassionName) {
        this.senderName= senderName;
        this.occassionDate = occassionDate;
        this.sentMoney = sentMoney;
        this.occassionName = occassionName;
    }

    public ReceivedGift() {
    }

}
