package com.emin.digit.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Samson on 16/8/15.
 */
public class TestBarcode extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void setup(){
//        startActivityForResult();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
