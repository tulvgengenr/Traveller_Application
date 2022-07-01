package com.example.test3;


import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.w3c.dom.Text;
/**
 * 温度、光强、湿度刷新Handler
 *
 *
 */
//handler类用于线程的通信
public class MyHandler extends Handler implements Device{
    //主界面
    MainActivity ma;
    //使能
    private RBean  temperature_en;//温度检测使能
    private RBean  guang_en;//光强检测使能
    private RBean  shidu_en;//湿度检测使能
    //是否在线
    boolean online = false;
    //箱子是否打开
    int is_open = 0;
    //不稳定因素
    boolean unstable_open = false;
    boolean unstable_online = false;
    boolean unstable_location = false;
    long utime_online_1 = 0, utime_online_2 = 0;
    long utime_open_1 = 0, utime_open_2 = 0;
    long utime_location_1 = 0, utime_location_2 = 0;
    //温湿度阈值
    int tem_max = 30;
    int tem_min = 20;
    int shidu_max = 80;
    int shidu_min = 20;
    //是否弹窗过（true为已弹窗过，false为未弹窗）
    boolean[] dialog_done;
    //温度、光强、湿度、在线、开闭
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    //api
    API api;
    //x,y,z以及flag表示摆放状态可取的值为：0,1,2,3,4。摆放位置发生变化或者角度变化超过50度都会提醒用户去看
    int acce_x;
    int acce_y;
    int acce_z;
    int new_flag = 0;
    int old_flag = 0;
    float new_degree = 0;
    float old_degree = 0;
    //构造函数，传递TextView
    public MyHandler(MainActivity ma,TextView t1,TextView t2,TextView t3,TextView t4, TextView t5,RBean temperature_en,RBean  guang_en, RBean shidu_en,API api){
        this.ma = ma;
        this.t1=t1;
        this.t2=t2;
        this.t3=t3;
        this.t4=t4;
        this.t4.setText(Html.fromHtml("设备: <font color='#FF0000'><small>异常</small></font>"));
        this.t5=t5;
        this.t5.setText(Html.fromHtml("箱子: <font color='#FF0000'><small>关闭</small></font>"));
        this.temperature_en=temperature_en;
        this.guang_en=guang_en;
        this.shidu_en=shidu_en;
        this.api=api;
        dialog_done = new boolean[]{false, false, false};

        this.api.refresh_acce(deviceId,apiKey);
        acce_x = -api.acce_x;
        acce_y = api.acce_y;
        acce_z = api.acce_z;
        new_degree = (float)(Math.atan2((double)acce_x, (double)acce_z) * 180 / Math.PI);
        if(acce_y>700 && acce_y<1200) new_flag = 1;
        else if(acce_y>300 && acce_y<=700) new_flag = 2;
        else if(acce_y>-1200 && acce_y<-800) new_flag = 3;
        else if(acce_y>=-800 && acce_y<-400) new_flag = 4;
        else new_flag = 0;
        old_flag = new_flag;
        old_degree = new_degree;
    }
    //    int i=0;
//    int j=0;
//    int k=0;
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        //更新数据，刷新UI
        switch(msg.what){
            case 0:
                String str = api.getDevice(deviceId,apiKey);
                boolean temponline = judgeOnline(str);
                //考虑不稳定情况，记录不稳定开始时间
                if (temponline != online && !unstable_online) {
                    utime_online_1 = System.currentTimeMillis();
                    unstable_online = true;
                }
                //若不稳定，记录当前时间
                if (unstable_online) utime_online_2 = System.currentTimeMillis();
                //若由不稳定转向稳定
                if (unstable_online && temponline == online) unstable_online = false;
                //确定改变状态
                if (temponline != online  && unstable_online && utime_online_2-utime_online_1 >= 1000) {
                    onlineDialog(temponline);
                    onlineNotification(temponline);
                    String tempstr1="设备: <font color='#FF0000'><small>正常</small></font>";
                    String tempstr2="设备: <font color='#FF0000'><small>异常</small></font>";
                    if (temponline == true) t4.setText(Html.fromHtml(tempstr1));
                    else t4.setText(Html.fromHtml(tempstr2));
                    online = temponline;
                    unstable_online = false;
                }

                //如果箱子形状发生改变要引导用户去查看箱子摆放状态，每3秒检查一次
                api.refresh_acce(deviceId,apiKey);
                acce_x = -api.acce_x;
                acce_y = api.acce_y;
                acce_z = api.acce_z;
                new_degree = (float)(Math.atan2((double)acce_x, (double)acce_z) * 180 / Math.PI);
                if(acce_y>700 && acce_y<1200) new_flag = 1;
                else if(acce_y>300 && acce_y<=700) new_flag = 2;
                else if(acce_y>-1200 && acce_y<-800) new_flag = 3;
                else if(acce_y>=-800 && acce_y<-400) new_flag = 4;
                else new_flag = 0;
                //不稳定的时候更新时间，直到与上一次差3000ms
                if(unstable_location) {
                    utime_location_2 = System.currentTimeMillis();
                    if(utime_location_2 - utime_location_1 >=3000){
                        unstable_location = false;
                    }
                }
                //稳定的时候与old比较
                else
                {
                    boolean same = true;
                    if(new_flag != old_flag) same = false;
//                    else{
//                        if(Math.abs(new_degree - old_degree)>=40)
//                            same = false;
//                    }
                    if(!same){
                        AlertDialog dialog = new AlertDialog.Builder(ma)
                                .setTitle("箱子状态")//标题
                                .setMessage("箱子状态发生改变，请前往查看")//内容
                                .setIcon(R.drawable.box_2)
                                .create();
                        dialog.show();
                    }
                    //更新状态和time1
                    utime_location_1  = System.currentTimeMillis();
                    old_degree = new_degree;
                    old_flag = new_flag;
                    unstable_location = true;
                }
                break;
            case 1:
                api.refresh_temperature(deviceId,apiKey);
                t1.setText("温度: "+api.temperature_zheng+"."+api.temperature_xiao);
                showDialog(1,api.temperature_zheng,tem_max,tem_min);
                break;
            case 2:
                api.refresh_guang(deviceId,apiKey);
                t2.setText("光强: "+api.guang_zheng+"."+api.guang_xiao);
                break;
            case 3:
                api.refresh_shidu(deviceId,apiKey);
                t3.setText("湿度: "+api.shi_du_zheng+"."+api.shi_du_xiao);
                showDialog(3,api.shi_du_zheng,shidu_max,shidu_min);
                break;
            case 4:
                api.refresh_isopen(deviceId,apiKey);
                int tempisopen = api.is_open;
                //考虑不稳定情况，记录不稳定开始时间
                if (tempisopen != is_open && !unstable_open) {
                    utime_open_1 = System.currentTimeMillis();
                    unstable_open = true;
                }
                //若不稳定，记录当前时间
                if (unstable_open) utime_open_2 = System.currentTimeMillis();
                //若由不稳定转向稳定
                if (unstable_open && tempisopen == is_open) {
                    unstable_open = false;
                }
                //确定改变状态
                if (tempisopen != is_open && unstable_open && utime_open_2-utime_open_1 >= 1000) {
                    openDialog(tempisopen);
                    openNotification(tempisopen);
                    String tempstr1="箱子: <font color='#FF0000'><small>打开</small></font>";
                    String tempstr2="箱子: <font color='#FF0000'><small>关闭</small></font>";
                    if (tempisopen == 1) t5.setText(Html.fromHtml(tempstr1));
                    else t5.setText(Html.fromHtml(tempstr2));
                    is_open = tempisopen;
                    unstable_open = false;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + msg.what);
        }
        //若关闭检测则不显示数据
        if (temperature_en.en == 0) t1.setText("温度:");
        if (guang_en.en == 0) t2.setText("光强:");
        if (shidu_en.en == 0) t3.setText("湿度:");
    }
    //弹窗
    public void showDialog(int what,int now,int max,int min) {
        switch (what) {
            case 1:
                if (now >= max && dialog_done[0] == false) {
                    AlertDialog dialog = new AlertDialog.Builder(ma)
                            .setTitle("温度警告")//标题
                            .setMessage("温度过高，超过"+max+"°C！")//内容
                            .setIcon(R.drawable.wendu)//图标
                            .create();
                    dialog.show();
                    dialog_done[0] = true;
                }
                else if (now < min && dialog_done[0] == false) {
                    AlertDialog dialog = new AlertDialog.Builder(ma)
                            .setTitle("温度警告")//标题
                            .setMessage("温度过低，低于"+min+"°C！")//内容
                            .setIcon(R.drawable.wendu)//图标
                            .create();
                    dialog.show();
                    dialog_done[0] = true;
                }
                else if (now < max && now >= min) dialog_done[0] = false;
                break;
            case 2:
                break;
            case 3:
                if (now>=max && dialog_done[2] == false) {
                    AlertDialog dialog = new AlertDialog.Builder(ma)
                            .setTitle("湿度警告")//标题
                            .setMessage("湿度过高！")//内容
                            .setIcon(R.drawable.shidu)//图标
                            .create();
                    dialog.show();
                    dialog_done[2] = true;
                }
                else if (now < min && dialog_done[2] == false) {
                    AlertDialog dialog = new AlertDialog.Builder(ma)
                            .setTitle("湿度警告")//标题
                            .setMessage("湿度过低！")//内容
                            .setIcon(R.drawable.shidu)//图标
                            .create();
                    dialog.show();
                    dialog_done[2] = true;
                }
                else if (now < max && now >= min) dialog_done[2] = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + what);
        }
    }
    //判断是否在线
    public boolean judgeOnline(String str) {
        int tempp = str.indexOf("online");
        char tempch = str.charAt(tempp+8);
        if (tempch == 't') return true;
        else return false;
    }
    //在线断线弹窗
    public void onlineDialog(boolean tempbool) {
        String tempstr1,tempstr2;
        tempstr1 = "通知";
        if (tempbool == true) {
            tempstr2 = "智能设备正常！";
        }
        else {
            tempstr2 = "智能设备异常！";
        }
        AlertDialog dialog = new AlertDialog.Builder(ma)
                .setTitle(tempstr1)//标题
                .setMessage(tempstr2)//内容
                .setIcon(R.mipmap.ic_launcher_box)//图标
                .create();
        dialog.show();
    }
    //创建通知渠道
    public void createChannel(String id) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel=new NotificationChannel(id, "name",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("123");
            NotificationManager notificationManager=ma.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    //在线断线通知
    void onlineNotification(boolean tempbool) {
        createChannel("3587");
        String tempstr1,tempstr2;
        tempstr1 = "通知";
        if (tempbool == true) {
            tempstr2 = "智能设备正常！";
        }
        else {
            tempstr2 = "智能设备异常！";
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(ma,"3587031")
                .setSmallIcon(R.mipmap.ic_launcher_box)
                .setContentTitle(tempstr1)
                .setContentText(tempstr2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(ma);
        notificationManagerCompat.notify(1,builder.build());
    }
    //箱子开闭弹窗
    public void openDialog(int is_open) {
        String tempstr1,tempstr2;
        if (is_open == 1) {
            tempstr1 = "箱子开启";
            tempstr2 = "箱子被打开！";
        }
        else {
            tempstr1 = "箱子关闭";
            tempstr2 = "箱子被关闭！";
        }
        AlertDialog dialog = new AlertDialog.Builder(ma)
                .setTitle(tempstr1)//标题
                .setMessage(tempstr2)//内容
                .setIcon(R.mipmap.ic_launcher_box)//图标
                .create();
        dialog.show();
    }
    //箱子开闭通知
    public void openNotification(int is_open) {
        createChannel("3588");
        String tempstr1,tempstr2;
        if (is_open == 1) {
            tempstr1 = "箱子开启";
            tempstr2 = "箱子被打开！";
        }
        else {
            tempstr1 = "箱子关闭";
            tempstr2 = "箱子被关闭！";
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(ma,"3587031")
                .setSmallIcon(R.mipmap.ic_launcher_box)
                .setContentTitle(tempstr1)
                .setContentText(tempstr2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(ma);
        notificationManagerCompat.notify(1,builder.build());
    }
}
