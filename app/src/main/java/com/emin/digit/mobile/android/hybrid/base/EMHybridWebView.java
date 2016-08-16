package com.emin.digit.mobile.android.hybrid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 *
 * 混合开发WebView封装
 *
 * Created by Samson on 16/8/5.
 */
public class EMHybridWebView extends WebView {

    private static final String TAG = EMHybridWebView.class.getSimpleName();

    private Context mContext;   // 上下文
    private Activity mActivity; // 关联的Activity
    private EMHybridWebViewClient mWebViewClient;         // WebViewClient
    private EMHybridWebChromeClient mWebViewChromeClient; // WebChromeClient
    private String mUrl; // 加载的资源地址

    // WebView注入的对象暴露给javascript的名称
    private static final String INJECTED_BRIDGE_NAME = "EminBridge";

    private ProgressBar mProgressBar;   // 网页加载的进度条

    private FrameLayout mLayout;

    private boolean webPageAnimated = false;

    // - - - - - - - - - - - 构造方法 - - - - - - - - - - -

    public EMHybridWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EMHybridWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EMHybridWebView(Context context, Activity activity){
        super(context);
        mActivity = activity;
        init(context);
    }

    public EMHybridWebView(Context context, Activity activity, String url){
        super(context);
        Log.d(TAG,"EMHybridWebView constructor context:" + context);
        Log.d(TAG,"EMHybridWebView constructor activity:" + activity);
        Log.d(TAG,"EMHybridWebView constructor url:" + url);
        mActivity = activity;
        mUrl = url;
        init(context);
    }

    // 私有初始化方法,内部进行WebView各种配置
    private void init(Context context){
        Log.d(TAG,"init");
        mContext = context;
        mLayout = new FrameLayout(context);
        this.setWebViewClient(new EMHybridWebViewClient());     // 设置WebViewClient
        this.setWebChromeClient(new EMHybridWebChromeClient()); // 设置WebChromeClient
        settingWebView();     // WebView其它配置
        //settingProgressBar(); // 加载进度条配置

        configJavascriptInterface();

        if(mUrl != null){
            loadUrl(mUrl);
        }
    }

    // WebView配置
    private void settingWebView(){
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true); // 支持Javascript
        String encodingName = "UTF-8";
        settings.setDefaultTextEncodingName(encodingName); // 配置字符编码

        // 屏蔽长按事件。默认情况下长按,会出现全选,复制等小功能
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"LongClick view:" + v);
                return true;
            }
        });
    }

    // 注入EminBridge对象
    private void configJavascriptInterface(){
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            Log.i(TAG, "Disabled addJavascriptInterface() bridge since Android version is old.");
            return;
        }
        EMBridge injectedObject = new EMBridge(mActivity, this);   // 注入的对象
        String nameUsedInJs = INJECTED_BRIDGE_NAME; // javascript通过该名字调用注入对象的方法
        this.addJavascriptInterface(injectedObject, nameUsedInJs);
    }

    // 网页加载进度条配置
    private void settingProgressBar(){
        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
        this.addView(mProgressBar);
    }

    public FrameLayout getLayout(){
        return mLayout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyDown keyCode:" + keyCode + "KeyEvent:" + event);
        return super.onKeyDown(keyCode, event);
    }

    public Activity getActivity() {
        return mActivity;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public boolean isWebPageAnimated(){
        return webPageAnimated;
    }

    public void setWebPageAnimated(boolean animated) {
        this.webPageAnimated = animated;
    }
}
