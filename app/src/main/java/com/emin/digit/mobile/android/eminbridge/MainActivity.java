package com.emin.digit.mobile.android.eminbridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.emin.digit.android.eminbridge.eminbridge.R;
import com.emin.digit.mobile.android.eminbridge.plugin.GPSPlugin;

public class MainActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("= = = = = = = = 111");
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
        debugLog("locad init local page called 5..");
        // 加载本地的Html资源
        loadLocalPage();
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
//        webview.addJavascriptInterface(new DatabasePlugin(),"EminBridge.dbplugin");
//        webview.addJavascriptInterface(new EminBridge().DBPlugin,"DBPlugin");

        webview.addJavascriptInterface(new EminBridge().dbPlugin,"EminBridge.dp");

        // 在涉及到弹窗,如JS中的alert("message"),需要用到WebChromeClient
        webview.setWebChromeClient(new EminChromeClient());

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



}
