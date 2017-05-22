package com.example.rmacintosh.nutihelkur.utils;

import android.support.annotation.NonNull;

import com.example.rmacintosh.nutihelkur.models.Sensor;

import java.util.ArrayList;
import java.util.List;


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

public class SensorsGenerator {

    @NonNull
    public static List<Sensor> generatePreSensorList() {

        List<Sensor> sensorsList = new ArrayList<>();

        Sensor sensor1 = new Sensor();
        Sensor sensor2 = new Sensor();
        Sensor sensor3 = new Sensor();
        Sensor sensor4 = new Sensor();
        Sensor sensor5 = new Sensor();

        /*sensor1.setSensorId("00000000-0000-0000-0000-000000000000");
        sensor2.setSensorId("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6");
        sensor3.setSensorId("52414449-5553-4e45-5457-4f524b53434f");
        sensor4.setSensorId("5a4bcfce-174e-4bac-a814-092e77f6b7e5");
        sensor5.setSensorId("5affffff-ffff-ffff-ffff-ffffffffffff");*/

        sensor1.setSensorId("52414449-5553-4e45-5457-4f524b53434f");
        sensor2.setSensorId("52414449-5553-4e45-5457-4f524b53434f");
        sensor3.setSensorId("52414449-5553-4e45-5457-4f524b53434f");
        sensor4.setSensorId("52414449-5553-4e45-5457-4f524b53434f");
        sensor5.setSensorId("52414449-5553-4e45-5457-4f524b53434f");

        sensor1.setLocationId(1);
        sensor2.setLocationId(2);
        sensor3.setLocationId(3);
        sensor4.setLocationId(4);
        sensor5.setLocationId(5);

        sensor1.setLocation("Annelinna Keskus");
        sensor2.setLocation("Novira Plaza parkla");
        sensor3.setLocation("Tulika bussipeatus");
        sensor4.setLocation("Nutihelkuri koduhoov");
        sensor5.setLocation("Pirogovi plats");

        sensorsList.add(sensor1);
        sensorsList.add(sensor2);
        sensorsList.add(sensor3);
        sensorsList.add(sensor4);
        sensorsList.add(sensor5);

        return sensorsList;
    }
}
