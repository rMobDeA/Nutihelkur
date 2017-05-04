package com.example.rmacintosh.nutihelkur.applications;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.example.rmacintosh.nutihelkur.BuildConfig;
import com.example.rmacintosh.nutihelkur.ReflectorActivity;
import com.example.rmacintosh.nutihelkur.dao.SensorDao;
import com.example.rmacintosh.nutihelkur.helpers.NotificationHelper;
import com.example.rmacintosh.nutihelkur.models.Sensor;
import com.example.rmacintosh.nutihelkur.utils.RealmManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class ScannerApplication extends Application implements BootstrapNotifier,
        BeaconConsumer, RangeNotifier {

    private static final String TAG = "ScannerApplication";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private ReflectorActivity reflectorActivity = null;
    BeaconManager beaconManager;

    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        RealmManager.open();

        realmWorked();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // http://stackoverflow.com/questions/33594197/altbeacon-setbeaconlayout
        BeaconParser beaconParser = new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
        beaconManager.getBeaconParsers().add(beaconParser);

        /**
         * Constructs a new Region object to be used for Ranging or Monitoring
         * @param uniqueId - A unique identifier used to later cancel Ranging and
         *                 Monitoring, or change the region being Ranged/Monitored
         * @param id1 - most significant identifier (can be null)
         * @param id2 - second most significant identifier (can be null)
         * @param id3 - third most significant identifier (can be null)
         */

        Region region = new Region("backgroundRegion", null, null, null);
        // simply wake up the app when a beacon is seen
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);


        Log.d(TAG, " isBackgroundModeUninitialized "
                + beaconManager.isBackgroundModeUninitialized());

        /*
            DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD	300000L
            DEFAULT_BACKGROUND_SCAN_PERIOD	10000L
            DEFAULT_EXIT_PERIOD	10000L
            DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD	0L
            DEFAULT_FOREGROUND_SCAN_PERIOD	1100L
         */

        beaconManager.setBackgroundBetweenScanPeriod(1000l);
        beaconManager.setBackgroundScanPeriod(1000l);
        beaconManager.setForegroundBetweenScanPeriod(1000l);
        beaconManager.bind(this);


    }

    /**
     * Called when at least one beacon in a <code>Region</code> is visible.
     * @param region a Region that defines the criteria of beacons to look for
     * Tells the BeaconService to start looking for beacons that match the
     * passed Region object, and providing updates on the estimated mDistance
     * every seconds while beacons in theRegion are visible.
     */
    @Override
    public void didEnterRegion(Region region) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        }
        catch (RemoteException e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Can't start ranging");
        }
    }

  public void realmWorked() {
      Log.d(TAG, "realmworked kaivatutud");
      RealmResults<Sensor> result = RealmManager.createSensorDao().loadAll();;

        for(Sensor sensor : result) {
            Log.d(TAG, "realmworked !!!! SensorId " + sensor.getSensorId()  + "Location" + sensor.getLocation());
        }
    }

    /**
     * Called when at least one beacon in a <code>Region</code> is visible.
     * @param region a Region that defines the criteria of beacons to look for
     */
    @Override
    public void didExitRegion(Region region) {
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (reflectorActivity != null) {
            reflectorActivity.logToDisplay("NO DANGEROUS PLACES");
        }
    }

    // OUTSIDE val 0, MonitorNotifier.INSIDE val 1
    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        if (i == 0) {
            Log.d(TAG, " CANT SEE ANY SENSOR");
        } else {
            Log.d(TAG, " WELL LUCKY YOU, AT LEAST ONE SENSOR!");
        }

    }



    public void setReflectorActivity(ReflectorActivity activity) {
        this.reflectorActivity = activity;
    }

    
    /**
     * Called once per second to give an estimate of the mDistance to visible beacons
     * @param beacons a collection of <code>Beacon<code> objects that have been seen in the past second
     * @param region the <code>Region</code> object that defines the criteria for the ranged beacons
     */
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

        //miks pean siin uuesti avama???
        RealmManager.open();
        if (beacons.size() > 0) {
            Log.d(TAG, " didRangeBeaconsInRegion found beacons - but if it get id1?");
            for (Beacon b : beacons) {
                boolean x = RealmManager.createSensorDao().isValidSensor(b.getId1().toString());
                if (x == true) {
                    String f = RealmManager.createSensorDao().loadSensorLocation(b.getId1().toString());
                   NotificationHelper.sendNotification(this, "Nutihelkur, olete ohtlikus kohas: ", f);
                    Log.d(TAG, " load sesnor location ja is valid sensor töötavad ehk...." + x );

                } else {
                    Log.d(TAG, "MINGI ERROR SESOSES LOAD SENSOR LOCATION JA VALID SESNORIGA" + x + b.getId1().toString());
                }
            }
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(this);
    }
}
