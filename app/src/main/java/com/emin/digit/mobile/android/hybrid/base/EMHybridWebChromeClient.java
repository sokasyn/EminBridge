package com.emin.digit.mobile.android.hybrid.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

//import com.emin.digit.mobile.android.hybrid.EminBridge;
import com.emin.digit.mobile.android.hybrid.EminBridge.R;


/**
 * Created by Samson on 16/8/11.
 */
public class EMHybridWebChromeClient extends WebChromeClient{

    private static final String TAG = EMHybridWebChromeClient.class.getSimpleName();

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
         final EMHybridWebView rootView = (EMHybridWebView)view;
//        Log.d(TAG,"hasAnimated :"+hasAnimated + " webPageAnimated:"+webPageAnimated);
        Log.d(TAG,"target webView:" +view + " view url:" + view.getUrl());
        Log.d(TAG,"newProgress :" + newProgress);

        // TODO: 16/8/17 android 4.x 之后调用两次的问题
        if(newProgress == 100){
            Log.d(TAG,"finish loading");
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
    }

    /**
     * 可以监听到web的控制台信息
     * 比如Js的错误信息
     *
     * @param consoleMessage
     * @return
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }
}
