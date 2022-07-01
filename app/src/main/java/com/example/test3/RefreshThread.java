package com.example.test3;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
/**
 * 温度、光强、湿度刷新Thread
 *
 *
 *
 */
public class RefreshThread extends Thread implements Device{
    //handler用于sendMessage
    MyHandler handler;
    //使能
    private RBean  temperature_en;//温度检测使能
    private RBean  guang_en;//光强检测使能
    private RBean  shidu_en;//湿度检测使能
    //构造函数
    public RefreshThread (MyHandler handler,RBean int1,RBean  int2, RBean int3)
    {
        this.handler=handler;
        this.temperature_en=int1;
        this.guang_en=int2;
        this.shidu_en=int3;
    }
            @Override
            public void run() {
                while (true){
                    //一个线程内发消息不能用同一个message
                    Message message0 = new Message();
                    Message message1 = new Message();
                    Message message2 = new Message();
                    Message message3 = new Message();
                    Message message4 = new Message();
                    for(int i=0;i<=4;i++){
                        //使能有效就发message，否则不发
                        switch (i){
                            case 0:
                                message0.what = 0;
                                handler.sendMessage(message0);
                                break;
                            case 1:
                                if(temperature_en.en==1) {
                                    message1.what=1;
                                    handler.sendMessage(message1);
                                }
                                break;
                            case 2:
                                if(guang_en.en==1) {
                                    message2.what=2;
                                    handler.sendMessage(message2);
                                }
                                break;
                            case 3:
                                if(shidu_en.en==1) {
                                    message3.what=3;
                                    handler.sendMessage(message3);
                                }
                                break;
                            case 4:
                                message4.what = 4;
                                handler.sendMessage(message4);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + i);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
}