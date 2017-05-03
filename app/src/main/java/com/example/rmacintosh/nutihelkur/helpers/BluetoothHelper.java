package com.example.rmacintosh.nutihelkur.helpers;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by rmacintosh on 01/04/2017.
 */

public class BluetoothHelper {

    public static void isBluetoothOn() {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
    }


        /*
        SUPPORTED 0
        NOT_SUPPORTED_MIN_SDK 1
        NOT_SUPPORTED_BLE 2
        NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS 3
        NOT_SUPPORTED_CANNOT_GET_ADVERTISER 4
        NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS 5
         */

    public static String bleCompatibility(int code){
        String compatibility;
        if (code == 0) {
            compatibility = "Fully Supported";
        } else if (code == 1) {
            compatibility = "Not Supported min SDK 1";
        } else if (code == 2) {
            compatibility = "Not Supported BLE 2";
        } else if (code == 3) {
            compatibility = "Not Supported multiple advertisements";
        } else if (code == 4) {
            compatibility = "Not Supported cannot get advertiser";
        } else if (code == 5) {
            compatibility = "Not Supported cannot get advertiser multiple advertisements";
        } else {
            compatibility = "Unknown error";
        }

        return compatibility;
    }
}


