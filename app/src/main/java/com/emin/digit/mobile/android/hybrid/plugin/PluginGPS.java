package com.emin.digit.mobile.android.hybrid.plugin;

import com.emin.digit.mobile.android.hybrid.base.PluginParams;

/**
 * Created by Samson on 16/8/2.
 */
public class PluginGPS {

    public String getLocation(PluginParams params){
        debugLog("PluginGPS getLocation called.." + params);
//        String result = "111.01,222.23";
        String result = "{经度:111.01,纬度:222.23}"; // 返回中文测试
        return result;
    }

    public void startGPS(PluginParams params){
        debugLog("PluginGPS startGPS called..");
    }

    private void debugLog(String info){
        System.out.println(info);
    }
}
