package com.emin.digit.test;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;

import com.emin.digit.mobile.android.hybrid.EminBridge.R;

/**
 * Created by Samson on 16/9/13.
 */
public class TestCameraActivity extends Activity {

    private static final String TAG = TestCameraActivity.class.getSimpleName();

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_camera);
    }
}
