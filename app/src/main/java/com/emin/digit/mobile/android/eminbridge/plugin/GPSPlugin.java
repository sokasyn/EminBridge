package com.emin.digit.mobile.android.eminbridge.plugin;

import android.webkit.JavascriptInterface;

/**
 * Created by Samson on 16/8/2.
 */
public class GPSPlugin {

    @JavascriptInterface
    public void getLocation(String arg){
        debugLog("GPSPlugin getLocation called.." + arg);
    }

    private void debugLog(String info){
        System.out.println(info);
    }

}
