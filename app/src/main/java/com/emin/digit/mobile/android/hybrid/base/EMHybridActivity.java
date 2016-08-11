package com.emin.digit.mobile.android.hybrid.base;

import android.os.Bundle;
import android.util.Log;

/**
 * 以HTML5为前端的混合开发中,原生层总控Activity
 * 负责初始化配置;
 * 负责加载WebView,以及在WebView注入EMBridge对象,以供页面统一调用原生Plugin实现原生android功能;
 *
 *
 * Created by Samson on 16/8/5.
 */
public class EMHybridActivity extends EMBaseActivity {

    // 日志标志
    private static final String TAG = EMHybridActivity.class.getSimpleName();

    // 加载HTML页面的WebView
    private EMHybridWebView mWebView;

    // HTML加载首页Url,通过该Url,配置Hybrid部分的启动页面
    private String mIndexUrl;

    // WebView注入的对象暴露给javascript的名称
    private static final String INJECTED_BRIDGE_NAME = "EminBridge";

    /**
     * 复写onCreate方法,初始化mWebView,注入Java接口对象
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setup();
    }

    // 各种初始化
    private void setup(){
        setupWebView();
    }

    // WebView的初始化
    private void setupWebView(){
//        mWebView = new EMHybridWebView(this,EMHybridActivity.this, mIndexUrl);
        mIndexUrl = "file:///android_asset/apps/eminCloud/www/html/init.html";
        mWebView = new EMHybridWebView(this,EMHybridActivity.this,mIndexUrl);

        EMBridge injectedObject = new EMBridge();   // 注入的对象
        String nameUsedInJs = INJECTED_BRIDGE_NAME; // javascript通过该名字调用注入对象的方法
        mWebView.addJavascriptInterface(injectedObject, nameUsedInJs);

        if(mIndexUrl != null){
            mWebView.loadUrl(mIndexUrl);
        }
        setContentView(mWebView.getLayout());
    }

    // - - - - - - - - - - Activity的生命周期各个阶段涉及的WebView处理
    // Activity醒来,webView也开始工作
    @Override
    protected void onResume() {
        super.onResume();
        if(mWebView != null){
            mWebView.onResume();
        }
    }

    // Activity暂停,webView暂停
    @Override
    protected void onPause() {
        super.onPause();
        if(mWebView != null){
            mWebView.onPause();
        }
    }

    // Activity停止,webView停止加载
    @Override
    protected void onStop() {
        super.onStop();
        if(mWebView != null){
            mWebView.stopLoading();
        }
    }

    // Activity销毁,webView销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView != null){
            mWebView.destroy();
        }
    }

    public String getIndexUrl() {
        return mIndexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.mIndexUrl = indexUrl;
    }
}
