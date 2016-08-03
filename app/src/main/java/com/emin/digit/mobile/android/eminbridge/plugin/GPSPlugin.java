package com.emin.digit.mobile.android.eminbridge.plugin;

import android.webkit.JavascriptInterface;

/**
 * Created by Samson on 16/8/2.
 */
public class GPSPlugin {

    public String getLocation(String arg){
        debugLog("GPSPlugin getLocation called.." + arg);
        String result = "111.01,222.23";
        return result;
    }

    public void startGPS(String arg){
        debugLog("GPSPlugin startGPS called..");
    }



    private void debugLog(String info){
        System.out.println(info);
    }

}
