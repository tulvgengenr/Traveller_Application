package com.example.test3;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

/**
 * 形状刷新Handler
 *
 *
 */
public class MyHandler_xingzhuang extends Handler {
    //api调用
    API api =new API();
    //自定义View
    MyView myView;
    public MyHandler_xingzhuang(MyView myView){
        this.myView=myView;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        //实时的位置更新,删除原来的marker点显示新的maker点
        if(msg.what == 4){
            myView.invalidate();
        }
    }
}
