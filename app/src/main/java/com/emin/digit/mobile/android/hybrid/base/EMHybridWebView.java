package com.emin.digit.mobile.android.hybrid.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

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
        settingWebView(); // WebView其它配置
    }

    // WebView配置
    private void settingWebView(){
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true); // 支持Javascript
        String encodingName = "UTF-8";
        settings.setDefaultTextEncodingName(encodingName); // 配置字符编码
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

    public boolean isWebPageAnimated(){
        return webPageAnimated;
    }

    public void setWebPageAnimated(boolean animated) {
        this.webPageAnimated = animated;
    }
}
