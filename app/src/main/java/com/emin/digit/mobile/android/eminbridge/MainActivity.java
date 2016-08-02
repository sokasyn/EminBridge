package com.emin.digit.mobile.android.eminbridge;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emin.digit.android.eminbridge.eminbridge.R;
import com.emin.digit.mobile.android.eminbridge.plugin.DatabasePlugin;
import com.emin.digit.mobile.android.eminbridge.plugin.GPSPlugin;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private LinkedList<WebView> webList;
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
        webList = new LinkedList<WebView>();
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
//        loadLocalPage();
        testPushWebView();
    }

    private void testPushWebView(){
        webview = (WebView)findViewById(R.id.webView);
        webList.add(webview);
        webList.add(webview);

        System.out.println("webList fist element : " + webList.getFirst());
        System.out.println("webList last element : " + webList.getLast());
    }

    // 加载本地HTML页面资源
    private void loadLocalPageWithAbsolutePath(){
        debugLog("loadLocalPageWithAbsolutePath 2");
        webview = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 1.向该webview注入Java原生对象-DatabasePlugin
        DatabasePlugin pluginDb = new DatabasePlugin();
        webview.addJavascriptInterface(pluginDb, "dbPlugin"); //对应js中的plugin.method

        // 2.向该webView注入Java原生对象-GPSPlugin
        GPSPlugin pluginGPS = new GPSPlugin();
        webview.addJavascriptInterface(pluginGPS, "gpsPlugin"); //对应js中的plugin.method

        // TODO: 16/8/2 改为配置的形式,不能写死
        String filePath = "file:///android_asset/apps/eminCloud/www/html/index.html";
        webview.loadUrl(filePath);
    }

    private void loadLocalPage(){
        debugLog("loadLocalPage 7");

        webview = (WebView)findViewById(R.id.webView);
        String url = "file:///android_asset/apps/eminCloud/www/html/init.html";
        webview.loadUrl(url);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 添加Javascript接口,相当于一个JAVA类的别名
        webview.addJavascriptInterface(new EminBridge(),"EminBridge");

        // 在涉及到弹窗,如JS中的alert("message"),需要用到WebChromeClient
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // TODO Auto-generated method stub
                debugLog("onJsAlert view:" + view + " url:" + url + " message:" + message);
                message = "EminAlert:";
                return super.onJsAlert(view, url, message, result);
            }
        });

        /*
        // WebViewClient
        webView.setWebViewClient(new WebViewClient(){
            //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                debugLog("shouldOverrideUrlLoading url:" + url);
                view.loadUrl(url);
                return true;
            }

        });
        */
    }

    public void testTransiton(){
        webview = (WebView)findViewById(R.id.webView);
        String url = "file:///android_asset/apps/eminCloud/www/html/init.html";
        webview.loadUrl(url);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 添加Javascript接口,相当于一个JAVA类的别名
        webview.addJavascriptInterface(new EminBridge(),"EminBridge");

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                debugLog("shouldOverrideUrlLoading url:" + url);
                view.loadUrl(url);   //在当前的webview中跳转到新的url
                return true;
            }
        });

        // 在涉及到弹窗,如JS中的alert("message"),需要用到WebChromeClient
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // TODO Auto-generated method stub
                debugLog("onJsAlert view:" + view + " url:" + url + " message:" + message);
                message = "EminAlert:";
                return super.onJsAlert(view, url, message, result);
            }
        });

        debugLog("跳转测试");
    }


    // 独立以下这些API到plugin
//    @JavascriptInterface
//    public void insert(String msg){ //对应js中plugin.helloBridge("message")
//        debugLog("helloBridge called.....arg:" + msg);
//    }

//    @JavascriptInterface
//    public void getLocation(String msg){ //对应js中plugin.helloBridge("message")
//        debugLog("getLocation...arg:" + msg);
//    }


    private void debugLog(String info){
        System.out.println(info);
    }



}
