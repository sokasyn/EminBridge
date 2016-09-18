package com.emin.digit.test;

import android.app.Activity;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.InterruptedIOException;
import java.security.PublicKey;

/**
 * Created by Samson on 16/8/26.
 */
public class TestSurfaceActivity extends Activity {

    private static final String TAG = TestSurfaceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new MyView(this));
//        setContentView(new DrawView(this));

        View view = new DrawViewSurface(this);
//        view.setBackgroundColor(Color.GREEN);
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    // 测试1 普通的 view 的绘画 (不用surface)
    class DrawView extends View  {

        private final String DRAW_TAG = DrawView.class.getSimpleName();

        Paint paint;
        float radius = 10;

        public DrawView(Context context){
            super(context);
            Log.d(DRAW_TAG,"DrawView Constructor");
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Log.d(DRAW_TAG,"= = onDraw");
            canvas.drawCircle(100, 100, radius++, paint);
            if(radius > 100){
                radius = 10;
            }
            invalidate(); // 刷新View

        }
    }

    // 测试2 DrawView 的改进(用surface,继承surfaceView)
    class DrawViewSurface extends SurfaceView implements SurfaceHolder.Callback{

        private final String DRAW_SURFACE = DrawViewSurface.class.getSimpleName();

        DrawThread drawThread;

        public DrawViewSurface(Context context){
            super(context);
            init();
        }

        private void init(){
            Log.d(DRAW_SURFACE,"init");
            SurfaceHolder surfaceHolder = this.getHolder();
            surfaceHolder.addCallback(this);

            // 启动绘制线程开始绘画
            drawThread = new DrawThread(surfaceHolder);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(DRAW_SURFACE,"surfaceCreated");
            drawThread.stop = false;
            drawThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(DRAW_SURFACE,"surfaceChanged");
            Log.d(DRAW_SURFACE,"format:" + format + " width:" + width + " height:" + height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(DRAW_SURFACE,"surfaceDestroyed");
            drawThread.stop = true;
        }

        // 在surfaceView 上做的绘画线程
        class DrawThread extends Thread{

            boolean stop;
            SurfaceHolder surHolder;
            Paint paint;
            float radius = 10;

            public DrawThread(SurfaceHolder holder){
                Log.d(DRAW_SURFACE,"DrawThread created");
                surHolder = holder;
                paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
            }

            @Override
            public void run() {
                Log.d(DRAW_SURFACE,"== run");

                while (!stop){

                    Canvas canvas = null;
                    try {
                        synchronized (surHolder){
                            canvas = surHolder.lockCanvas();
                            doDraw(canvas);
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if(canvas != null){
                            surHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }

            public void doDraw(Canvas canvas){
                Log.d(DRAW_SURFACE,"doDraw");
                canvas.drawColor(Color.BLACK);
                canvas.translate(200, 200);
                canvas.drawCircle(100, 100, radius++, paint);
                if(radius > 100){
                    radius = 10;
                }
            }

        }
    }

    // 测试3 通过surfaceView可以做其它复杂的绘画组合
    // 只要继承SurfaceView类并实现SurfaceHolder.Callback接口就可以实现一个自定义的SurfaceView了
    //视图内部类
    class MyView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder holder;
        private MyThread myThread;
        public MyView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            holder = this.getHolder();
            holder.addCallback(this);
            myThread = new MyThread(holder);//创建一个绘图线程
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
            Log.d(TAG,"surfaceChanged width:" + width + " height:" + height);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            Log.d(TAG,"surfaceCreated");
            myThread.isRun = true;
            myThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            Log.d(TAG,"surfaceDestroyed");
            myThread.isRun = false;
        }

    }

    //线程内部类
    class MyThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun ;
        public MyThread(SurfaceHolder holder) {
            this.holder =holder;
            isRun = true;
        }
        @Override
        public void run() {
            int count = 0;
            while(isRun) {
                Canvas c = null;
                try {
                    synchronized (holder) {
                        c = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
//                        c.drawColor(Color.BLACK);//设置画布背景颜色
                        c.drawColor(Color.BLUE);
                        Paint p = new Paint(); //创建画笔
                        p.setColor(Color.WHITE);
                        Rect r = new Rect(100, 50, 600, 250);
                        c.drawRect(r, p);
                        c.drawText("这是第"+(count++)+"秒", 100, 310, p);
                        Thread.sleep(1000);//睡眠时间为1秒
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally {
                    if(c!= null) {
                        holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。

                    }
                }
            }
        }
    }
}


