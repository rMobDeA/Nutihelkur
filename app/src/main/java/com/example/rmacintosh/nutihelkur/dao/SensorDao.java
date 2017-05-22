package com.example.rmacintosh.nutihelkur.dao;

import android.support.annotation.NonNull;

import com.example.rmacintosh.nutihelkur.models.Sensor;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
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

public class SensorDao {

    private Realm mRealm;

    public SensorDao(@NonNull Realm realm) {

        mRealm = realm;
    }


    public RealmResults<Sensor> loadAll() {

        return mRealm.where(Sensor.class).findAll();
    }

    public String loadSensorLocation(String id) {
        return mRealm.where(Sensor.class).equalTo("sensorId", id).findFirst().getLocation();
    }

    public boolean isValidSensor(String searchId) {
       if (mRealm.where(Sensor.class).equalTo("sensorId", searchId).findFirst() != null) {
           return true;
       } else {
           return false;
       }
    }

    public String getSensorLocation(int locationId) {
        if (mRealm.where(Sensor.class).equalTo("locationId", locationId).count() != 0) {
            return mRealm.where(Sensor.class).equalTo("locationId", locationId).findFirst().getLocation();
        } else {
            return "ERROR, or sensor UUID has been cloned!";
        }
    }


    public void save(final Sensor sensor) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(sensor);
            }
        });
    }


    /**
     * @param preDefSenorList from SensorsGenerator
     */
    public void savePreDefSenorList(final List<Sensor> preDefSenorList) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(preDefSenorList);
            }
        });
    }

    public void removeAll() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.delete(Sensor.class);
            }
        });
    }

    public long count() {

        return mRealm.where(Sensor.class).count();
    }



}
