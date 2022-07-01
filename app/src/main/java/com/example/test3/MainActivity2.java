package com.example.test3;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

/**
 * 命令下发页面（按钮初始化、退出程序后保留状态未实现）
 *
 *
 */
public class MainActivity2 extends Activity implements Device {
    EditText eText;
    Button b1;//显示内容
    Button b3;//关闭屏幕
    Switch s2;//蜂鸣器
    Switch s4;//照明灯
    Switch s5;//温度检测
    Switch s6;//湿度检测
    Switch s7;//光强检测
    Switch s8;//六轴检测
    Switch s9;//蓝牙检测
    Switch s10;//打开/关闭wifi断开警报
    Switch s11;//一键实现低功耗
    API api=new API();
    API_EDP  api_edp = new API_EDP();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivity.setStatusBarColor(this,0xFF1777FF);
        }
        setContentView(R.layout.activity_main2);
        //与前端部件相连
        eText=findViewById(R.id.edittext);
        b1 = findViewById(R.id.btn1);
        b3 = findViewById(R.id.btn3);
        s2 = findViewById(R.id.sw2);    s2.setChecked(Data.fengmingqi);
        s4 = findViewById(R.id.sw4);    s4.setChecked(Data.zhaomingdeng);
        s5 = findViewById(R.id.sw5);    s5.setChecked(Data.wendujiance);
        s6 = findViewById(R.id.sw6);    s6.setChecked(Data.shidujiance);
        s7 = findViewById(R.id.sw7);    s7.setChecked(Data.guangqiangjiance);
        s8 = findViewById(R.id.sw8);    s8.setChecked(Data.liuzhoujiance);
        s10 = findViewById(R.id.sw10);  s10.setChecked(Data.wifi_isopen);
        s11 = findViewById(R.id.sw11);  s11.setChecked(Data.lowpower_isopen);
        s9 =findViewById(R.id.sw9);     s9.setChecked(Data.lanya);
        //按钮监听器
        //显示信息
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.postCommand(deviceId,apiKey,30,eText.getText().toString(),1,1);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.postCommand(deviceId,apiKey,30,"",3,1);
            }
        });
        //开关监听器
        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",2,1);
                else api.postCommand(deviceId,apiKey,30,"",2,0);
                Data.fengmingqi = !Data.fengmingqi;
            }
        });
        s4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",4,1);
                else api.postCommand(deviceId,apiKey,30,"",4,0);
                Data.zhaomingdeng = !Data.zhaomingdeng;
            }
        });
        s5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",5,1);
                else api.postCommand(deviceId,apiKey,30,"",5,0);
                Data.wendujiance = !Data.wendujiance;
            }
        });
        s6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",6,1);
                else api.postCommand(deviceId,apiKey,30,"",6,0);
                Data.shidujiance = !Data.shidujiance;
            }
        });
        s7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",7,1);
                else api.postCommand(deviceId,apiKey,30,"",7,0);
                Data.guangqiangjiance = !Data.guangqiangjiance;
            }
        });
        s8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",8,1);
                else api.postCommand(deviceId,apiKey,30,"",8,0);
                Data.liuzhoujiance = !Data.liuzhoujiance;
            }
        });
        //蓝牙使能
        s9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
//                if(isChecked)
//                    api_edp.postCommand(Device.deviceId_Camera,Device.apiKey_Camera,30,"",9,1);
//                else
//                    api_edp.postCommand(Device.deviceId_Camera,Device.apiKey_Camera,30,"",9,0);
                Data.lanya = !Data.lanya;
            }
        });
        s10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                if(isChecked) api.postCommand(deviceId,apiKey,30,"",10,1);
                else api.postCommand(deviceId,apiKey,30,"",10,0);
                Data.wifi_isopen=!Data.wifi_isopen;
            }
        });
        s11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //防止初始化的时候出发监听
                if (!buttonView.isPressed()) {
                    return;
                }
                //如果打开低功耗模式，那么需要关闭温度检测、湿度检测、光强检测和六轴检测
                if(isChecked)
                {
                    api.postCommand(deviceId,apiKey,30,"",5,0);
                    api.postCommand(deviceId,apiKey,30,"",6,0);
                    api.postCommand(deviceId,apiKey,30,"",7,0);
                    api.postCommand(deviceId,apiKey,30,"",8,0);
                    Data.wendujiance = false;
                    Data.shidujiance = false;
                    Data.guangqiangjiance = false;
                    Data.liuzhoujiance = false;
                }
                //关闭低功耗模式，默认全部打开温度检测、湿度检测、光强检测和六轴检测
                else
                {
                    api.postCommand(deviceId,apiKey,30,"",5,1);
                    api.postCommand(deviceId,apiKey,30,"",6,1);
                    api.postCommand(deviceId,apiKey,30,"",7,1);
                    api.postCommand(deviceId,apiKey,30,"",8,1);
                    Data.wendujiance = true;
                    Data.shidujiance = true;
                    Data.guangqiangjiance = true;
                    Data.liuzhoujiance = true;
                }
                Data.lowpower_isopen  = !Data.lowpower_isopen;
                s5.setChecked(Data.wendujiance);
                s6.setChecked(Data.shidujiance);
                s7.setChecked(Data.guangqiangjiance);
                s8.setChecked(Data.liuzhoujiance);
            }
        });
    }
}
