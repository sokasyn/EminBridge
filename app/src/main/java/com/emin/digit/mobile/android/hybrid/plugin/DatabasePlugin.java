package com.emin.digit.mobile.android.hybrid.plugin;

import com.emin.digit.mobile.android.hybrid.framework.EmDatabase;

/**
 * Created by Samson on 16/8/1.
 */
public class DatabasePlugin {

    public static void insert(String arg){
        System.out.println("[DatabasePlugin] insert called..arg:" + arg);
        System.out.println("[DatabasePlugin] insert Thread id :" + Thread.currentThread().getId());

        EmDatabase.insert(arg);
    }

    public static void update(){

    }
}
