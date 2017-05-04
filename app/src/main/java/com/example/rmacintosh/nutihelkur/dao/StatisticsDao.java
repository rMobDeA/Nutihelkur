package com.example.rmacintosh.nutihelkur.dao;

import android.support.annotation.NonNull;

import com.example.rmacintosh.nutihelkur.models.Statistics;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by rmacintosh on 04/05/2017.
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



}
