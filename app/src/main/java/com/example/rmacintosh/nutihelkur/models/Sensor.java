package com.example.rmacintosh.nutihelkur.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 *       String sensorId - AltBeacon UUID
 *       int locationId - AltBeacon ID2, max value should be 65 536
 *       String location - location name that connects to ID2
 *
 *                    created by Rauno Pügi
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

public class Sensor extends RealmObject {


    private String sensorId;
    private int locationId;
    private String location;

    public int getLocationId() {

        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getSensorId() {

        return sensorId;
    }

    public String getLocation() {

        return location;
    }

    public void setSensorId(String sensorId) {

        this.sensorId = sensorId;
    }

    public void setLocation(String location) {

        this.location = location;
    }

}
