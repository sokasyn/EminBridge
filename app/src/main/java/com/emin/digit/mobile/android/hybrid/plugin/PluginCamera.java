package com.emin.digit.mobile.android.hybrid.plugin;

import android.content.Intent;
import android.util.Log;

import com.emin.digit.mobile.android.hybrid.base.EMHybridActivity;
import com.emin.digit.mobile.android.hybrid.base.EMHybridWebView;
import com.emin.digit.mobile.android.hybrid.base.PluginParams;
import com.emin.digit.test.TestCameraActivity;

/**
 * Created by Samson on 16/8/16.
 */
public class PluginCamera {

    private static final String TAG = PluginCamera.class.getSimpleName();

    public void startCamera(PluginParams params){
        Log.d(TAG,"= = = startCamera");
        final EMHybridWebView webView = (EMHybridWebView) params.getWebView();
        final EMHybridActivity activity = (EMHybridActivity)webView.getActivity();

        webView.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("com.emin.digit.test.TestCameraActivity");
                activity.startActivity(intent);
            }
        });
    }

    public void startSurface(PluginParams params){
        Log.d(TAG," * * * startSurface");
        final EMHybridWebView webView = (EMHybridWebView) params.getWebView();
        final EMHybridActivity activity = (EMHybridActivity)webView.getActivity();

        webView.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("com.emin.digit.test.TestSurfaceActivity");
                activity.startActivity(intent);
            }
        });
    }
}
