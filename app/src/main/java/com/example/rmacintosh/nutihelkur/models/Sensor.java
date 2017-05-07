package com.example.rmacintosh.nutihelkur.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * String sensorId - AltBeacon UUID
 * int locationId - AltBeacon ID2, max value 65 536
 * String location - location name that connects to ID2
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
