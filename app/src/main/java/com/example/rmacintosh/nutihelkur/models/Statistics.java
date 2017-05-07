package com.example.rmacintosh.nutihelkur.models;

import io.realm.RealmObject;


/**
 * Statistics - object to store data about found sensors
 * String location and locationId - sensor's ID2
 * int count - how many times sensor has captured Nutihelkur's signals - from ID3
 */

public class Statistics extends RealmObject {

    private String location;
    private int locationId;
    private int count;
    private long date;

    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {

        this.count = count;
    }

    public long getDate() {

        return date;
    }

    public void setDate(long date) {

        this.date = date;
    }
}
