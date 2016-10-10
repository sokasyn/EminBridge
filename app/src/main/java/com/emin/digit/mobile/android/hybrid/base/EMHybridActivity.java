package com.emin.digit.mobile.android.hybrid.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.emin.digit.mobile.android.hybrid.EminBridge.R;
import com.emin.digit.test.MainActivity;
import com.emin.digit.test.zxing.BarcodeController;

import java.util.LinkedList;
import java.util.logging.Handler;

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

    private static final String sBaseWebPageUrl = "file:///android_asset/apps/test/www/html/";
//    private static final String sBaseWebPageUrl = "file:///android_asset/apps/AppPage/www/";

    //    private FrameLayout rootLayout;
    private FrameLayout containerView;

    private EMHybridWebView mWebView; // 加载HTML页面的WebView
    private String mUrl;              // HTML加载页面Url


    private Handler handler;
    private ProgressDialog progressDialog;

    private static LinkedList<EMHybridWebView> webViewList = new LinkedList<EMHybridWebView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setup();
    }

    // 初始化
    private void setup(){
        Log.d(TAG,"= = = = = = = = = = 1");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消默认的标题栏以及全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.bridge_activity);
//        rootLayout = (FrameLayout) findViewById(R.id.idActivityHybrid);
        containerView = (FrameLayout)findViewById(R.id.idContainerView);

        // Activity初始化加载webApp的首页地址
        // TODO: 16/8/17 改善:通过读取配置文件获取
        //mUrl = "file:///android_asset/apps/test/www/html/init.html";
        mUrl = "init.html";
        loadPage(mUrl);
    }

    /**
     * 加载新的web页面
     * Web前端传入的是相对路劲,需要
     *
     * @param url
     */
    public void loadPage(String url){
        url = getFullPathForFile(url);
        mWebView = createWebView(url);
        webViewList.add(mWebView);

        //progressDialog = ProgressDialog.show(this, "标题", "加载中，请稍后……");

        Log.d(TAG,"11111");
        mWebView.loadUrl(url);

        Log.d(TAG,"222222");
        containerView.addView(mWebView);

        // 切换动画
        Animation translate_in = AnimationUtils.loadAnimation(mWebView.getContext(), R.anim.transition_push_in);
        translate_in.setFillAfter(true);
//        translate_in.setDuration(300); // 持续时间在res/anim 相关xml文件中配置
        translate_in.setDetachWallpaper(true);
        mWebView.setAnimation(translate_in);
    }

    /**
     * 创建WebView
     *
     * @param url 页面url
     *
     * @return EMHybridWebView
     */
    public EMHybridWebView createWebView(String url){
        Log.d(TAG,"createWebView:" + url);
        Log.d(TAG,"= = = = = = this:" + this);
        Log.d(TAG,"= = = = = = EMHybridActivity.this:" + EMHybridActivity.this);
        return new EMHybridWebView(this, EMHybridActivity.this, url);
    }

    public void preloadWebViews(String[] urlArray){

    }

    // web前端页面跳转传的是相对路劲,必须拼接成android 资源目录下的全路劲
    private String getFullPathForFile(String url){
        return sBaseWebPageUrl + url;
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
        destroyAllWebViews();
    }

    private void destroyWebView(){

    }

    // 销毁所有的webView
    private void destroyAllWebViews(){
        for(WebView webView : webViewList){
            webView.destroy();
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
            /*
            // 如果有历史记录,在返回上一个页面
            if(mWebView.canGoBack()){
                mWebView.goBack();
            }else {
                // 没有历史记录,则提示用户是否期望退出应用
                exitAppWhenDoublePressed();
            }
            */

            BarcodeController.getInstance().stop();

            // 每个webView对应一个页面
            int count = webViewList.size();
            Log.d(TAG,"webViewList.size():" + count);
            for(int i = 0; i < count; i++){
                Log.d(TAG,"webView in list[:" + i + "]:" + (EMHybridWebView)webViewList.get(i));
            }
            if(webViewList.size() <= 1){
                exitAppWhenDoublePressed();
            }else {
                //Toast.makeText(this, "前面还加载这web view", Toast.LENGTH_SHORT).show();
                int childViewCount = containerView.getChildCount();
                Log.d(TAG,"childViewCount:" + childViewCount);
                containerView.removeViewAt(childViewCount - 1);

                mWebView = webViewList.getLast();
                containerView.removeView(mWebView);
                Animation translate_out = AnimationUtils.loadAnimation(mWebView.getContext(), R.anim.tansition_pop_out);
                translate_out.setFillAfter(true);
//                translate_out.setDuration(300);
                translate_out.setDetachWallpaper(true);
                mWebView.setAnimation(translate_out);


                webViewList.removeLast();
                mWebView = webViewList.getLast();
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

    public static LinkedList<EMHybridWebView> getWebViewList() {
        return webViewList;
    }

    public FrameLayout getContainerView() {
        return containerView;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }


    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    private void showInfo(){
        Log.d(TAG,"= = = = = = EMHybridActivity:" + this);
        Log.d(TAG,"= = = = = = EMHybridActivity.this:" + EMHybridActivity.this);
        Log.d(TAG,"= = = = = = webView in Activity initialize:" + mWebView);
        Log.d(TAG,"= = = = = = WebView getContext:" + mWebView.getContext());
    }

}
