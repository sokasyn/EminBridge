package com.emin.digit.mobile.android.hybrid.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 以HTML5为前端的混合开发中,原生层总控Activity
 * 负责初始化配置;
 * 负责加载WebView,以及在WebView注入EMBridge对象,以供页面统一调用原生Plugin实现原生android功能;
 *
 *
 * Created by Samson on 16/8/5.
 */
public class EMHybridActivity extends EMBaseActivity {


    private static final String TAG = EMHybridActivity.class.getSimpleName(); // 日志标志
    private EMHybridWebView mWebView; // 加载HTML页面的WebView
    private String mIndexUrl;         // HTML加载首页Url,通过该Url,配置Hybrid部分的启动页面

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
        Log.d(TAG,"= = = = = = = = = = 1");

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消默认的标题栏以及全屏
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setupWebView();
    }

    // WebView的初始化
    private void setupWebView(){
//        mIndexUrl = "file:///android_asset/apps/eminCloud/www/html/init.html";
        mIndexUrl = "file:///android_asset/apps/AppPage/index.html";

        mWebView = new EMHybridWebView(this, EMHybridActivity.this, mIndexUrl);
        Log.d(TAG,"= = = = = = EMHybridActivity:" + this);
        Log.d(TAG,"= = = = = = EMHybridActivity.this:" + EMHybridActivity.this);
        Log.d(TAG,"= = = = = = webView in Activity initialize:" + mWebView);
        Log.d(TAG,"= = = = = = WebView getContext:" + mWebView.getContext());

        configJavascriptInterface();

        if(mIndexUrl != null){
            mWebView.loadUrl(mIndexUrl);
        }
        setContentView(mWebView);
    }

    // 注入EminBridge对象
    private void configJavascriptInterface(){
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            Log.i(TAG, "Disabled addJavascriptInterface() bridge since Android version is old.");
            return;
        }
        EMBridge injectedObject = new EMBridge(this, mWebView);   // 注入的对象
        String nameUsedInJs = INJECTED_BRIDGE_NAME; // javascript通过该名字调用注入对象的方法
        mWebView.addJavascriptInterface(injectedObject, nameUsedInJs);
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

    /**
     * 监听设备返回按键事件
     * 如果存在历史记录,即可以返回(canGoBack)则返回上一个页面;
     * 如果不处理,则WebView直接就退出了
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // 如果有历史记录,在返回上一个页面
            if(mWebView.canGoBack()){
                mWebView.goBack();
            }else {
                // 没有历史记录,则提示用户是否期望退出应用
                exitAppWhenDoublePressed();
            }
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

    private long firstTimePressed = 0;
    private void exitAppWhenDoublePressed(){
        // 2秒之内按两次,则退出应用
        long nextTimePressed = System.currentTimeMillis();
        if( (nextTimePressed - firstTimePressed) > 2 *1000){
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            firstTimePressed = nextTimePressed;
        }else {
            finish();
            System.exit(0);
        }
    }

    public String getIndexUrl() {
        return mIndexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.mIndexUrl = indexUrl;
    }
}
