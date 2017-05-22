package com.example.rmacintosh.nutihelkur.utils;

import com.example.rmacintosh.nutihelkur.dao.SensorDao;
import com.example.rmacintosh.nutihelkur.dao.StatisticsDao;
import com.example.rmacintosh.nutihelkur.models.Sensor;
import com.example.rmacintosh.nutihelkur.models.Statistics;

import io.realm.Realm;

/**
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

public class RealmManager {

    private static Realm mRealm;

    public static Realm open() {
        mRealm = Realm.getDefaultInstance();
        return mRealm;
    }

    public static void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    public static SensorDao createSensorDao() {
        checkForOpenRealm();
        return new SensorDao(mRealm);
    }

    public static StatisticsDao createStatisticsDao() {
        checkForOpenRealm();
        return new StatisticsDao(mRealm);
    }

    public static void clear() {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Sensor.class);
                realm.delete(Statistics.class);
                //clear rest of your dao classes
            }
        });
    }

    private static void checkForOpenRealm() {
        if (mRealm == null || mRealm.isClosed()) {
            throw new IllegalStateException("RealmManager: Realm is closed, call open() method first");
        }
    }
}
