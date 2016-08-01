package com.emin.digit.mobile.android.eminbridge.plugin;

import com.emin.digit.mobile.android.eminbridge.framework.EmDatabase;

/**
 * Created by Samson on 16/8/1.
 */
public class DatabasePlugin {

    public static void insert(){
        System.out.println("DatabasePlugin insert called..");
        EmDatabase.insert();
    }
}
