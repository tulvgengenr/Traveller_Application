package com.example.test3;

import android.app.Activity;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity_camera extends Activity implements Device {

    //handler
    MyHandler_camera myHandler_camera;
    //线程
    RefreshThread_camera refreshThread_camera;
    //页面
    MyView_camera myView_camera;
    //开箱id
    RBean box_id=new RBean();
    //总的开箱id
    RBean box_total = new RBean();
    //菜单
    PopupMenu popup = null;
    //api
    API_EDP api_edp = new API_EDP();
    //ImageView
    ImageView imageView;
    //TextView
    TextView text_number;
    TextView text_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //解决闪退
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivity.setStatusBarColor(this,0xFF1E212A);
        }
        //初始化box_id与box_total
        box_id.value = api_edp.getBox_id(deviceId_Camera,apiKey_Camera);    //第几次开箱
        box_id.en = 1;  //第一帧开始
        box_id.sum = api_edp.getBox_sum(deviceId_Camera,apiKey_Camera,box_id.value);    //总帧数
        box_total.value = box_id.value; //总共开箱次数
        //将activity_main_camera作为界面
        setContentView(R.layout.activity_main_camera);
        //测试
        ImageView imageView = (ImageView) findViewById(R.id.test_image);
        //开箱编号显示
        text_number = (TextView) findViewById(R.id.text_number);
        String tempstr="开箱编号: <font color='#FF0000'>"+box_id.value+"</font>";
        text_number.setText(Html.fromHtml(tempstr));
        //开箱时间显示
        text_time = (TextView) findViewById(R.id.text_time);
        System.out.println("box_id"+box_id.value);
        box_id.time = api_edp.getBox_time(deviceId_Camera,apiKey_Camera,box_id.value);
        tempstr = "开箱时间: <font><small>"+box_id.time+"</small></font>";
        text_time.setText(Html.fromHtml(tempstr));
        //初始化myView
        myView_camera = (MyView_camera) findViewById(R.id.myView_camera);
        myView_camera.setBox_id(box_id);
        myView_camera.setImageView(imageView);
        //将myView传递给handler
        myHandler_camera = new MyHandler_camera(myView_camera,box_id);
        //建立并启动线程
        refreshThread_camera = new RefreshThread_camera(myHandler_camera,box_id,box_total,api_edp);
        refreshThread_camera.start();
    }

    //弹出菜单
    public void showPopup(View v) {
        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.mymenu, popup.getMenu());
        //加菜单监听器
        MenuListener ml = new MenuListener();
        popup.setOnMenuItemClickListener(ml);
        //加组件
        if (box_total.value <= 10) {    //开箱总次数小于10
            Menu menu1 = popup.getMenu();
            for(int i=1;i<=box_total.value;i++) {
                String tempstr = "编号" + i;
                menu1.add(Menu.NONE, Menu.FIRST + i, 1, tempstr);
            }
        }
        else {                          //开箱总次数大于10
            Menu menu1 = popup.getMenu();
            for(int i=box_total.value-9;i<=box_total.value;i++) {
                String tempstr = "编号" + i;
                menu1.add(Menu.NONE, Menu.FIRST+i-box_total.value+10, 1, tempstr);
            }
        }
        //显示
        popup.show();
    }

    //菜单监听器
    public class MenuListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            box_id.en = 1;
            box_id.state = 0;
            switch (item.getItemId()) {
                case Menu.FIRST + 1:
                    box_id.value= box_total.value<=10? 1 : box_total.value-9 ;
                    break;
                case Menu.FIRST + 2:
                    box_id.value= box_total.value<=10? 2 : box_total.value-8 ;
                    break;
                case Menu.FIRST + 3:
                    box_id.value= box_total.value<=10? 3 : box_total.value-7 ;
                    break;
                case Menu.FIRST + 4:
                    box_id.value= box_total.value<=10? 4 : box_total.value-6 ;
                    break;
                case Menu.FIRST + 5:
                    box_id.value= box_total.value<=10? 5 : box_total.value-5 ;
                    break;
                case Menu.FIRST + 6:
                    box_id.value= box_total.value<=10? 6 : box_total.value-4 ;
                    break;
                case Menu.FIRST + 7:
                    box_id.value= box_total.value<=10? 7 : box_total.value-3 ;
                    break;
                case Menu.FIRST + 8:
                    box_id.value= box_total.value<=10? 8 : box_total.value-2 ;
                    break;
                case Menu.FIRST + 9:
                    box_id.value= box_total.value<=10? 9 : box_total.value-1 ;
                    break;
                case Menu.FIRST + 10:
                    box_id.value= box_total.value<=10? 10 : box_total.value ;
                    break;
                default:
                    break;
            }
            //更新总帧数
            box_id.sum = api_edp.getBox_sum(deviceId_Camera,apiKey_Camera,box_id.value);
            //更新开箱编号文字
            String tempstr = "开箱编号: <font color='#FF0000'><small>"+box_id.value+"</small></font>";
            text_number.setText(Html.fromHtml(tempstr));
            //更新开箱时间文字
            box_id.time = api_edp.getBox_time(deviceId_Camera,apiKey_Camera,box_id.value);
            tempstr = "开箱时间: <font><small>"+box_id.time+"</small></font>";
            text_time.setText(Html.fromHtml(tempstr));
            return false;
        }
    }
}