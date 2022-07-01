package com.example.test3;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class BluetoothMonitorReceiver extends BroadcastReceiver implements Device {
    //主界面
    MainActivity ma;
    API api =new API();
    public BluetoothMonitorReceiver(MainActivity ma){
        this.ma = ma;
    }

    @Override
    /*在打开蓝牙或者关闭蓝牙时，不知道为什么这个函数执行了两次*/
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null){
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    final int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                                Toast.makeText(context, "蓝牙正在打开", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_ON:
                                Toast.makeText(context, "蓝牙已经打开", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                                Toast.makeText(context, "蓝牙正在关闭", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_OFF:
                                Toast.makeText(context, "蓝牙已经关闭", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    //dialog弹框
                    AlertDialog dialog1 = new AlertDialog.Builder(ma)
                            .setTitle("通知")
                            .setMessage("发现旅行箱，连接成功")
                            .setIcon(R.drawable.lanya)
                            .create();
                    dialog1.show();
//                    Toast.makeText(context,"蓝牙设备已连接",Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                     //引导用户打开蜂鸣器
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ma);//内部使用构建者的设计模式
                    final String[] items = {"是","否"};
                    if(Data.lanya) {
                        builder.setTitle("打开蜂鸣器寻找？");
                        builder.setIcon(R.drawable.lanya);
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {//第二个参数是设置默认选中哪一项-1代表默认都不选
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //在“是”的情况下打开蜂鸣器
                                if (which == 0) {
                                    Data.fengmingqi = true;
                                    api.postCommand(deviceId, apiKey, 30, "", 2, 1);
                                }
                            }
                        });
                        builder.create().show();//创建对象
                    }
                    //dialog弹框
                    AlertDialog dialog2 = new AlertDialog.Builder(ma)
                            .setTitle("通知")
                            .setMessage("旅行箱远离")
                            .setIcon(R.drawable.lanya)
                            .create();
                    dialog2.show();
                    //断开后要一直搜索直到搜索到设备(通过关闭蓝牙再打开蓝牙实现)
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    bluetoothAdapter.disable();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bluetoothAdapter.enable();
//                    Toast.makeText(context,"蓝牙设备已断开",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
