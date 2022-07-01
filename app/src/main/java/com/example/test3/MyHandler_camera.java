package com.example.test3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class MyHandler_camera extends Handler {
    //界面
    MyView_camera myView_camera;
    //开箱id
    RBean box_id;
    //旧的box_id
    int temp = 0;
    //api
    API_EDP api_edp = new API_EDP();
    //构造函数
    public MyHandler_camera(MyView_camera myView_camera, RBean box_id) {
        this.myView_camera = myView_camera;
        this.box_id = box_id;
        temp=box_id.value;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        //实时更新
        //如果开箱id发生了更新那么需要重新绘制
        if (msg.what == 5) {
            //刷新view展示新的照片
            if(box_id.state == 0)
                myView_camera.invalidate();
        }
    }
}
