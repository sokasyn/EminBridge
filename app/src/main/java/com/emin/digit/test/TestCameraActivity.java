package com.emin.digit.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.emin.digit.mobile.android.hybrid.EminBridge.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by Samson on 16/9/13.
 */
public class TestCameraActivity extends Activity implements SurfaceHolder.Callback{

    private static final String TAG = TestCameraActivity.class.getSimpleName();

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_camera);
        surfaceView = (SurfaceView)findViewById(R.id.camera_preview_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // SurfaceHolder Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surfaceCreated width:" + width + " height:" + height); // 720 1280
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (camera != null) {
            camera.stopPreview();
        }
        camera.release();
        camera = null;
    }

    private void initCamera(){
        camera = camera.open();

        showCameraDefaultParams(camera);

        camera.setDisplayOrientation(90);  // SDK2.2+
        Camera.Parameters parameters = camera.getParameters();

        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int defaultWidth = display.getWidth();
        int defaultHeight = display.getHeight();
        Log.d(TAG,"display default width:" + defaultWidth + " default height:" + defaultHeight); // 720 1280

        parameters.setPreviewSize(defaultHeight,defaultWidth);
//        parameters.setPreviewSize(defaultWidth,defaultHeight);
        //parameters.setPreviewSize(480, 320); // 设置
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        camera.startPreview();
    }

    // 查看Camera的Parameters
    private void showCameraDefaultParams(Camera camera){
        Log.d(TAG,"showing CameraDefaultParams");

        Camera.Parameters parameters = camera.getParameters();
        int CurPreFmt = parameters.getPreviewFormat();

        Log.d(TAG, "Enter SurfaceChanged Function ");
        Log.d(TAG, "PreviewFormat =" + CurPreFmt);
        switch(CurPreFmt) {
            case ImageFormat.JPEG:
                Log.d(TAG, "Current Preview Format is JPEG");
                break;
            case ImageFormat.NV16:
                Log.d(TAG, "Current Preview Format is NV16");
                break;
            case ImageFormat.NV21:
                Log.d(TAG, "Current Preview Format is NV21");
                break;
            case ImageFormat.RGB_565:
                Log.d(TAG, "Current Preview Format is RGB_565");
                break;
            case ImageFormat.YUY2:
                Log.d(TAG, "Current Preview Format is YUY2");
                break;
            case ImageFormat.YV12:
                Log.d(TAG, "Current Preview Format is YV12");
                break;
            case ImageFormat.UNKNOWN:
                Log.d(TAG, "Current Preview Format is UNKNOWN");
                break;
            default:
                Log.d(TAG, "Current Preview Format is default : UNKNOWN");
                break;
        }

        Size previewSize = parameters.getPreviewSize();
        Log.d(TAG, "= = = = = = = current Preview Size :" + previewSize.width + " x " + previewSize.height);

        Size curPicSize = parameters.getPictureSize();
        Log.d(TAG, "current Picture Size :" + curPicSize.width + " x " + curPicSize.height);

        int CurPicFmt = parameters.getPictureFormat();
        switch(CurPicFmt) {
            case ImageFormat.JPEG:
                Log.d(TAG, "Picture Format is JPEG");
                break;
            case ImageFormat.NV16:
                Log.d(TAG, "Picture Format is NV16");
                break;
            case ImageFormat.NV21:
                Log.d(TAG, "Picture Format is NV21");
                break;
            case ImageFormat.RGB_565:
                Log.d(TAG, "Picture Format is RGB_565");
                break;
            case ImageFormat.YUY2:
                Log.d(TAG, "Picture Format is YUY2");
                break;
            case ImageFormat.YV12:
                Log.d(TAG, "Picture Format is YV12");
                break;
            case ImageFormat.UNKNOWN:
                Log.d(TAG, "Picture Format is UNKNOWN");
                break;
            default:
                Log.d(TAG, "Picture Format is default : UNKNOWN");
                break;

        }

        int CurPreRange[];
        CurPreRange = new int[2];
        parameters.getPreviewFpsRange(CurPreRange);
        Log.d(TAG, "Current Preview MinFps = " + CurPreRange[0] + "  MaxFps = " + CurPreRange[1]);
        Log.d(TAG, "Current EV setting is " + parameters.getExposureCompensation() * parameters.getExposureCompensationStep());
        Log.d(TAG, "Jpeg Quality = " + parameters.getJpegQuality());
        Log.d(TAG, " Min Exposure Comp  = " + parameters.getMinExposureCompensation());
        Log.d(TAG, " Max Exposure Com = " + parameters.getMaxExposureCompensation());

        String strCurFocusMode = parameters.getFocusMode();
        if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_AUTO)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_AUTO");
        }else if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_CONTINUOUS_PICTURE");
        }else if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_CONTINUOUS_VIDEO");
        }else if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_INFINITY");
        }else if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_MACRO)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_MACRO");
        }else if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_FIXED)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_FIXED");
        }else if (strCurFocusMode.equalsIgnoreCase(Camera.Parameters.FOCUS_MODE_EDOF)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_EDOF");
        }

        List<int[]> SupPreFpsRangeList = parameters.getSupportedPreviewFpsRange();
        int SupPreRange[] = new int[2];

        Log.d(TAG, "Support Preview FPS Range List :  " );
        for(int num = 0; num < SupPreFpsRangeList.size(); num++) {
            SupPreRange = SupPreFpsRangeList.get(num);
            Log.d(TAG, "< " + num + " >" + " Min = " + SupPreRange[0] + "  Max = " + SupPreRange[1]);
        }

        if(parameters.isAutoExposureLockSupported() ) {
            if(parameters.getAutoExposureLock()) {
                Log.d(TAG, "Auto Exposure is Locked");
            } else {
                Log.d(TAG, "Auto Exposure is not Locked");
            }
        }

        List<String> focusModesList = parameters.getSupportedFocusModes();
        if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            Log.d(TAG, "Camera Support AUTO FOCUS MODE");

        }else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_CONTINUOUS_PICTURE");
        }else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_CONTINUOUS_VIDEO");
        }else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_INFINITY");
        }else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_MACRO)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_MACRO");
        }else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_FIXED");
        }else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_EDOF)) {
            Log.d(TAG, "Current Focus Mode is FOCUS_MODE_EDOF");
        }

        List<Integer> supPicFmtList = parameters.getSupportedPictureFormats();
        Log.d(TAG, "Supported Picutre Format :" );

        for(int num = 0; num < supPicFmtList.size(); num++) {
            Integer ISupPicFmt = supPicFmtList.get(num);
            Log.d(TAG, "" + ISupPicFmt.intValue());
        }

        List<Integer> supPreFmtList = parameters.getSupportedPreviewFormats();
        Log.d(TAG, "Supported Preview Format :");

        for(int num = 0; num < supPreFmtList.size(); num++) {
            Integer ISupPreFmt = supPreFmtList.get(num);
            Log.d(TAG, "" + ISupPreFmt.intValue());
        }

        List<Size> PicSupSizeList = parameters.getSupportedPictureSizes();
        Log.d(TAG, "Camera Supported Pic Size are :");
        for(int num = 0; num < PicSupSizeList.size(); num++) {
            Size PicSupSize = PicSupSizeList.get(num);
            Log.d(TAG, "< " + num +" >" + PicSupSize.width + " x " + PicSupSize.height);
        }

        Log.d(TAG, "Camera Supported Preview Size are :");
        List<Size> supPreviewList = parameters.getSupportedPreviewSizes();
        for(int num = 0; num < supPreviewList.size(); num++) {
            Size PicPreSupSize = supPreviewList.get(num);
            Log.d(TAG, "< " + num +" >" + PicPreSupSize.width + " x " + PicPreSupSize.height);
        }
    }

}
