package com.emin.digit.mobile.android.eminbridge;

import android.webkit.JavascriptInterface;

/**
 * Created by Samson on 16/8/1.
 */
public class EminBridge {

    @JavascriptInterface
    public static void executePlugin(String pluginName){
        System.out.println("EminBridge called..." + pluginName);
    }

    @JavascriptInterface
    public static String helloBridge(){
        System.out.println("EminBridge helloBridge() called...");
        return "Hello";

    }
}
