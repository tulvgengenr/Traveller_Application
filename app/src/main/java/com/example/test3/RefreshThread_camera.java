package com.example.test3;

import android.os.Message;

public class  RefreshThread_camera extends Thread {
    //hanlder
    MyHandler_camera myHandler_camera;
    //开箱id
    RBean box_id;
    RBean box_total;
    //api
    API_EDP api_edp;
    public RefreshThread_camera(MyHandler_camera myHandler_camera,RBean box_id,RBean box_total,API_EDP api_edp) {
        this.myHandler_camera = myHandler_camera;
        this.box_id=box_id;
        this.api_edp = api_edp;
        this.box_total = box_total;
    }

    @Override
    public void run() {
        while(true){
            try {
                //这个message必须每次new一次，不能发同一个message，会冲突
                //每1秒更新一次开箱总id,并以message形式发送给handler
                Message message = new Message();
                message.what = 5;
                myHandler_camera.sendMessage(message);
                Thread.sleep(25);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
