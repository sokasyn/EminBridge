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

        for(int i = 0; i < params.getArguments().length; i++){

        }

        // 调用Camera
        // 测试通过回调的方式告知返回值
        final WebView webView = params.webView;
        String type = params.getArguments()[0];
        final String callBack = params.getArguments()[1];
        debugLog("argument type:" + type);
        debugLog("argument callback:" + callBack);

        //All WebView methods must be called on the same thread Expected Looper Looper (mai
        webView.post(new Runnable() {
            @Override
            public void run() {
//                webView.loadUrl("javascript:alert('callback in plugin')");
//                webView.loadUrl("javascript:function(){alert('callback in plugin');}");
//                webView.loadUrl("javascript:foo()");
//                webView.loadUrl("javascript:functionInJs()");
                webView.loadUrl("javascript:" + callBack +"()");
//                webView.loadUrl("javascript:" + "alert()");
            }
        });
//
//        if(callBack.indexOf("function") != -1){
//            debugLog("argument is javascript function");
//            String url = "javascript:"+ callBack +"()";
//            webView.loadUrl(url);
//        }else{
//            debugLog("argument is not javascript function");
//        }

//        return "barcode";
    }

    public void testCallbackFun(PluginParams params){
        // 调用Camera
        // 测试通过回调的方式告知返回值
        final WebView webView = params.webView;
        String type = params.getArguments()[0];
        final String callBackName = params.getArguments()[1];
        debugLog("argument type:" + type);
        debugLog("argument callbackName:" + callBackName);

        //All WebView methods must be called on the same thread Expected Looper Looper (mai
        webView.post(new Runnable() {
            @Override
            public void run() {
//                webView.loadUrl("javascript:alert('callback in plugin')");
//                webView.loadUrl("javascript:function(){alert('callback in plugin');}");
//                webView.loadUrl("javascript:foo()");
//                webView.loadUrl("javascript:functionInJs()");
//                webView.loadUrl("javascript:functionInJs()");
                webView.loadUrl("javascript:" + callBackName +"()");
//                webView.loadUrl("javascript:" + "alert()");
            }
        });
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
