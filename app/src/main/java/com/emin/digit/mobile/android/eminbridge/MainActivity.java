package com.emin.digit.mobile.android.eminbridge;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emin.digit.android.eminbridge.eminbridge.R;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 当前的线程id
        long threadId = Thread.currentThread().getId();
        debugLog("[Thread id] onCreate:" + threadId);

        // 取消标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        // 加载本地的Html资源
        loadLocalPage();
    }

    private void loadLocalPage(){
        System.out.println("= = = = = = = = 5555");

        webView = (WebView)findViewById(R.id.webView);
        String url = "file:///android_asset/apps/eminCloud/www/html/init.html";
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 添加Javascript接口,相当于一个JAVA类的别名,在Javascript中采用该名称调用EminBridge对象中的方法
        webView.addJavascriptInterface(new EminBridge(this),"EminBridge");

        // 在涉及到弹窗,如JS中的alert("message"),需要用到WebChromeClient
//        webView.setWebChromeClient(new EminChromeClient());
        webView.setWebChromeClient(new CustomChromeClient());

        // 在涉及到页面的跳转,默认是通过系统的浏览器加载,如果向在webview中实现,则需要配置WebViewClient
        webView.setWebViewClient(new CustomWebViewClient());

        // webView 执行js代码
//        webView.loadUrl("javascript:console.log('execute javascript directly in native webview')");
    }

    class CustomWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            debugLog("shouldOverrideUrlLoading : url:" + url);
             return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            debugLog("onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            debugLog("onPageFinished");
            super.onPageFinished(view, url);
//            webView.loadUrl("javascript:functionInJs()");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            debugLog("onLoadResource");
            super.onLoadResource(view, url);
        }
    }

    class CustomChromeClient extends WebChromeClient{


    }


    /*
    public static void(WebView web view, JSONObject jsonObj, Callback callback){

    }*/


    private void debugLog(String info){
        System.out.println(info);
    }

    private void sleepForSeconds(int seconds){
        debugLog("will sleep for " + seconds + " seconds..");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
