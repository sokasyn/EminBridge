package com.emin.digit.mobile.android.hybrid.base;

import android.webkit.WebView;

/**
 * 对Plugin方法参数的封装
 * 保证通过反射机制调用Plugin方法的时候,参数的统一
 * Javascript在传入参数可能是多个,通过数组的形式,调用EminBridge的exec方法时,
 * 这个参数数组对象对应,PluginParams的arguments
 *
 *
 * Created by Samson on 16/8/15.
 */
public class PluginParams {

    public WebView webView;

    public String[] arguments;

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
