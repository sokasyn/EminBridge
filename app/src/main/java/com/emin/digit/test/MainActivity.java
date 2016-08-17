package com.emin.digit.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emin.digit.mobile.android.hybrid.EminBridge.R;
import com.emin.digit.mobile.android.hybrid.base.EMBridge;

public class MainActivity extends AppCompatActivity {

    private LinearLayout rootView;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // old package="com.emin.digit.android.eminbridge.eminbridge"
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_main);

        // 取消标题
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 继承自AppCompatActivity
//        getSupportActionBar().hide();

//        setup();
    }

    private void getRootView(){
//        findViewById(android.R.)
    }

    // - - - - - - - - - - - 基本配置 - - - - - - - - - - -

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        debugLog("MainActivity onKeyDown :" + keyCode);

        // 重写该方法,当点了设备的返回键,是返回上一个页面(如果有的话),而不是直接退出应用
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        } else{
            return super.onKeyDown(keyCode,event);
        }
    }

    // - - - - - - - - - - - 初始化 Start - - - - - - - - - - -
    private void setup(){
        // 隐藏默认标题栏

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView = (LinearLayout) inflater.inflate(R.layout.test_activity_main, null);
        setContentView(rootView);

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
//        String url = "file:///android_asset/apps/eminCloud/www/html/init.html";
        String url = "file:///android_asset/apps/scan/www/Scan_file.html";
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        webSettings.setCacheMode();

        // 添加Javascript接口,相当于一个JAVA类的别名,在Javascript中采用该名称调用EminBridge对象中的方法
//        webView.addJavascriptInterface(new EMBridge(this),"EminBridge");
        webView.addJavascriptInterface(new EMBridge(this,webView),"EminBridge");

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
            // URI
            Uri uri = Uri.parse(url);
            String host = uri.getHost();

            // 如果是本地的Page,在WebView中处理
            if(url.indexOf("file:///") != -1){
                debugLog("local html");
                return false;
            }
            debugLog("web html");
            view.loadUrl(url);
            return false;
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
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            debugLog("onLoadResource");
            super.onLoadResource(view, url);
        }

    }


    private boolean hasAnimated = false;
    class CustomChromeClient extends WebChromeClient{

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            System.out.println("load progress:" + newProgress );
            if(newProgress == 100){
                System.out.println("[EminChromeClient] 页面加载完成 100%");

                // 由于 onProgressChanged 会重复调用(2次),所以会导致动画效果重复执行了两次,如果动画完成,则屏蔽动画切换
                if(!hasAnimated){
                    System.out.println("= = = = = = = = =[EminChromeClient] 页面加载完成 will transition view :" + hasAnimated);
                    // 加载完成之后,加入动画效果实现页面的切换
                    transitionView(view);
                }else{
                    System.out.println("= = = = = = = = = [EminChromeClient] 页面加载完成 will not transition view:" + hasAnimated);
                }
            }
//            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onCloseWindow(WebView webView) {
            super.onCloseWindow(webView);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        /**
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            System.out.println("[EminChromeClient] onJsAlert");
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle("提示").setMessage(message).setPositiveButton("好的", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//                Log.v("onJsAlert", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("对话框")
                    .setMessage(message)
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//                Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
         * window.prompt('请输入您的域名地址', '618119.com');
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle("对话框").setMessage(message);

            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }

                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//                Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });

            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsPrompt(view, url, message, defaultValue,
            // result);
        }


        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
        }








    }


    // 页面在加载完成通过动画切换页面
    private void transitionView(WebView view) {
        debugLog("transitionView before animation :" + hasAnimated);
        startAnimation(view);
        hasAnimated = true;
        debugLog("transitionView after finished animation :" + hasAnimated);

    }

    private ImageView imageView = null;
    private void startAnimation(WebView view){
        debugLog("startAnimation 1111");
        if (imageView == null) {
            debugLog("11111 - 1111 imageView is null");
            imageView = new ImageView(MainActivity.this);
        }

        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            Bitmap b = Bitmap.createBitmap(bitmap);
            imageView.setImageBitmap(b);
        }

        debugLog("2222");
        // 当前布局载入当前页面的截图
        if(imageView.getParent() == null){
            rootView.addView(imageView);
        }

        Animation translate_in = AnimationUtils.loadAnimation(MainActivity.this, R.anim.transition_push_in);
        translate_in.setFillAfter(true);
        translate_in.setDuration(500);
        translate_in.setDetachWallpaper(true);
        view.setAnimation(translate_in);


        debugLog("33333");

        Animation translate_out = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tansition_pop_out);
        translate_out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                debugLog("translate_out 33333-1111");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
                debugLog("translate_out 33333-22222");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                debugLog("translate_out 33333-33333");
                if(null!=imageView){
                    MainActivity.this.rootView.removeView(imageView);
                    imageView=null;
                }
            }
        });
        translate_out.setFillAfter(true);
        translate_out.setDuration(500);
        translate_out.setDetachWallpaper(true);

        debugLog("44444");
        if(null != imageView) {
            debugLog("imageView not null");
            imageView.setAnimation(translate_out);
        }



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
