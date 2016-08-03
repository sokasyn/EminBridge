package com.emin.digit.mobile.android.eminbridge;

import android.webkit.JavascriptInterface;

import com.emin.digit.mobile.android.eminbridge.plugin.DatabasePlugin;
import com.emin.digit.mobile.android.eminbridge.plugin.GPSPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Samson on 16/8/1.
 */
public class EminBridge {

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
        debugLog("同步调用:当前线程 id:" + Thread.currentThread().getId());
        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            Class pluginClass = Class.forName(pluginName);
            Method method = pluginClass.getDeclaredMethod(methodName,String.class);
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
        System.out.println("[EminBridge] 异步调用 pluginName:" + pluginName + " methodName:" + methodName + " arg:" + arg);

        Object rtnObj = null;
        // 通过pluginName找到JAVA类,并执行method
        try{
            final Class pluginClass = Class.forName(pluginName);
            final Method method = pluginClass.getDeclaredMethod(methodName,String.class);
            final String argument = arg;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    debugLog("异步调用,当前线程id:" + Thread.currentThread().getId());
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

    // 检查插件是否存在
    private static boolean isExistPlugin(String pluginName){
        return true;
    }


    // - - - - - - - - - - - - - - - 直接调用Plugin
    public static DatabasePlugin dbPlugin = new DatabasePlugin();
    public GPSPlugin GPSPlugin = new GPSPlugin();

    public class CameraPlugin{

        @JavascriptInterface
        public void takePhoto(){
            debugLog("take photo..");
        }
    }

    private static void debugLog(String info){
        System.out.println(info);
    }
}
