package com.example.test3;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import org.w3c.dom.Text;
/**
 * 定位刷新Handler
 *
 *
 */
//时刻更新箱子位置用到的handler
public class Myhandler_location extends Handler implements Device{
    API api;
    AMap amap;
    //记录箱子的位置
    LatLng latLng;
    //标记点
    Marker marker;
//    //写死的时候的经纬度
//    double jd;
//    double wd;
    public Myhandler_location(API api,AMap amap){
        this.api=api;
        this.amap=amap;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        int num = msg.what;
        //实时的位置更新,删除原来的marker点显示新的maker点
        if(msg.what==4){
            System.out.println("i 'm flag");
            if(marker!=null)
            marker.remove();
            //正常情况
            api.refresh_location(deviceId,apiKey);
            latLng = new LatLng(get_location_double(api.wz,api.wx),get_location_double(api.jz,api.jx));
//            //写死
//            jd = Math.random()*(112.028286-112.027959)+112.027959;
//            wd =Math.random()*(22.934812-22.934634)+22.934634;
//            latLng =  new LatLng(wd,jd);
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            marker = amap.addMarker(new MarkerOptions().position(latLng).title("旅行箱").snippet("旅行箱"));
            System.out.println("经度为："+get_location_double(api.jz,api.jx));
            System.out.println("纬度为："+get_location_double(api.wz,api.wx));
        }
    }
    /**
     * 辅助函数1
     * 返回经（纬）度的double类型数据
     * @param z:纬度的整数部分
     * @param x:纬度的小数部分
     * @return
     */
    public double get_location_double(int z,int x){
        double result=0.0;
        //处理整数部分
        result+=(double)z;
        //处理小数部分
        double temp1=0.0;
        temp1+=(double)x/1000000.0;
//        int num=0;//位数
//        for(num=0;;num++){
//            if(x==0)
//                break;
//            else
//                x=x/10;
//            temp1=temp1/10;
//        }
        result+=temp1;
        return result;
    }
}
