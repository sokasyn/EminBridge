package com.emin.digit.mobile.android.eminbridge;

import android.content.res.AssetManager;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.emin.digit.android.eminbridge.eminbridge.R;

public class MainActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    // - - - - - - - - - - - 初始化 Start - - - - - - - - - - -
    private void setup(){
        debugLog("setup...");
        // 配置PageManager
        setupPageManager();

        // 配置Bridge
//        setupBridgeManager();
    }


    // 配置页面管理器
    private void setupPageManager(){
        loadInitPage();
    }

    // 配置桥接管理器
    private void setupBridgeManager(){

    }
    // - - - - - - - - - - - 初始化 End - - - - - - - - - - -

    // 通过PageManager加载web层的初始化页面
    private void loadInitPage(){
        debugLog("locad init local page called 5..");
        // 加载本地的Html资源
        loadLocalPageWithAbsolutePath();

    }

    // 加载本地HTML页面资源
    private void loadLocalPageWithAbsolutePath(){
        debugLog("loadLocalPageWithAbsolutePath 2");
        String filePath = "file:///android_asset/apps/eminCloud/www/html/index.html";
        webview = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(this, "dbPlugin"); //对应js中的plugin.method
        webview.addJavascriptInterface(this, "locationPlugin"); //对应js中的plugin.method
        webview.loadUrl(filePath);
    }

    @JavascriptInterface
    public void insert(String msg){ //对应js中plugin.helloBridge("message")
        debugLog("helloBridge called.....arg:" + msg);
    }

    @JavascriptInterface
    public void getLocation(String msg){ //对应js中plugin.helloBridge("message")
        debugLog("getLocation...arg:" + msg);
    }


    private void debugLog(String info){
        System.out.println(info);
    }


}
