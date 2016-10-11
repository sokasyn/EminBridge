package com.emin.digit.test.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.Tag;
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
import com.emin.digit.mobile.android.hybrid.base.EMHybridActivity;
import com.emin.digit.mobile.android.hybrid.base.EMHybridWebView;
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

    // TODO: 2016/10/9 测试
    private boolean isLoaded;

    public boolean isLoaded() {
        return isLoaded;
    }

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
        Log.d(TAG," ############ handleDecode:");
//        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        //这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (fromLiveScan) {
            Log.d(TAG,"fromLiveScan");
            Log.d(TAG,"扫描成功,Value:" + rawResult.getText());
            Toast.makeText(activity, "扫描成功", Toast.LENGTH_SHORT).show();

            // 回传到WebView
            EMHybridWebView webView = EMHybridActivity.getWebViewList().getLast();

            String callBackName = "barcodeResult";
            webView.loadUrl("javascript:" + callBackName + "('" + rawResult.getText() + "')");

//            beepManager.playBeepSoundAndVibrate();
//            Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();
//            Intent intent = getIntent();
//            intent.putExtra("codedContent", rawResult.getText());
//            intent.putExtra("codedBitmap", barcode);
//            setResult(RESULT_OK, intent);
//            finish();
        }

    }
    // - - - - - - - - IBarHandler Interface End - - - - - - - -

    private static BarcodeController ourInstance = new BarcodeController();

    public static BarcodeController getInstance() {
        return ourInstance;
    }

    private BarcodeController() {
    }


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
        Log.d(TAG," QQQQQQQQ Barcode init..");

        // web js传入的div区域测试
        int divHeight = 640;

        // 二维码扫描的布局文件:xml
        LayoutInflater inflater = LayoutInflater.from(activity);
        FrameLayout layout = (FrameLayout)inflater.inflate(R.layout.barcode_capture_2,null);

        // SurfaceView(硬件摄像头的预览)
        SurfaceView surfaceView = (SurfaceView) layout.findViewById(R.id.preview_view_2);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        // viewfinderView
        viewfinderView = (ViewfinderView) layout.findViewById(R.id.viewfinder_view_2);
        cameraManager = new CameraManager(activity.getApplication());
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        // 主Activity的布局
        FrameLayout actFrameLayout = (FrameLayout) activity.findViewById(R.id.idActivityHybrid);

        // 用mainView装载SurfaceView和ViewfinderView整体
        mainView = new FrameLayout(activity.getApplicationContext());
        mainView.setClipChildren(false);
//        mainView.setClipBounds();
        FrameLayout.LayoutParams bcLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, divHeight);
//        FrameLayout.LayoutParams bcLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bcLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        bcLayoutParams.leftMargin = 0;//0;
        bcLayoutParams.topMargin = 0;//200;
        actFrameLayout.addView(mainView,bcLayoutParams);  // add mainView into activity
        mainView.setBackgroundColor(Color.BLUE);

        if(surfaceView!= null && surfaceView.getParent() != null) {
            Log.d(TAG,"surfaceView has parent");
            layout.removeView(surfaceView);
        }

        if(viewfinderView != null && viewfinderView.getParent() != null) {
            Log.d(TAG,"viewfinderView has parent");
//            surfaceView.getParent()
            layout.removeView(viewfinderView);
        }

        // 这样摄像头就不会被拉伸,长宽的比率满足支持的比率(720*1280),但是宽度小了,surfaceView在mainView中就只有一小部分,不是满mainView的
        // 解决办法:surfaceView还是720,但是高度定位1280,因为是加在mainView中的,所以surfaceView的垂直方向看到一部分,但是我们只关注的是二维码扫描框框的那一部分就行了
//        mainView.addView(surfaceView,new FrameLayout.LayoutParams((720 *divHeight)/1280, divHeight)); // 不拉伸
//        float r = (1280 * divHeight)/720;
//        mainView.addView(surfaceView,new FrameLayout.LayoutParams(720,1280));
        mainView.addView(surfaceView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainView.addView(viewfinderView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mainView.addView(viewfinderView);

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

        isLoaded = true;
    }

    /* 全屏没有问题,设置了区域大小（非全屏)就会出现拉伸的问题
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
//                600
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        bcLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        bcLayoutParams.leftMargin = 0;
        bcLayoutParams.topMargin = 0;//200;
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
    }*/

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
        Log.d(TAG,"format:" + format + " width:" + width + " height:" + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"# # # # # # surfaceDestroyed ");
    }


    // - - - - - - Life circle - - - - - -
    public void stop(){
//        onDestroy();
//        inactivityTimer.shutdown();

        cameraManager.stopPreview();
        hasSurface = false;
        decodeFormats = null;
        characterSet = null;
        cameraManager.closeDriver();

        FrameLayout layout = (FrameLayout)mainView.getParent();
        layout.removeView(mainView);
        isLoaded = false;
    }

    protected void onDestroy() {
        Log.d(TAG,"### onDestroy");

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
