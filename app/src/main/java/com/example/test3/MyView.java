package com.example.test3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.View;

/**
 * 自定义View用于显示箱子形状
 *
 *
 *
 */
/* 自定义继承View 的MyView*/
public  class MyView extends View implements Device {
    API api = new API();

    public MyView(Context context) {
        super(context);
    }

    /*重写onDraw()*/
    @Override
    protected void onDraw(Canvas canvas) {
        api.refresh_acce(deviceId, apiKey);
        int x = -api.acce_x;
        int y = api.acce_y;
        int z = api.acce_z;
        float degree = (float) (Math.atan2((double) x, (double) z) * 180 / Math.PI);
        int canvas_width = canvas.getWidth();
        int canvas_height = canvas.getHeight();
        //最上方
        Paint p_top = new Paint();
        p_top.setARGB(255, 0, 133, 119);
        canvas.drawRect(0,0,canvas_width,150,p_top);
        //标题
        Paint p_top_text = new Paint();
        p_top_text.setColor(Color.WHITE);
        p_top_text.setTextSize(56);
        p_top_text.setTextAlign(Paint.Align.CENTER);
        p_top_text.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("箱子形状",canvas_width/2,96,p_top_text);
        //System.out.println("角度为:" + degree);
        Paint p = new Paint();
        Bitmap bitmap;
        //画箱子形状
        if(y>700 && y<1200)
        {
            bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.zheng_y);
        }
        else if(y>300 && y<=700){
            bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.zheng_y_yiban);
        }
        else if(y>-1200 && y<-800){
            bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.fu_y);
        }
        else if(y>=-800 && y<-400){
            bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.fu_y_yiban);
        }
        else//平面二维的情况
            bitmap = rotateBimap(degree,BitmapFactory.decodeResource(getResources(), R.drawable.box));
        canvas.drawBitmap(bitmap, canvas_width/5 ,canvas_height/4, p);


//        //画标记线
//        p.setColor(Color.RED);
//        p.setTextSize(150);
//        canvas.drawText("g", 200, 1500, p);
//        canvas.drawLine(400, 1300, 400, 1600, p);
//        drawTrangle(canvas, p, 400, 1400, 400, 1600, 30, 30);
    }

    private Bitmap rotateBimap(float degree, Bitmap srcBitmap) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree,srcBitmap.getWidth() / 2, 2*srcBitmap.getHeight() / 3);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        return bitmap;
    }
    /**
     * 绘制三角
     * @param canvas
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param height
     * @param bottom
     */
    private void drawTrangle(Canvas canvas, Paint paintLine, float fromX, float fromY, float toX, float toY, int height, int bottom){
        try{
            float juli = (float) Math.sqrt((toX - fromX) * (toX - fromX)
                    + (toY - fromY) * (toY - fromY));// 获取线段距离
            float juliX = toX - fromX;// 有正负，不要取绝对值
            float juliY = toY - fromY;// 有正负，不要取绝对值
            float dianX = toX - (height / juli * juliX);
            float dianY = toY - (height / juli * juliY);
            float dian2X = fromX + (height / juli * juliX);
            float dian2Y = fromY + (height / juli * juliY);
            //终点的箭头
            Path path = new Path();
            path.moveTo(toX, toY);// 此点为三边形的起点
            path.lineTo(dianX + (bottom / juli * juliY), dianY
                    - (bottom / juli * juliX));
            path.lineTo(dianX - (bottom / juli * juliY), dianY
                    + (bottom / juli * juliX));
            path.close(); // 使这些点构成封闭的三边形
            canvas.drawPath(path, paintLine);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}