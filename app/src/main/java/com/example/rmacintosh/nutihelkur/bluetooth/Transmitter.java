package com.example.rmacintosh.nutihelkur.bluetooth;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

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
                .setId3(Integer.toString(2))
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
