package com.example.test3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MyView_camera extends View implements Device {
    //API
    API_EDP api_edp = new API_EDP();
    //箱子ID
    RBean box_id;
    //字节数组
    byte[] dataImage;
    //图片
    ImageView imageView;

    //构造函数
    public MyView_camera(Context context) {
        super(context);
    }
    public MyView_camera(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyView_camera(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle );
    }
    public void setBox_id(RBean box_id){
        this.box_id = box_id;
    }
    //onDraw
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        //照片的字节数组
        Bitmap image = null;
        //画笔
        Paint p = new Paint();
        //byte数组转换为bitmap
        image = byte2image(api_edp.getFile(Device.apiKey_Camera, box_id.value, box_id.en));//获取照片
        //最后一张3
        if (box_id.en >= box_id.sum) {
//            if (api_edp.Lookup(deviceId_Camera,apiKey_Camera,box_id.value,box_id.en+1) == 1) {
//                box_id.en ++;
//                return;
//            }
            box_id.state = 1;
            return;
        }
        else if (image == null) {
            box_id.en++;
            return;
        }
        imageView.setImageBitmap(image);
        box_id.en++;//下一帧
        box_id.state = 0;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }
    //byte数组转bitmap
    public Bitmap byte2image(byte[] dataImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        if (dataImage != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImage,0,dataImage.length,options);
            return bitmap;
        }
        else return null;
    }
    //画上方标签
    public void drawTitle(Canvas canvas) {
        //最上方
        Paint p_top = new Paint();
        p_top.setARGB(255, 30, 33, 42);
        canvas.drawRect(0,0,canvas.getWidth(),150,p_top);
        //标题
        Paint p_top_text = new Paint();
        p_top_text.setColor(Color.WHITE);
        p_top_text.setTextSize(56);
        p_top_text.setTextAlign(Paint.Align.CENTER);
        p_top_text.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("摄像记录",canvas.getWidth()/2,96,p_top_text);
    }
}