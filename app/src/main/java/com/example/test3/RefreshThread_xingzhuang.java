package com.example.test3;

import android.os.Message;

/**
 * 箱子形状刷新Thread
 *
 *
 *
 */
public class RefreshThread_xingzhuang  extends Thread{
    MyHandler_xingzhuang myHandler_xingzhuang;
    public RefreshThread_xingzhuang(MyHandler_xingzhuang myHandler_xingzhuang){
        this.myHandler_xingzhuang=myHandler_xingzhuang;
    }
    //控制每10秒刷新一次形状
    @Override
    public void run() {
        while(true){
            try {
                //这个message必须每次new一次，不能发同一个message，会冲突
                Message message=new Message();
                message.what=4;
                myHandler_xingzhuang.sendMessage(message);
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
