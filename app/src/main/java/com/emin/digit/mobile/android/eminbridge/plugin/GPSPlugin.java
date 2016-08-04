package com.emin.digit.mobile.android.eminbridge.plugin;

/**
 * Created by Samson on 16/8/2.
 */
public class GPSPlugin {

    public String getLocation(String arg){
        debugLog("GPSPlugin getLocation called.." + arg);
//        String result = "111.01,222.23";
        String result = "{经度:111.01,纬度:222.23}"; // 返回中文测试
        return result;
    }

    public void startGPS(String arg){
        debugLog("GPSPlugin startGPS called..");
    }

    private void debugLog(String info){
        System.out.println(info);
    }

}
