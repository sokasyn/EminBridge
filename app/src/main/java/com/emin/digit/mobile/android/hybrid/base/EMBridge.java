package com.emin.digit.mobile.android.hybrid.base;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.emin.digit.mobile.android.hybrid.plugin.UIAlert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Samson on 16/8/11.
 */
public class EMBridge {

    private static final String TAG = EMBridge.class.getSimpleName(); // 日志标志
    private Context mContext; // 上下文

    // 无参构造方法
    public EMBridge(){
        this(null);
    }

    /**
     * 构造方法,在原生层中,Android的一些功能需要用到Context,则需要设置该Context.
     * 当然,公开的mContext的存取方法也可以随时的设置它
     *
     * @param context android上下文
     */
    public EMBridge(Context context){
        this.mContext = context;
    }

    /**
     * 同步执行plugin
     *
     *
     * @param pluginName
     * @param methodName
     * @param arg
     */
    @JavascriptInterface
    public static String execSyncPlugin(String pluginName, String methodName, String arg){
        // TODO: 16/8/4 传入参数的设计,可以采用JSONArray来储存
        // TODO: 16/8/4 返回值的设计,可以采用JSON封装的形式
        // TODO: 16/8/4 回调函数的设计,在入参的时候,将JS的回调保存起来,通过WebView的loadUrl()执行该js函数！
        System.out.println("[EminBridge] 同步调用 pluginName:" + pluginName + " methodName:" + methodName + " arg:" + arg);

        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            Class pluginClass = Class.forName(pluginName);
            Method method = pluginClass.getDeclaredMethod(methodName,String.class);

            Log.d(TAG,"[EminBridge] execSyncPlugin to invoke:当前线程 id:" + Thread.currentThread().getId());

            rtnObj =  method.invoke(pluginClass.newInstance(),arg);
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

    /**
     * 异步执行plugin
     *
     * @param pluginName
     * @param methodName
     * @param arg
     */
    @JavascriptInterface
    public static void execPlugin(String pluginName, String methodName, String arg){
        System.out.println("[EminBridge] execPlugin pluginName:" + pluginName + " methodName:" + methodName + " arg:" + arg);

        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            final Class pluginClass = Class.forName(pluginName);
            final Method method = pluginClass.getDeclaredMethod(methodName,String.class);
            final String argument = arg;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO: 16/8/3 异步调用的多线程交互问题,可以封装Handler
                    try{
                        method.invoke(pluginClass.newInstance(),argument);
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

    // 测试需要Android context的功能,
    // TODO: 16/8/4 在使用反射调用时候,webView/context参数的设计
    @JavascriptInterface
    public void toast(String text){
        new UIAlert().toast(mContext,text);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
