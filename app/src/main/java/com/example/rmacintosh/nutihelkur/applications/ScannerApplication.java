package com.example.rmacintosh.nutihelkur.applications;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.example.rmacintosh.nutihelkur.BuildConfig;
import com.example.rmacintosh.nutihelkur.ReflectorActivity;
import com.example.rmacintosh.nutihelkur.helpers.NotificationHelper;
import com.example.rmacintosh.nutihelkur.models.Sensor;

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
    Realm realm;

    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();





        //compile 'com.facebook.stetho:stetho:1.5.0'
        //vb hakkab siit error
        //beaconManager.getInstanceForApplication(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);


       // if (beaconManager.getInstanceForApplication(this) == null) {
         //   Log.d(TAG, "ERRROR ONN GETINSTANCEFORAPPLICATIONSis");
        //}
        // http://stackoverflow.com/questions/33594197/altbeacon-setbeaconlayout


        BeaconParser beaconParser = new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
        //beaconTransmitter = new BeaconTransmitter(context, beaconParser);
        //beaconTransmitter.setBeacon(createBeacon());

        beaconManager.getBeaconParsers().add(beaconParser);



        Region region = new Region("backgroundRegion", null, null, null);
        // simplywake up the app when a beacon is seen
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        Log.d(TAG, "onCreate ScannerApplication, setting up " +
                "background monitoring for beacons and power saving");
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

    @Override
    public void didEnterRegion(Region region) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG, " didEnterRegion, should send notification!");

        //        ((ScannerApplication) this.getApplicationContext()).setReflectorActivity(this);
        NotificationHelper.sendNotification(this, "Nutihelkur", "didEnterRegion worked");
        realmWorked();


        try {
            beaconManager.startRangingBeaconsInRegion(region);
        }
        catch (RemoteException e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Can't start ranging");
        }
    }

    public void realmWorked() {
        RealmResults<Sensor> result = realm.where(Sensor.class).findAll();


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
            reflectorActivity.logToDisplay("NO DANGEROUS PLACES atm");
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
        if (beacons.size() > 0) {
            Log.d(TAG, " didRangeBeaconsInRegion found beacons - but if it get id1?");
            for (Beacon b : beacons) {
                String a = b.getId1().toString();
                String aa = "5a4bcfce-174e-4bac-a814-092e77f6b7e5";

                Log.d(TAG, "ID2 on " + a + " peaks olema " + "5A4BCFCE-174E-4BAC-A814-092E77F6B7E5");
               // if(b.getId1().toString().equals("5a4bcfce-17ae-4Bac-a814-092e77f6b7e5")) {
                    Log.d(TAG, " getId edukas Beacon with my Instance ID found!" + a.equals(aa));
                Log.d(TAG, " getId edukas Beacon with my Instance ID found!" + a );
                Log.d(TAG, " getId edukas Beacon with my Instance ID found!" + aa );


                    //sendNotification("Beacon with my Instance ID found!");
                }
            }

        //}
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(this);
    }
}
