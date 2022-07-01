package com.example.test3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 主页面
 *
 *
 */
public class MainActivity extends AppCompatActivity implements Device{
    //蓝牙按钮与蓝牙监听
    private Button openBle = null;
    private Button closeBle = null;
    private BluetoothMonitorReceiver bleListenerReceiver = null;
    //api
    API api=new API();
    //控制按钮与状态使能
    private Button bu_check;//温光湿检测
    //温度、光强、湿度使能(使用Integer 同步不了)
    private RBean temperature_en =new RBean();
    private RBean guang_en =new RBean();
    private RBean shidu_en =new RBean();
    //显示文本
    private TextView t1;//显示温度文本
    private TextView t2;//显示光强文本
    private TextView t3;//显示湿度文本
    private TextView t4;//显示在线文本
    private TextView t5;//显示开闭文本
    //handler用于线程通信
    MyHandler handler;
    //刷新数据子线程，由使能控制
    RefreshThread ref;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            //解决闪退
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
            setStatusBarColor(this,0xFF874BDF);//通知栏颜色
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //蓝牙部分
            // 初始化广播
            this.bleListenerReceiver = new BluetoothMonitorReceiver(this);
            IntentFilter intentFilter = new IntentFilter();
            // 监视蓝牙关闭和打开的状态
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            // 监视蓝牙设备与APP连接的状态
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            // 注册广播
            registerReceiver(this.bleListenerReceiver, intentFilter);
            // 初始化控件
            this.closeBle = findViewById(R.id.close_ble);
            this.openBle = findViewById(R.id.open_ble);
            // 关闭蓝牙
            this.closeBle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*
                // 跳转到蓝牙设置页面，关闭蓝牙，没有发现弹出对话框关闭蓝牙的
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(intent,RESULT_OK);
                */
                    // 隐式关闭蓝牙
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    bluetoothAdapter.disable();
                }
            });
            // 打开蓝牙
            this.openBle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*
                // 弹出对话框，打开蓝牙
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,RESULT_OK);
                */

                    // 隐式打开蓝牙
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    bluetoothAdapter.enable();

                }
            });

            //温度、光强、湿度与控件连接
            t1 = (TextView) findViewById(R.id.temperature);
            t2 = (TextView) findViewById(R.id.guang);
            t3 = (TextView) findViewById(R.id.shidu);
            t4 = (TextView) findViewById(R.id.text_online);
            t5 = (TextView) findViewById(R.id.text_open);
            bu_check = (Button)findViewById(R.id.but_check);
            Konzhi_Lis2 kzl = new Konzhi_Lis2();
            bu_check.setOnClickListener(kzl);
            //建立handler
            handler = new MyHandler(this, t1, t2, t3, t4, t5, temperature_en, guang_en, shidu_en, api);
            //建立监听器
            //用于更改阈值（温度和湿度最大最小值）
            EditText edt_tem_max = (EditText) findViewById(R.id.edt_tem_max);
            EditText edt_tem_min = (EditText) findViewById(R.id.edt_tem_min);
            EditText edt_shidu_max = (EditText) findViewById(R.id.edt_shidu_max);
            EditText edt_shidu_min = (EditText) findViewById(R.id.edt_shidu_min);
            Button bu_tem_max = (Button) findViewById(R.id.bu_tem_max);
            Button bu_tem_min = (Button) findViewById(R.id.bu_tem_min);
            Button bu_shidu_max = (Button) findViewById(R.id.bu_shidu_max);
            Button bu_shidu_min = (Button) findViewById(R.id.bu_shidu_min);
            //阈值监听器
            Threshold_Lis th_lis_tem_max = new Threshold_Lis(edt_tem_max, bu_tem_max);
            Threshold_Lis th_lis_tem_min = new Threshold_Lis(edt_tem_min, bu_tem_min);
            Threshold_Lis th_lis_shidu_max = new Threshold_Lis(edt_shidu_max, bu_shidu_max);
            Threshold_Lis th_lis_shidu_min = new Threshold_Lis(edt_shidu_min, bu_shidu_min);
            bu_tem_max.setOnClickListener(th_lis_tem_max);
            bu_tem_min.setOnClickListener(th_lis_tem_min);
            bu_shidu_max.setOnClickListener(th_lis_shidu_max);
            bu_shidu_min.setOnClickListener(th_lis_shidu_min);
            //建立子线程并启动
            ref = new RefreshThread(handler, temperature_en, guang_en, shidu_en);
            ref.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //监听器类
    public class Kongzhi_Lis implements View.OnClickListener {
        private RBean  flag;
        public Kongzhi_Lis(RBean flag){
            this.flag=flag;
        }
        //触发按钮改变使能
        public void onClick(View v){
            if(flag.en==1)
                flag.en=0;
            else
                flag.en=1;
            System.out.println("使能为"+flag.en);
        }
    }
    //监听器类2
    public class Konzhi_Lis2 implements  View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (temperature_en.en==1) temperature_en.en=0;
            else temperature_en.en=1;
            if (guang_en.en==1) guang_en.en=0;
            else guang_en.en=1;
            if (shidu_en.en==1) shidu_en.en=0;
            else shidu_en.en=1;
        }
    }
    //阈值监听器
    public class Threshold_Lis implements View.OnClickListener {
        String str;
        EditText edt;
        Button bu;
        int num;
        Threshold_Lis(EditText edt,Button bu) {
            this.edt = edt;
            this.bu = bu;
            //转成整数
            String temp_str = edt.getText().toString();
            num = Integer.parseInt(temp_str);
        }
        @Override
        public void onClick(View v) {
            //转成整数
            String temp_str = edt.getText().toString();
            num = Integer.parseInt(temp_str);
            //判断阈值类型
            str = bu.getText().toString();
            if (str.equals("更改最高温度")) {
                handler.tem_max = num;
                handler.dialog_done[0] = false;
            }
            else if (str.equals("更改最低温度")) {
                handler.tem_min = num;
                handler.dialog_done[0] = false;
            }
            else if (str.equals("更改最高湿度")) {
                handler.shidu_max = num;
                handler.dialog_done[2] = false;
            }
            else if (str.equals("更改最低湿度")) {
                handler.shidu_min = num;
                handler.dialog_done[2] = false;
            }
        }
    }
    //跳转
    public void startActivity2(View view){
        startActivity(new Intent(this,MainActivity2.class));
    }
    public void startActivity3(View view){
        startActivity(new Intent(this,MainActivity3.class) );
    }
    public void startActivity4(View view){
        startActivity(new Intent(this,MainActivity4.class));
    }
    public void startActivity_camera(View view) {
        startActivity(new Intent(this,MainActivity_camera.class));
    }

    //通知栏颜色
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.bleListenerReceiver);
    }
}
