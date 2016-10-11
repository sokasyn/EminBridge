package com.emin.digit.mobile.android.hybrid.base;

import android.content.Context;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebView;

/**
 * Created by Samson on 2016/10/11.
 */
public class ReddotManager {
    private static final String TAG = ReddotManager.class.getSimpleName();

    private EMHybridActivity mContext;
    private static ReddotManager instance = new ReddotManager();

    public static ReddotManager getInstance(){
        return instance;
    }

    private ReddotManager(){
    }

    // 红点服务的注册
    public void register(final WebView webView){
        Log.d(TAG,"11111 register");
        EMHybridWebView view = (EMHybridWebView) webView;
        mContext = (EMHybridActivity) view.getActivity();
        view.setReddotRegister(true);
    }

    // 红点服务的注册
    public void register(final WebView webView,final String callback){
        Log.d(TAG,"22222 register");
        EMHybridWebView view = (EMHybridWebView) webView;
        mContext = (EMHybridActivity) view.getActivity();
        view.setReddotRegister(true);
        // 如果有需要,可以在js传入js的回调方法
        webView.post(new Runnable() {
            String callBackName = callback;
            String result = "registered done";
            @Override
            public void run() {
                Log.d(TAG,"callback run");
                webView.loadUrl("javascript:" + callBackName + "('" + result + "')");
            }
        });
    }

    // 红点计数处理
    public void reddotCounting(){
        Log.d(TAG,"3333 pushMessageComing then calculate reddot count");

        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        broadcastReddotDataReady();
    }

    // 广播红点已经更新的通知(数据库的数据已经准备好)
    private void broadcastReddotDataReady(){
        Log.d(TAG,"After reddotCounting done,broadcast to tell activity");
        mContext.updateReddot();
    }

}
