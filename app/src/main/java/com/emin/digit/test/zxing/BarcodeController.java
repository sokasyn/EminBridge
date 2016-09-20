package com.emin.digit.test.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emin.digit.mobile.android.hybrid.EminBridge.R;
import com.emin.digit.mobile.android.hybrid.base.EMBaseActivity;
import com.emin.digit.test.zxing.android.BeepManager;
import com.emin.digit.test.zxing.android.CaptureActivity;
import com.emin.digit.test.zxing.android.CaptureActivityHandler;
import com.emin.digit.test.zxing.android.InactivityTimer;
import com.emin.digit.test.zxing.android.IntentSource;
import com.emin.digit.test.zxing.camera.CameraManager;
import com.emin.digit.test.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Samson on 16/9/19.
 */
public class BarcodeController implements SurfaceHolder.Callback,IBarHandler{

    private static final String TAG = BarcodeController.class.getSimpleName();

    EMBaseActivity activity;
    // 相机控制
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    // 电量控制
    private InactivityTimer inactivityTimer;
    // 声音、震动控制
    private BeepManager beepManager;

    private ImageButton imageButton_back;

    private FrameLayout mainView;


    // - - - - - - - - IBarHandler Interface Start - - - - - - - -
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }


    /**
     * 扫描成功，处理反馈信息
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.d(TAG,"handleDecode");
        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        //这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (fromLiveScan) {
            Log.d(TAG,"fromLiveScan");
            beepManager.playBeepSoundAndVibrate();
            Log.d(TAG,"扫描成功");
//
//            Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();
//
//            Intent intent = getIntent();
//            intent.putExtra("codedContent", rawResult.getText());
//            intent.putExtra("codedBitmap", barcode);
//            setResult(RESULT_OK, intent);
//            finish();
        }

    }
    // - - - - - - - - IBarHandler Interface End - - - - - - - -

    public void loadBarcodeView(EMBaseActivity act){
        Log.d(TAG,"&&&&&&& loadBarcodeView thread id:" + Thread.currentThread().getId());
        activity = act;
        init();
    }

    private void test(){

        Button button = new Button(activity);
        activity.addContentView(button,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void init(){
        cameraManager = new CameraManager(activity.getApplication());

        LayoutInflater inflater = LayoutInflater.from(activity);
        FrameLayout layout = (FrameLayout)inflater.inflate(R.layout.barcode_capture_2,null);

        viewfinderView = (ViewfinderView) layout.findViewById(R.id.viewfinder_view_2);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        SurfaceView surfaceView = (SurfaceView) layout.findViewById(R.id.preview_view_2);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();


        // add view
        FrameLayout actFrameLayout = (FrameLayout) activity.findViewById(R.id.idActivityHybrid);

        // 将barcodeFrame 加载到activity的layout中
        mainView = new FrameLayout(activity.getApplicationContext());
        FrameLayout.LayoutParams bcLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                600
//                ViewGroup.LayoutParams.MATCH_PARENT
        );
        bcLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        bcLayoutParams.leftMargin = 0;
        bcLayoutParams.topMargin = 200;
        actFrameLayout.addView(mainView,bcLayoutParams);

        if(surfaceView.getParent() != null) {
            Log.d(TAG,"surfaceView has parent");
//            surfaceView.getParent()
            layout.removeView(surfaceView);
        }

        if(viewfinderView.getParent() != null) {
            Log.d(TAG,"viewfinderView has parent");
//            surfaceView.getParent()
            layout.removeView(viewfinderView);
        }

        mainView.addView(surfaceView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mainView.addView(viewfinderView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            Log.d(TAG,"111 initCamera()");
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            Log.d(TAG,"222 重置callback，等待surfaceCreated()来初始化camera");
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated");
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 初始化Camera
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"initCamera");
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.d(TAG,"cameraManager isOpen");
            return;
        }
        try {
            Log.d(TAG,"cameraManager openDriver");
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
//            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
//            displayFrameworkBugMessageAndExit();
        }
    }
}
