package com.emin.digit.mobile.android.hybrid.framework;

/**
 * Created by Samson on 16/8/1.
 */
public class EmDatabase {

    public static void insert(String sqlString){
        System.out.println("[Emin Framework] EmDatabse insert called...sqlString:" + sqlString);
        System.out.println("[Emin Framework] EmDatabse insert Thread id :" + Thread.currentThread().getId());
    }
}
