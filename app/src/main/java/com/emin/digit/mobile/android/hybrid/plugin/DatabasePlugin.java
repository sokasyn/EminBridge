package com.emin.digit.mobile.android.hybrid.plugin;

import com.emin.digit.mobile.android.hybrid.base.PluginParams;
import com.emin.digit.mobile.android.hybrid.framework.EmDatabase;

/**
 * Created by Samson on 16/8/1.
 */
public class DatabasePlugin {

//    public static void insert(String arg){
//        System.out.println("[DatabasePlugin] insert called..arg:" + arg);
//        System.out.println("[DatabasePlugin] insert Thread id :" + Thread.currentThread().getId());
//
//        EmDatabase.insert(arg);
//    }

    public static void insert(PluginParams params){
        System.out.println("[DatabasePlugin] insert called..arg:" + params);
        System.out.println("[DatabasePlugin] insert Thread id :" + Thread.currentThread().getId());

        String sqlString = params.getArguments()[0];
        System.out.println("arguments in params[0]:" + sqlString);

        EmDatabase.insert(sqlString);
    }

    public static void update(PluginParams params){

    }
}
