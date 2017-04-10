package com.example.iapp.models;

/**
 * Created by rahul on 10/04/17.
 */

public class SendGift {

    public String receiverName;
    public String occassionDate;
    public String sentMoney;
    public String occassionName;

    public SendGift(String receiverName, String occassionDate, String sentMoney, String occassionName) {
        this.receiverName = receiverName;
        this.occassionDate = occassionDate;
        this.sentMoney = sentMoney;
        this.occassionName = occassionName;
    }

    public SendGift() {
    }
}
