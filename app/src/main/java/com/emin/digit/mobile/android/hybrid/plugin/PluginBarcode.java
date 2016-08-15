package com.emin.digit.mobile.android.hybrid.plugin;

import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

import com.emin.digit.mobile.android.hybrid.base.PluginParams;

/**
 * Created by Samson on 16/8/15.
 */
public class PluginBarcode {

    private static final String TAG = PluginBarcode.class.getSimpleName();

    public void startBarcode(PluginParams params){
        Log.d(TAG,"startBarcode");

        // 调用Camera
        // 测试通过回调的方式告知返回值
        WebView webView = params.webView;
        String type = params.getArguments()[0];
        String callBack = params.getArguments()[1];
        debugLog("argument type:" + type);
        debugLog("argument callback:" + callBack);

        if(callBack.indexOf("function") != -1){
            debugLog("argument is javascript function");
            String url = "javascript:"+ callBack +"()";
            webView.loadUrl(url);
        }else{
            debugLog("argument is not javascript function");
        }

//        return "barcode";
    }


    /*
    private void startSystemCameraOnly(){
        Intent intentCamera = new Intent();
        String sysCameraAction = "android.media.action.STILL_IMAGE_CAMERA";
        intentCamera.setAction(sysCameraAction);
        startActivity(intentCamera);
    }*/

    private void debugLog(String info){
        System.out.println(info);

    }
}
