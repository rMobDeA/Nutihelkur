package com.example.rmacintosh.nutihelkur.helpers;

import java.text.DateFormat;



public class TimeHelper {

    /**
     * @return time since 01.01.1970 in seconds
     */
    public static long getTime() {
        long time = System.currentTimeMillis()/1000;
        return time;
    }

    /**
     * @param time in seconds since 01.01.1970
     * @return date in String like "Dec 31, 1969 4:00:00 PM"
     */
    public static String getDate(long time){
        String date = DateFormat.getDateTimeInstance().format(time*1000);
        return date;
    }
}
