package com.example.rmacintosh.nutihelkur.dao;

import android.support.annotation.NonNull;

import com.example.rmacintosh.nutihelkur.models.Sensor;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rmacintosh on 04/05/2017.
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