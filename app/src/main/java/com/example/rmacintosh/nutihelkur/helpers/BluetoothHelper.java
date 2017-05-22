package com.example.rmacintosh.nutihelkur.helpers;

import android.bluetooth.BluetoothAdapter;

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

public class BluetoothHelper {

    /**
     * Check if bluetooth is turned on.
     * When off, bluetooth will be turned on.
     */
    public static void isBluetoothOn() {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
    }


    /**
     * @param code  SUPPORTED 0
                    NOT_SUPPORTED_MIN_SDK 1
                    NOT_SUPPORTED_BLE 2
                    NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS 3
                    NOT_SUPPORTED_CANNOT_GET_ADVERTISER 4
                    NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS 5
     * @return code translated to text
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


