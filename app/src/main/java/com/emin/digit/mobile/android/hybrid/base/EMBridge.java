package com.emin.digit.mobile.android.hybrid.base;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.emin.digit.mobile.android.hybrid.plugin.PluginAlert;
import com.emin.digit.test.zxing.BarcodeController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Web前端与原生层交互的桥梁
 * HTML(javascript)统一通过此被注入到WebView的对象,来实现对原生Java类方法的调用
 *
 * Created by Samson on 16/8/11.
 */
public class EMBridge {

    private static final String TAG = EMBridge.class.getSimpleName(); // 日志标志
    private Context mContext; // 上下文
    private WebView mWebView;

    // 无参构造方法
//    public EMBridge(){
//        this(null,null);
//    }

    /**
     * 构造方法,在原生层中,Android的一些功能需要用到Context,则需要设置该Context.
     * 当然,公开的mContext的存取方法也可以随时的设置它
     *
     * @param context android上下文
     */
//    public EMBridge(Context context){
//        this(context,null);
//    }

//    public EMBridge(WebView webView){
//        this(null,webView);
//    }

    public EMBridge(Context context, WebView webView){
        mContext = context;
        mWebView = webView;
    }

    /**
     * 同步执行plugin
     *
     * @param pluginName
     * @param methodName
     * @param args
     */
    @JavascriptInterface
    public String execSyncPlugin(String pluginName, String methodName, String[] args){
        // TODO: 16/8/4 传入参数的设计,可以采用JSONArray来储存
        // TODO: 16/8/4 返回值的设计,可以采用JSON封装的形式
        // TODO: 16/8/4 回调函数的设计,在入参的时候,将JS的回调保存起来,通过WebView的loadUrl()执行该js函数！
        System.out.println("[EminBridge] 同步调用 pluginName:" + pluginName + " methodName:" + methodName + " args:" + args);

        if(args != null){
            for(int i = 0 ; i < args.length; i++){
                Log.d(TAG,"execSyncPlugin args[" + i + "] :" + args[i]);
            }
        }

        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            Class pluginClass = Class.forName(pluginName);
            Method method = pluginClass.getDeclaredMethod(methodName,PluginParams.class);

            Log.d(TAG,"[EminBridge] execSyncPlugin invoked:当前线程 id:" + Thread.currentThread().getId());

            PluginParams params = new PluginParams();
            params.setWebView(mWebView);
            if(args != null){
                params.setArguments(args);
            }
            rtnObj = method.invoke(pluginClass.newInstance(),params);

            if(rtnObj != null){
                Log.d(TAG,"return value is not null ");
            }else {
                Log.d(TAG,"return value is null ");
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }
        return (rtnObj == null) ? null : rtnObj.toString();
    }

    @JavascriptInterface
    public String execSyncPlugin(String pluginName, String methodName){
        Log.d(TAG,"execSyncPlugin:" + pluginName + " methodName:" + methodName);
        return execSyncPlugin(pluginName,methodName,null);
    }

    /**
     * 异步执行plugin
     *
     * @param pluginName
     * @param methodName
     * @param args
     */
    @JavascriptInterface
    public void execPlugin(String pluginName, String methodName, String[] args){
        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            final Class pluginClass = Class.forName(pluginName);
            final Method method = pluginClass.getDeclaredMethod(methodName,String.class);
            final String[] arguments = args;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO: 16/8/3 异步调用的多线程交互问题,可以封装Handler
                    try{

                        PluginParams params = new PluginParams();
                        params.setWebView(mWebView);
                        if(arguments != null){
                            params.setArguments(arguments);
                        }
                        method.invoke(pluginClass.newInstance(),params);
                    }catch (InstantiationException e){
                        e.printStackTrace();
                    }catch (IllegalAccessException e){
                        e.printStackTrace();
                    }catch (InvocationTargetException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }
    }

    /**
     * javascript的页面跳转事件
     *
     * @param url
     */
    @JavascriptInterface
    public void loadWebPage(final String url){
        final EMHybridActivity activity = (EMHybridActivity)mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.loadPage(url);
            }
        });
    }

    /**
     * javascript的页面返回上一页事件
     */
    @JavascriptInterface
    public void lastWebPage(){

        final EMHybridActivity activity = (EMHybridActivity)mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
            }
        });

    }
    
    /**
     * 
     * javascript的页面返回上一页事件
     */
    // TODO: 16/9/22 临时为二维码扫描返回
    @JavascriptInterface
    public void barcodeBack(){

        final EMHybridActivity activity = (EMHybridActivity)mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                BarcodeController.getInstance().stop();
                activity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
            }
        });

    }

    // TODO: 2016/10/11 测试红点服务
    // 无回调
    @JavascriptInterface
    public void reddotRegister(){
        ReddotManager.getInstance().register(mWebView);
    }

    // callback为注册的回调方法名
    @JavascriptInterface
    public void reddotRegister(String callback){
        ReddotManager.getInstance().register(mWebView,callback);
    }

    // 因为没有后台service推送交互，临时在界面js做个方法触发
    @JavascriptInterface
    public void pushMessageComing(){
        Log.d(TAG,"3333 pushMessageComing");
        ReddotManager.getInstance().reddotCounting();
    }

    @JavascriptInterface
    public void toast(String text){
        new PluginAlert().toast(mContext,text);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
