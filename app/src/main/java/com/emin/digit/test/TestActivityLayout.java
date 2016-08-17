package com.emin.digit.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.emin.digit.mobile.android.hybrid.EminBridge.R;
/**
 * Created by Samson on 16/8/12.
 *
 * Activity的根视图的获取方式有两种
 * 1.findViewById(R.id.layoutId);这种方式需要在布局文件中设置layout的id(如R.id.layoutId,是xml文件中配置的一个id)
 *   这种方式一旦布局被加载,则任何时候获取的都是被加载出来的对象
 * 2.通过LayoutInflater。这种方式需要布局文件的resource(如R.layout.main,是一个xml文件);
 *   这种方式不管之前有没有加载过该布局,得到的view都是新创建的,当然也就包括了其中的子view
 *
 */
public class TestActivityLayout extends Activity {
    private static final String TAG = TestActivityLayout.class.getSimpleName();


    private Button btnChangeColor;
    private Button btnSettingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupComponents();
    }

    // - - - - - - - - - -  视图的加载 - - - - - - - - - -
    private void setupLayout(){
        currentMehthodInvoke();
//        setupViewStatic();
        setupViewInflate();
    }

    // 静态xml文件加载
    private void setupViewStatic(){
        setContentView(R.layout.test_activity_second);
    }

    // 动态获取生成布局文件对应的view加载
    private void setupViewInflate(){
        currentMehthodInvoke();

        LinearLayout layout0 = (LinearLayout)findViewById(R.id.idLayoutSecond);
        Log.d(TAG,"setupViewDynamic findViewById layout0:" + layout0);  // null,view 没加载(还没有setContentView)
        TextView textView0 = (TextView)findViewById(R.id.textView);
        Log.d(TAG,"setupViewDynamic textView0:" + textView0);  // null,没加载

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.test_activity_second,null);
        Log.d(TAG,"setupViewDynamic inflated layout:" + layout); // not null,通过inflater的方式,找到布局文件,然后创建view对象(LinearLayout也是view）
        setContentView(layout);

        // 以下两种方式得到的textView是一样的
        TextView textView1 = (TextView)findViewById(R.id.textView);
        TextView textView2 = (TextView)layout.findViewById(R.id.textView);
        Log.d(TAG,"setupViewDynamic textView1:" + textView1);
        Log.d(TAG,"setupViewDynamic textView2:" + textView2);

    }

    // - - - - - - - - - - 控件加载 - - - - - - - - - -
    private void setupComponents(){
        setupButtons();
    }

    // 按钮控件
    private void setupButtons(){
        btnChangeColor = (Button)findViewById(R.id.btnChangeColor);
        btnChangeColor.setOnClickListener(listener);

        btnSettingText = (Button)findViewById(R.id.btnSettingText);
        btnSettingText.setOnClickListener(listener);
    }

    // 按钮点击事件
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnChangeColor:{
                    btnChangeColorClicked();
                    break;
                }
                case R.id.btnSettingText:{
                    btnSettingTextClicked();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

    // Buttons click events
    // 更改根视图的背景颜色测试
    private void btnChangeColorClicked(){
        currentMehthodInvoke();
        settingWidget();
    }

    // 静态动态更改控件的属性测试
    private void btnSettingTextClicked(){
        settingWidgetDynamic();
    }


    private void settingWidget(){
        currentMehthodInvoke();
        TextView textView = (TextView)findViewById(R.id.textView);
        System.out.println("text:" + textView.getText());
        textView.setText("settingWidget " + System.currentTimeMillis());
    }

    private void settingWidgetDynamic(){
        currentMehthodInvoke();

        LinearLayout layout1 = (LinearLayout)findViewById(R.id.idLayoutSecond); // 与初始创建的layout是一样的
        Log.d(TAG,"settingWidgetDynamic findViewById layout1:" + layout1);
        layout1.setBackgroundColor(Color.GREEN);

        LinearLayout layoutWithCode = new LinearLayout(this);
        Log.d(TAG,"settingWidgetDynamic findViewById layoutWithCode:" + layoutWithCode);

        // 获取布局的根视图
        LayoutInflater inflater = LayoutInflater.from(this);
        // 通过inflater的方式,找到布局文件,然后创建view对象,所以跟初始创建的是不一样的。
        // 当界面控件被更改时,所新创建的这个对象是不受影响的,跟布局里设置的一致,如TextView的text等
//        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.acivity_second,null);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.test_activity_second,null);
        Log.d(TAG,"settingWidgetDynamic inflated layout:" + layout);


        // 如果调用了settingWidget方法,改变了textView的Text,则这里textView的text值也变了
        TextView textView1 = (TextView)findViewById(R.id.textView);

        // 如果调用了settingWidget方法,改变了textView的Text,而这里textView的text值还是布局时候的text
        TextView textView2 = (TextView)layout.findViewById(R.id.textView);
//        System.out.println("text:" + textView2.getText());

        Log.d(TAG,"settingWidget textView1:" + textView1);
        Log.d(TAG,"settingWidget textView2:" + textView2);

        if(viewHasParent(textView2)){
            System.out.println("Need to removeView from texView2's parent");
            layout.removeView(textView2);
        }
        Log.d(TAG,"settingWidget textView2:" + textView2);

        viewHasParent(textView2);

        // 动态添加从布局文件里得到的TextView对象到当前的 layout1当中
        layout1.addView(textView2);
        System.out.println("" + layout1.getChildCount());

//        TextView textView3 = (TextView)findViewById(R.id.textView);
//        System.out.println("text:" + textView3.getText());
//        textView3.setText("settingWidget " + System.currentTimeMillis());
    }

    private boolean viewHasParent(View view){
        if(view.getParent() != null){
            System.out.println("got parent :" + view.getParent());
            return true;
        }
        System.out.println("Got no parent");
        return false;
    }



    private boolean isDebug = false;
    private boolean isInfo = true;
    private void currentMehthodInvoke(){

        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if(isDebug){
            for(int i = 0 ; i < elements.length; i++){
                System.out.println("elements[" + i + "] MethodName: " + elements[i].getMethodName());
                System.out.println("elements[" + i + "] ClassName: " + elements[i].getClassName());
                System.out.println("elements[" + i + "] LineNumber: " + elements[i].getLineNumber());
                System.out.println("elements[" + i + "] FileName: " + elements[i].getFileName());
            }
        }
        if(isInfo){
            String methodInvoked = Thread.currentThread().getStackTrace()[3].getMethodName();
            Log.i(TAG,methodInvoked + " invoked.");
        }
    }
}
