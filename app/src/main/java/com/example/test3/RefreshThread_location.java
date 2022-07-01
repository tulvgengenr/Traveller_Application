package com.example.test3;

import android.os.Message;

public class RefreshThread_location extends Thread implements Device {
    /**
     * 定位刷新Thread
     *
     *
     *
     */
    //handler用于sendMessage
    Myhandler_location handler;
    public RefreshThread_location(Myhandler_location handler){
        this.handler=handler;
    }
    @Override
    public void run() {
        while(true){
            try {
                //这个message必须每次new一次，不能发同一个message，会冲突
                Message message=new Message();
                message.what=4;
                handler.sendMessage(message);
                Thread.sleep(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
