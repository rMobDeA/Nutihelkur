package com.example.rmacintosh.nutihelkur.models;

import io.realm.RealmObject;

/**
 * Created by rmacintosh on 03/05/2017.
 */

public class Statistics extends RealmObject{

    private String location;
    private int count;
    private String date;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
