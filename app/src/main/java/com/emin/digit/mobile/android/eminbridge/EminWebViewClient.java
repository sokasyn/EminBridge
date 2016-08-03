package com.emin.digit.mobile.android.eminbridge;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Samson on 16/8/3.
 */
public class EminWebViewClient extends WebViewClient {

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
