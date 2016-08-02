package com.emin.digit.mobile.android.eminbridge;

/**
 * Created by Samson on 16/8/1.
 */
public class PageManager {

    private static PageManager pageManager = new PageManager();

    private PageManager(){

    }

    public static PageManager getInstance(){
        return pageManager;
    }

    // 加载本地的页面资源
    public static void loadWebViewFromLocal(String pagePath){

    }

    // 加载远程服务器端的资源
    public static void loadWebViewFromServer(String webUrl){
//        WebView webView = new WebView();

    }

    // 页面的跳转
    public static void transitionWebView(int fromId , int toId){

    }

    // web层javascript调用到这个方法就成功了！
    public static void invokePlugin(String pluginName){
        EminBridge.executePlugin(pluginName);
    }

}
