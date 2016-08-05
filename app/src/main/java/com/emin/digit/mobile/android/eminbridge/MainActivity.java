package com.emin.digit.mobile.android.eminbridge;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emin.digit.android.eminbridge.eminbridge.R;

import org.json.JSONObject;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("= = = = = = = = 2222");

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
        webView = (WebView)findViewById(R.id.webView);
        String url = "file:///android_asset/apps/eminCloud/www/html/init.html";
//        String url = "http://www.baidu.com";
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 添加Javascript接口,相当于一个JAVA类的别名,在Javascript中采用该名称调用EminBridge对象中的方法
        webView.addJavascriptInterface(new EminBridge(this),"EminBridge");
//        webview.addJavascriptInterface(new DatabasePlugin(),"EminBridge.databasePlugin"); //NG
//        webview.addJavascriptInterface(new DatabasePlugin(),"databasePlugin"); // 将Plugin直接注入,但是脱离了EminBridge
//        webview.addJavascriptInterface(new EminBridge().DBPlugin,"DBPlugin"); // NG,入住的JAVA对象的属性不能为被访问,只有public和添加注解的方法可被访问
//        webview.addJavascriptInterface(new EminBridge().dbPlugin,"EminBridge.dp"); // NG

        // 在涉及到弹窗,如JS中的alert("message"),需要用到WebChromeClient
        webView.setWebChromeClient(new EminChromeClient());

        // 在涉及到页面的跳转,默认是通过系统的浏览器加载,如果向在webview中实现,则需要配置WebViewClient
        webView.setWebViewClient(new CustomWebViewClient());

        // webView 执行js代码
//        webView.loadUrl("javascript:console.log('execute javascript directly in native webview')");

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

    class CustomWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            debugLog("shouldOverrideUrlLoading : url:" + url);
            /*
             * webView自己跳转,我们可以不做处理,如不需要加上view.loadUrl(url)
             * 另外,Android API中该方法默认也是return false的,不用重载也没问题
             * 但是在做一个特殊的,比如发送邮件,短信,打电话等,这种必须要特殊处理
             */
            return false;

//            view.loadUrl(url);
            // return true; // true,跳转失效,这种情况下需要自己去处理,如,得加上view.loadUrl(url)才会跳转

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            debugLog("onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        /*
         * 如果Javascript函数还没有加载出来时,那么执行的WebView的loadUrl()会报函数is not defined异常
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            debugLog("onPageFinished");
            super.onPageFinished(view, url);
            webView.loadUrl("javascript:functionInJs()");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            debugLog("onLoadResource");
            super.onLoadResource(view, url);
        }
    }


    /*
    public static void(WebView web view, JSONObject jsonObj, Callback callback){

    }*/


    private void testExecJavascript(){

        // 简单的执行提示弹框
//        webview.loadUrl("javascript:alert('execute javascript directly in native webView')");
//        webview.loadUrl("javascript:alert('"+ alertMsg +"')"); // 注意alert()中的string要加引号'',或者转义的""

        String prefix_execJs = "javascript:";
        String alertMsg = "execute javascript directly in native webview";
        String str_execJs = prefix_execJs + "alert('" + alertMsg + "')";
        webView.loadUrl(str_execJs);
    }

    // 测试调用js函数
    private void testExecJavascriptFunction(){

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                debugLog("onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            /*
             * 如果Javascript函数还没有加载出来时,那么执行的WebView的loadUrl()会报函数is not defined异常
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                debugLog("onPageFinished");
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:functionInJs()");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                debugLog("onLoadResource");
                super.onLoadResource(view, url);
            }
        });
    }

    /*
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
    */

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
