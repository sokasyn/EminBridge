package com.emin.digit.mobile.android.hybrid.plugin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;

import com.emin.digit.mobile.android.hybrid.base.EMHybridActivity;
import com.emin.digit.mobile.android.hybrid.base.EMHybridWebView;
import com.emin.digit.mobile.android.hybrid.base.PluginParams;
import com.emin.digit.test.zxing.BarcodeController;
import com.emin.digit.test.zxing.android.CaptureActivity;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.Result;


/**
 * Created by Samson on 16/8/15.
 */
public final class PluginBarcode {//implements SurfaceHolder.Callback {

    private static final String TAG = PluginBarcode.class.getSimpleName();

    private EMHybridActivity activity;

    final static int REQUEST_CODE_SCAN = 0x0000;


    // 以单独 View的方式打开二维码扫描
    public void loadBarcodeView(PluginParams params){
        Log.d(TAG,"loadBarcodeView thread id:" + Thread.currentThread().getId());
        final EMHybridWebView webView = (EMHybridWebView)params.webView;
        final EMHybridActivity activity = (EMHybridActivity)webView.getActivity();
        webView.post(new Runnable() {
            @Override
            public void run() {
                new BarcodeController().loadBarcodeView(activity);
            }
        });
    }



    // 单独Activity的方式打开二维码扫描
    public void startBarcode(PluginParams params) {
        Log.d(TAG, "startBarcode");

        // 调用Camera
        // 测试通过回调的方式告知返回值
        final WebView webView = params.webView;
        String type = params.getArguments()[0];
        Log.d(TAG, "1111 argument type:" + type);

        webView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "webView:" + webView);
//                startSystemCameraOnly(webView.getContext());
                Context context = webView.getContext();
                Intent intent = new Intent(context, CaptureActivity.class);
                context.startActivity(intent);

                //Activity activity = (EMHybridActivity) webView.getContext();
                //ViewfinderView view = (ViewfinderView) activity.findViewById(R.id.viewfinder_view);
            }
        });

    }

    public void testCallbackFun(PluginParams params) {
        // 调用Camera
        // 测试通过回调的方式告知返回值
        final WebView webView = params.webView;
        String type = params.getArguments()[0];
        final String callBackName = params.getArguments()[1];
        Log.d(TAG, "argument type:" + type);
        Log.d(TAG, "argument callbackName:" + callBackName);


        //All WebView methods must be called on the same thread Expected Looper Looper (mai
        webView.post(new Runnable() {
            @Override
            public void run() {
//                webView.loadUrl("javascript:alert('callback in plugin')");
//                webView.loadUrl("javascript:function(){alert('callback in plugin');}");
//                webView.loadUrl("javascript:foo()");
//                webView.loadUrl("javascript:functionInJs()");
//                webView.loadUrl("javascript:functionInJs()");
                String result = "Good luck!";
                webView.loadUrl("javascript:" + callBackName + "('" + result + "')");
//                webView.loadUrl("javascript:" + "alert()");
            }
        });

        if (callBackName.indexOf("function") != -1) {
            Log.d(TAG, "argument is javascript function");
            String url = "javascript:" + callBackName + "()";
            webView.loadUrl(url);
        } else {
            Log.d(TAG, "argument is not javascript function");
        }
    }


}
