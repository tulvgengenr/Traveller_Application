package com.example.test3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

import java.util.ArrayList;
import java.util.List;
/**
 * 定位导航页面（导航未实现）
 *
 *
 */
public class MainActivity3 extends AppCompatActivity implements Device, INaviInfoCallback, AMap.InfoWindowAdapter {
    public MapView mapview=null;
    public MyLocationStyle myLocationStyle;
    private AMap aMap;
    //api
    API api=new API();
    //handler与thread更新位置
    Myhandler_location myhandler_location;
    RefreshThread_location refreshThread_location;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivity.setStatusBarColor(this,0xFF6BA821);
        }
        setContentView(R.layout.activity_main3);
        //显示地图
           //获取地图控件引用
        mapview = (MapView) findViewById(R.id.map);
           //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapview.onCreate(savedInstanceState);
        if(aMap==null)
        aMap=mapview.getMap();
        //handler类实例化
        myhandler_location=new Myhandler_location(api,aMap);
        //线程Thread实例化
        refreshThread_location=new RefreshThread_location(myhandler_location);
        //开启定位权限
            //看权限是否开启
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            //开始定位
            Toast.makeText(this,"已开启定位权限",Toast.LENGTH_LONG).show();
        }
        //开启定位蓝点
                myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        //显示箱子位置
//        api.refresh_location(deviceId,apiKey);
//        LatLng latLng = new LatLng(get_location_double(api.wz,api.wx),get_location_double(api.jz,api.jx));
//        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("旅行箱").snippet("旅行箱"));
            //开启更新位置的线程
       refreshThread_location.start();





        //开启导航(未实现)
            //终点(箱子位置)
//        Poi end = new Poi("旅行箱", new LatLng(get_location_double(api.wz,api.wx),get_location_double(api.jz,api.jx)),null);
//            // 组件参数配置
//        AmapNaviParams params = new AmapNaviParams(null, null,end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
//            // 启动组件
//        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);
//            //退出导航组件
//        AmapNaviPage.getInstance().exitRouteActivity();
//        //起点
//        Poi start = new Poi("北京首都机场", new LatLng(40.080525,116.603039), "B000A28DAE");
//        //途经点
//        List<Poi> poiList = new ArrayList();
//        poiList.add(new Poi("故宫", new LatLng(39.918058,116.397026), "B000A8UIN8"));
//        //终点
//        Poi end = new Poi("北京大学", new LatLng(39.941823,116.426319), "B000A816R6");
//        // 组件参数配置
//        AmapNaviParams params = new AmapNaviParams(start, poiList, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
//        // 启动组件
//        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);
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
        temp1+=(double)x;
        int num=0;//位数
        for(num=0;;num++){
            if(x==0)
                break;
            else
                x=x/10;
            temp1=temp1/10;
        }
        result+=temp1;
        return result;
    }
        //权限申请的回调
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode){
                case 200:
                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    }else {
                        Toast.makeText(this,"未开启定位权限，请手动到设置去开去权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:break;
            }
        }


    //生命周期
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapview.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapview.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapview.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapview.onSaveInstanceState(outState);
    }


//Infowindow类中的方法
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

//INaviInfoCallback
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        System.out.println("i'm flag");
    }
    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }



    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

}
