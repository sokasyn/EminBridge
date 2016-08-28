package com.emin.digit.test;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.pdf.PdfRenderer;
import android.media.Rating;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emin.digit.mobile.android.hybrid.EminBridge.R;

/**
 * Created by Samson on 16/8/26.
 */
public class TestDynamicLayout extends Activity {

    private static final String TAG = TestDynamicLayout.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenSize();

        // 通过xml布局(与动态布局的对比)
//        setupWithXmlLayout();

        setupWithDynamicDode();
    }

    // 手机屏幕的适配,获取手机屏幕的大小
    private void getScreenSize(){
        //获取屏幕高宽
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);

        // since SDK_INT = 1;
        int windowsWight = metric.widthPixels;
        int windowsHeight = metric.heightPixels;
        //  我的小米720 1280
        Log.d(TAG,"windowsWight :" + windowsWight + " windowsHeight:" +windowsHeight);

        int widthPixels,heightPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17){
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
                Log.d(TAG,"SKD_VERSION [14,17]" + "widthPixels :" + widthPixels + " heightPixels:" +heightPixels);
            } catch (Exception ignored) {
            }
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17){
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
                Log.d(TAG, "SKD_VERSION >=17" + " widthPixels :" + widthPixels + " heightPixels:" +heightPixels);
            } catch (Exception ignored) {
            }
        }
    }

    // 通过xml布局(与动态布局的对比)
    private void setupWithXmlLayout(){
        setContentView(R.layout.test_dynamic_layout);
        Button btnOne = (Button)findViewById(R.id.btnOne);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnOne clicked");
            }
        });
    }

    // 以下为纯代码方式创建Layout(布局)以及View(控件)
    // 主要测试 Layout与控件view的关系,以及如何配置view的在布局当中的位置信息
    // 关注点:
    // 1.控件加入到布局的情况下,LayoutParams要跟布局的类型匹配
    // 2.
    private LinearLayout linearLayout = null;

    // 通过代码动态布局(与静态xml文件布局对比)
    private void setupWithDynamicDode(){
        // 动态生成的一个线性布局
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.GREEN);
        linearLayout.setId(R.id.id_linearLayout);
        Log.d(TAG,"linearLayout while created" + linearLayout);

        // 动态生成一个TextView
        TextView textView = new TextView(this);
        textView.setText("纯代码动态布局测试");
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearLayout.addView(textView,tvParams);

        // 动态生成一个Button
        Button btnOne = new Button(this);
        btnOne.setText("Button one");
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button one clicked");
                btnOneClicked();
            }
        });

        // 定义该Button如何加入到布局当中
        LinearLayout.LayoutParams btnLayoutParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearLayout.addView(btnOne,btnLayoutParam);

        // 将布局加入到Activity的根布局当中(activity默认是有一个FrameLayout的)
        setContentView(linearLayout);
    }

    private void btnOneClicked(){

        /*
         * 测试结果:
         * A.linearLayout对象是通过setContentView加载显示出来的,
         *   但是实际上它并不是根视图,它的父视图是一个类型为FrameLayout的视图,是系统自带的,即它有Parent
         * B.当把一个view(具体的控件如,Button)加入到一个布局中当中时,需要配置该view在父视图的布局方式
         *   即需要构建LayoutParams,那么该LayoutParams的类型就要跟父视图的Layout类型一致
         *   该例子中,要将一个AbsoluteLayout加入到linearLayout中,那么该AbsoluteLayout在通过
         *   addView到linearLayout中时,LayoutParams的类型则为LinearLayout.LayoutParams;
         *   而将一个Button加入到AbsoluteLayout中,则该Button的LayoutParams的类型则为AbsoluteLayout.LayoutParams;
         */
        Log.d(TAG,"linearLayout:" + linearLayout);
        Log.d(TAG,"linearLayout.getParent():" + linearLayout.getParent());
        if(linearLayout.getParent() instanceof FrameLayout){
            Log.d(TAG,"linearLayout.getParent() is instanceof FrameLayout");
        }
        Log.d(TAG,"" + linearLayout.getParent().getClass());
        View androidContentView = this.findViewById(android.R.id.content); // 这个是最顶层的View了,一个FrameLayout,跟linearLayout的Parent是一致的
        Log.d(TAG,"content view" + androidContentView);

        // - - - - - - - - 1.创建AbsoluteLayout,并加入到 linearLayout 中,则需要 LinearLayout.LayoutParams
        AbsoluteLayout absoluteLayout = new AbsoluteLayout(this);
        absoluteLayout.setBackgroundColor(Color.BLUE);
        absoluteLayout.setId(R.id.id_absoluteLayout);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                500,
                300);
        linearLayout.addView(absoluteLayout,linearParams);


        // - - - - - - - - 2.创建Button2,并加入到 AbsoluteLayout 中,则需要 AbsoluteLayout.LayoutParams
        Button btnTwo = new Button(this);
        btnTwo.setText("Button two");
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button two clicked");
                btnTwoClicked();
            }
        });
        AbsoluteLayout.LayoutParams btnTwoParams = new AbsoluteLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,10,10);
        absoluteLayout.addView(btnTwo,btnTwoParams);

        // - - - - - - - - 3.创建Button3,并加入到 AbsoluteLayout 中
        Button btnThree = new Button(this);
        btnThree.setText("Button three");
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button three clicked");
                btnThreeClicked();
            }
        });
        // - - - - - - - - 3.1 Button3在绝对布局中设置x,y坐标,由于该绝对布局在后续的API Level中属于过期,替代方式看
        AbsoluteLayout.LayoutParams btnThreeParams = new AbsoluteLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,10,110);
        absoluteLayout.addView(btnThree,btnThreeParams);
    }

//    private
    // 测试在布局中加入一个控件,该控件不用Layout包装
    private void btnTwoClicked(){

        // 如果需要在ImageView中加入其它的view,应当在该ImageView套一个层Layout
        FrameLayout imgFrameLayout = new FrameLayout(this);
        Log.d(TAG,"ImageView FrameLayout created:" + imgFrameLayout);
        imgFrameLayout.setBackgroundColor(Color.WHITE);
        imgFrameLayout.setId(R.id.id_imageViewFrame);
        LinearLayout.LayoutParams imgFrameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                400
        );
        linearLayout.addView(imgFrameLayout,imgFrameParams);

        // ImageView
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.CYAN);
        imageView.setId(R.id.id_imageView);
        Log.d(TAG,"ImageView created:" + imageView);

        FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300
        );
        imgFrameLayout.addView(imageView, imgParams);
    }

    /**
     *  测试结果
     *  A.通过在特定布局中的children查找view和通过全局findViewById查找的的view是一致的
     *  B.虽然特定的控件如ImageView也是view,但试图在该控件内加入其它的控件不太合理,想要达到类似的效果,
     *    一个比较可行的方案是在该控件外套一层Layout,然后在在Layout上加入其它的控件,视觉上达到这两个view的关系
     *
     */
    private void btnThreeClicked(){
        FrameLayout imgFrameLayout = (FrameLayout) findViewById(R.id.id_imageViewFrame);
        Log.d(TAG,"ImgFrameLayout found:" + imgFrameLayout);

        // 测试对加载的view的获取/查找
        int childCount = imgFrameLayout.getChildCount();
        Log.d(TAG,"imageView's frame layout's child count:" + childCount);

        ImageView imageView = (ImageView)findViewById(R.id.id_imageView); // 通过全局查找方式获取View
        ImageView imageView1 = (ImageView) imgFrameLayout.getChildAt(0);   // 通过Layout的Child获取view
        Log.d(TAG,"imageView found:" + imageView);
        Log.d(TAG,"imageView1 from children:" + imageView1);

        // 测试对view的位置和大小等信息
        int imgFrameWidth = imgFrameLayout.getWidth();
        int imgFrameHeight = imgFrameLayout.getHeight();
        Log.d(TAG,"ImageView frame width:" + imgFrameWidth + " height:" + imgFrameHeight);

        int imgViewWidth = imageView.getWidth();
        int imgViewHeight = imageView.getHeight();
        Log.d(TAG,"ImageView width:" + imgViewWidth + " height:" + imgViewHeight);

        if(imgFrameLayout != null){
            Button btnFour = new Button(this);
            btnFour.setText("Button four");
            btnFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Button four clicked");
                    btnFourClicked();
                }
            });

            // 测试:通过FrameLayout的Params实现类似绝对布局的x,y坐标
            FrameLayout.LayoutParams btnFourFrameParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            btnFourFrameParams.gravity = Gravity.LEFT|Gravity.TOP;
            int point_x = 0;
            int point_y = imgViewHeight;
            btnFourFrameParams.leftMargin = point_x;
            btnFourFrameParams.topMargin  = point_y;
            imgFrameLayout.addView(btnFour,btnFourFrameParams);

        }
    }

    private void btnFourClicked(){

    }

}
