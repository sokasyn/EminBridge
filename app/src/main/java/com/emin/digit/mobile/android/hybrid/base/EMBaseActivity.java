package com.emin.digit.mobile.android.hybrid.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 基础Activity,封装了生命周期,以及设备返回键事件
 *
 * Created by Samson on 16/8/11.
 */
public class EMBaseActivity extends Activity{

    private static final String TAG = EMBaseActivity.class.getSimpleName();

    // - - - - - - - - - - 生命周期 - - - - - - - - - -

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 通过Intent开启Activity
     *
     * @param intent
     */
    protected void openActivity(Intent intent){
        startActivity(intent);
    }

    /**
     * 通过目标Activity类开启Activity
     *
     * @param targetClass
     */
    protected void openActivity(Class<?> targetClass){
        Intent intent = new Intent();
        intent.setClass(this, targetClass);
        startActivity(intent);
    }
}
