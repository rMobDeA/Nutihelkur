package com.example.rmacintosh.nutihelkur.applications;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.example.rmacintosh.nutihelkur.BuildConfig;
import com.example.rmacintosh.nutihelkur.ReflectorActivity;
import com.example.rmacintosh.nutihelkur.bluetooth.Transmitter;
import com.example.rmacintosh.nutihelkur.helpers.BluetoothHelper;
import com.example.rmacintosh.nutihelkur.helpers.NotificationHelper;
import com.example.rmacintosh.nutihelkur.helpers.TimeHelper;
import com.example.rmacintosh.nutihelkur.utils.RealmManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.BleNotAvailableException;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *                      created by Rauno Pügi
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
 *
 *        ScannerApplication was created with help from:
 *        https://altbeacon.github.io/android-beacon-library/samples.html
 */


public class ScannerApplication extends Application implements BootstrapNotifier,
        BeaconConsumer, RangeNotifier {

    private static final String TAG = "ScannerApplication";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private ReflectorActivity reflectorActivity = null;
    private Transmitter transmitter2;
    BeaconManager beaconManager;

    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        RealmManager.open();


        beaconManager = BeaconManager.getInstanceForApplication(this);
        BeaconParser beaconParser = new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
        beaconManager.getBeaconParsers().add(beaconParser);

        startBeaconTransmitService();
        startTransmitting();


        /**
         * Constructs a new Region object to be used for Ranging or Monitoring
         * @param uniqueId - A unique identifier used to later cancel Ranging and
         *                 Monitoring, or change the region being Ranged/Monitored
         * @param id1 - most significant identifier (can be null)
         * @param id2 - second most significant identifier (can be null)
         * @param id3 - third most significant identifier (can be null)
         */

        // PARSES our sensor UUID to Identifier
        Identifier x  = Identifier.parse("52414449-5553-4e45-5457-4f524b53434f");

        Region region = new Region("backgroundRegion", x , null, null);
        // simply wake up the app when a beacon is seen
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);


        Log.d(TAG, " isBackgroundModeUninitialized "
                + beaconManager.isBackgroundModeUninitialized());


        // DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD	300000L
        // DEFAULT_BACKGROUND_SCAN_PERIOD	10000L
        // DEFAULT_EXIT_PERIOD	10000L
        // DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD	0L
        // DEFAULT_FOREGROUND_SCAN_PERIOD	1100L


        beaconManager.setBackgroundBetweenScanPeriod(1000l);
        beaconManager.setBackgroundScanPeriod(1000l);
        beaconManager.setForegroundBetweenScanPeriod(1000l);
        beaconManager.bind(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startBeaconTransmitService() {
        transmitter2 = new Transmitter(this);
    }



    private void startTransmitting() {
        try {
            if (!beaconManager.checkAvailability()) {
                Toast.makeText(this, "BLE NOT ON!",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (!(BeaconTransmitter.checkTransmissionSupported(this) == BeaconTransmitter.SUPPORTED)) {
                    Toast.makeText(this, "BLE advert no support!",
                            Toast.LENGTH_SHORT).show();
                    notifyTransmittingNotSupported();
                } else if (transmitter2 != null) {

                    transmitter2.startTransmitting();
                }
            }
        } catch (BleNotAvailableException bleNotAvailableException) {
            notifyTransmittingNotSupported();
        }
    }

    private void notifyTransmittingNotSupported() {
        String bleProblem2 = BluetoothHelper.bleCompatibility(
                BeaconTransmitter.checkTransmissionSupported(this));
        new AlertDialog.Builder(this)
                .setTitle("HOW YOU MADE SO FAR?")
                .setMessage("BLE ERROR: " + bleProblem2)
                .setPositiveButton(android.R.string.ok, null)
                .show();
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
        }
    }


    /**
     *
     * @param i MonitorNotifier -> outside - 1, inside 1
     * @param region
     */
    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        if (i == 0) {
            Log.d(TAG, "NO SENSORS IN REGION");
        } else {
            Log.d(TAG, "AT LEAST ONE SENSOR IN REGION");
        }

    }



    public void setReflectorActivity(ReflectorActivity activity) {
        this.reflectorActivity = activity;
    }

    /**
     * Called once per second to give an estimate of the mDistance to visible beacons
     * @param sensors a collection of <code>Beacon<code> objects that have been seen in the past second
     * @param region the <code>Region</code> object that defines the criteria for the ranged beacons
     *
     *               Sends notification and writes sensor data to Realm if
     *               there hasn't been same sensor entry for 120 seconds.
     */
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> sensors, Region region) {
        RealmManager.open();
        if (sensors.size() > 0) {
            for (Beacon sensor : sensors) {
                int sensorLocationId = sensor.getId2().toInt();
                long locatingTime = TimeHelper.getTime();
                    if (RealmManager.createStatisticsDao().notInDatabase(
                        sensorLocationId, locatingTime)) {
                        //getGpsCoordinates();
                            String beaconLocation = RealmManager.createSensorDao().
                                getSensorLocation(sensorLocationId);
                            int usageCount = sensor.getId3().toInt();
                            RealmManager.createStatisticsDao().writeToDatabase(
                                    beaconLocation,
                                    sensorLocationId,
                                    locatingTime,
                                    usageCount);
                        whichNotification(beaconLocation);
                } else {
                    Log.d(TAG, "SENSOR ALREADY IN DATABASE!");
                }
            }
        }
    }

    /**
     * Decides wihich notification must be send - with sound on without.
     * @param beaconLocation to send notification with location
     */
    public void whichNotification(String beaconLocation) {
        boolean displayOff = true;

        DisplayManager displayM = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
        for (Display display : displayM.getDisplays()) {
            if (display.getState() == Display.STATE_OFF) {
                displayOff = false;

            }
        }
        Log.d(TAG, "DISPLAY " + displayOff );


        AudioManager audioM = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        boolean headPhonesOn = audioM.isWiredHeadsetOn();
        Log.d(TAG, "HEADPHONES " + headPhonesOn);


        if (displayOff || headPhonesOn) {
            NotificationHelper.sendSoundNotification(this, "Nutihelkur", beaconLocation);
        } else {
            NotificationHelper.sendNotification(this, "Nutihelkur", beaconLocation);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(this);
    }
}
