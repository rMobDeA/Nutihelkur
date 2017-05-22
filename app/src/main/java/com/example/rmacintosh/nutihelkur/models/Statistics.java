package com.example.rmacintosh.nutihelkur.models;

import io.realm.RealmObject;

/**
 *      Statistics - object to store data about found sensors
 *      String location and locationId - sensor's ID2
 *      int count - how many times sensor has captured Nutihelkur's signals - from ID3
 *
 *                  created by Rauno Pügi
 *
 *       significant help for learning and understanding processes:
 *       - https://github.com/AltBeacon/android-beacon-library-reference
 *       - https://github.com/uriio/beacons-android
 *       - https://github.com/beaconinside/awesome-beacon
 *       - https://github.com/BoydHogerheijde/Beacon-Scanner
 *       - https://github.com/Bridouille/android-beacon-scanner
 *       - https://github.com/justinodwyer/Beacon-Scanner-and-Logger
 *
 *        thanks to Radius Networks for providing a great beacon library,
 *        support and information
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
