package com.example.test3;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
/**
 * 箱子形状页面
 *
 *
 */
public class MainActivity4 extends Activity implements Device {
    //Handler
    MyHandler_xingzhuang myHandler_xingzhuang;
    //Thread
    RefreshThread_xingzhuang refreshThread_xingzhuang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main4);
        /*设置ContentView为自定义的MyVieW*/
        MyView myView = new MyView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivity.setStatusBarColor(this,0xFF008577);
        }
        setContentView(myView);
        //将myView传递给handler
        myHandler_xingzhuang=new MyHandler_xingzhuang(myView);
        //建立并启动线程
        refreshThread_xingzhuang=new RefreshThread_xingzhuang(myHandler_xingzhuang);
        refreshThread_xingzhuang.start();
    }
}
