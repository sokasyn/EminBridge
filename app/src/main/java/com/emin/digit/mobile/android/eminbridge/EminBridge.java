package com.emin.digit.mobile.android.eminbridge;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.emin.digit.mobile.android.eminbridge.plugin.DatabasePlugin;
import com.emin.digit.mobile.android.eminbridge.plugin.GPSPlugin;
import com.emin.digit.mobile.android.eminbridge.plugin.UIAlert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Samson on 16/8/1.
 */
public class EminBridge {

    private Context mContext;

//    public EminBridge(){
//
//    }

    public EminBridge(Context context){
        this.mContext = context;
    }

    // - - - - - - - - - - - - - - 通过反射机制调用Plugin(原生JAVA类)
    /**
     * 同步执行plugin
     *
     * @param pluginName
     * @param methodName
     * @param arg
     */
    @JavascriptInterface
    public static String execSyncPlugin(String pluginName, String methodName, String arg){
        System.out.println("[EminBridge] 同步调用 pluginName:" + pluginName + " methodName:" + methodName + " arg:" + arg);

        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            Class pluginClass = Class.forName(pluginName);
            Method method = pluginClass.getDeclaredMethod(methodName,String.class);

            debugLog("[EminBridge] execSyncPlugin to invoke:当前线程 id:" + Thread.currentThread().getId());

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
                    debugLog("[EminBridge] execPlugin to invoke:当前线程 id:" + Thread.currentThread().getId());
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

    // 检查插件是否存在
    private static boolean isExistPlugin(String pluginName){
        return true;
    }

    private static void debugLog(String info){
        System.out.println(info);
    }

}
