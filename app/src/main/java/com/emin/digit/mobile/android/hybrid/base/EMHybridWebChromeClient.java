package com.emin.digit.mobile.android.hybrid.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emin.digit.android.eminbridge.eminbridge.R;

/**
 * Created by Samson on 16/8/11.
 */
public class EMHybridWebChromeClient extends WebChromeClient{

    private static final String TAG = EMHybridWebChromeClient.class.getSimpleName();

    private boolean hasAnimated = false; // onProgressChanged多次调用导致动画也会多次调用的问题
//    private static EMHybridWebView rootView;

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
         final EMHybridWebView rootView = (EMHybridWebView)view;
        boolean webPageAnimated = rootView.isWebPageAnimated();
        Log.d(TAG,"hasAnimated :"+hasAnimated + " webPageAnimated:"+webPageAnimated);

        if(newProgress == 100){
            Log.d(TAG,"= = = = = = = 页面加载完成 100%");
            if(webPageAnimated || (!hasAnimated )){
                 Log.d(TAG,"页面加载 100% will transition view");
                // 加载完成之后,加入动画效果实现页面的切换
                transitionView(view);
            }else{
                Log.d(TAG,"页面加载100% will not transition view");
            }
        }
//        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
    }

    /**
     * 可以监听到web的控制台信息
     * 比如Js的错误信息
     *
     * @param consoleMessage
     * @return
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }

    // - - - - - - - - - - 切换动画处理 - - - - - - - - - -
    // 页面在加载完成通过动画切换页面
    private void transitionView(WebView view) {
        Log.d(TAG,"transitionView:" + hasAnimated);
        hasAnimated = true;
        startAnimation(view);
        Log.d(TAG,"###### after transitionView:" + hasAnimated);
    }

    private ImageView imageView = null;
    private void startAnimation(WebView view){

//        Activity hybridActivity = ((EMHybridWebView)view).getActivity();
//        LayoutInflater inflater = (LayoutInflater) hybridActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
//        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.hybrid_webview, null);

        final WebView rootView = (EMHybridWebView)view;
        Log.d(TAG,"startAnimation 1111");
        if (imageView == null) {
            Log.d(TAG,"11111 - 1111 imageView is null");
            imageView = new ImageView(view.getContext());
        }

        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            Bitmap b = Bitmap.createBitmap(bitmap);
            imageView.setImageBitmap(b);
        }

        Log.d(TAG,"2222");
        // 当前布局载入当前页面的截图
        if(imageView.getParent() == null){
            rootView.addView(imageView);
        }

        Animation translate_in = AnimationUtils.loadAnimation(view.getContext(), R.anim.transition_in);
        translate_in.setFillAfter(true);
        translate_in.setDuration(500);
        translate_in.setDetachWallpaper(true);
        view.setAnimation(translate_in);


        Log.d(TAG,"33333");

        Animation translate_out = AnimationUtils.loadAnimation(view.getContext(), R.anim.tansition_out);
        translate_out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                Log.d(TAG,"translate_out 33333-1111");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
                Log.d(TAG,"translate_out 33333-22222");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG,"translate_out 33333-33333");
                if(null != imageView){
                    rootView.removeView(imageView);
                    imageView=null;
                }
            }
        });
        translate_out.setFillAfter(true);
        translate_out.setDuration(500);
        translate_out.setDetachWallpaper(true);

        Log.d(TAG,"44444");
        if(null != imageView) {
            Log.d(TAG,"imageView not null");
            imageView.setAnimation(translate_out);
        }
    }




}
