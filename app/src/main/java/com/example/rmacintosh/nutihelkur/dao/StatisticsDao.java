package com.example.rmacintosh.nutihelkur.dao;

import android.support.annotation.NonNull;

import com.example.rmacintosh.nutihelkur.models.Statistics;

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

public class StatisticsDao {

    private Realm mRealm;

    public StatisticsDao(@NonNull Realm realm) {
        mRealm = realm;
    }

    public void save(final Statistics stat) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(stat);
            }
        });
    }

    public RealmResults<Statistics> loadAll() {
        return mRealm.where(Statistics.class).findAll();
    }

    public void removeAll() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.delete(Statistics.class);

            }
        });
    }


    public long count() {
        return mRealm.where(Statistics.class).count();
    }


    public void writeToDatabase(final String sensorLocation,
                                final int locationId,
                                final long time,
                                final int usage) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Statistics stat = mRealm.createObject(Statistics.class);
                stat.setLocation(sensorLocation);
                stat.setLocationId(locationId);
                stat.setDate(time);
                stat.setCount(usage);
            }
        });
    }

    /**
     * @param locationId         location where sensor was located
     * @param sensorLocationTime time when sensor was located in seconds since 1970
     * @return boolean, if that sensor has been located in that location before during 120 seconds
     *
     * // should have used realm.where(object).max("id").intValue()
     */


    public boolean notInDatabase(int locationId, long sensorLocationTime) {
        boolean needsWriting = false;
        //  defining time value that helps to prevent writing duplicates during 120 seconds
        long maxTimeDatabase = sensorLocationTime - 120;
        //  does DB has entries about located sensor?
        long notEmpty = mRealm.where(Statistics.class).equalTo("locationId", locationId).count();
            if (notEmpty != 0) {
                // does DB has entries about that sensor which was made less than 120 seconds ago
                long inDatabase = mRealm.where(Statistics.class)
                        .equalTo("locationId", locationId)
                        .greaterThan("date", maxTimeDatabase)
                        .count();
                if (inDatabase == 0) {
                    needsWriting = true;
                } else {
                    needsWriting = false;
                }
        }
        if (notEmpty == 0) {
            needsWriting = true;
        }

        return needsWriting;
    }
}
