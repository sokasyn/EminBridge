package com.emin.digit.mobile.android.hybrid.plugin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

import com.emin.digit.mobile.android.hybrid.base.EMHybridActivity;
import com.emin.digit.mobile.android.hybrid.base.EMHybridWebView;
import com.emin.digit.mobile.android.hybrid.base.PluginParams;
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

    private void startSystemCameraOnly(Context context) {
        Log.d(TAG, "context:" + context);
        Log.d(TAG, "start camera thread id:" + Thread.currentThread().getId());

        Intent intentCamera = new Intent();
        String sysCameraAction = "android.media.action.STILL_IMAGE_CAMERA";
        intentCamera.setAction(sysCameraAction);
        context.startActivity(intentCamera);
    }

    public void loadBarcodeView(PluginParams params) {
        final EMHybridWebView webView = (EMHybridWebView) params.getWebView();
        activity = (EMHybridActivity) webView.getActivity();
        String position = params.getArguments()[0];
        Log.d(TAG, "- - - - - position:" + position);

        webView.post(new Runnable() {
            @Override
            public void run() {
                // 构造二维码的view

                // activity add 这个view



            }
        });
    }
}
