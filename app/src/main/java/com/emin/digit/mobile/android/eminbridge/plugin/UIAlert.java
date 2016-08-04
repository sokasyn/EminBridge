package com.emin.digit.mobile.android.eminbridge.plugin;

import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Samson on 16/8/4.
 */
public class UIAlert {

//    Context mContext;

    // 弹出一个自动隐藏的提示框
    public void toast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
