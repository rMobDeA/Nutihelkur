package com.example.rmacintosh.nutihelkur.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by rmacintosh on 03/05/2017.
 */

public class Sensor extends RealmObject {


    private String sensorId;
    private String location;

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

    //oneliner setter?
}
