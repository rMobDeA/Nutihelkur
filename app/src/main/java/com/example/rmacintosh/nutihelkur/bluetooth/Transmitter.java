package com.example.rmacintosh.nutihelkur.bluetooth;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

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
 *
 *        Transmitter class was created with help from:
 *        https://github.com/BoydHogerheijde/Beacon-Scanner/blob/master/app/src/main/
 *        java/com/hogervries/beaconscanner/Transmitter.java
 */

public class Transmitter {

    private Context context;
    private BeaconTransmitter beaconTransmitter;

    public Transmitter(Context context) {
        this.context = context;
        setUpTransmitter();
        setAdvertisingMode();
    }

    public void startTransmitting() {

        beaconTransmitter.startAdvertising();
    }

    public void stopTransmitting() {

        beaconTransmitter.stopAdvertising();
    }

    private void setUpTransmitter() {
        BeaconParser beaconParser = new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
        beaconTransmitter = new BeaconTransmitter(context, beaconParser);
        beaconTransmitter.setBeacon(createBeacon());
    }

    private Beacon createBeacon() {
        return new Beacon.Builder()
                .setId1("6fb0e0e9-2ae6-49d3-bba3-3cb7698c77e2")
                .setId2(Integer.toString(1))
                .setId3(Integer.toString(1))
                .setManufacturer(0x0000)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[] {0l}))
                .build();

    }



    /**
     * AdvertiseSettings.ADVERTISE_MODE_BALANCED 3 Hz
     * AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY 10 Hz
     * AdvertiseSettings.ADVERTISE_MODE_LOW_POWER 1 Hz
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setAdvertisingMode() {

        beaconTransmitter.setAdvertiseMode(10);
    }

}
