package com.emin.digit.mobile.android.eminbridge;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Samson on 16/8/3.
 */
public class EminWebViewClient extends WebViewClient {

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

    private void debugLog(String info){
        System.out.println("[EminWebViewClient] " + info);
    }
}
