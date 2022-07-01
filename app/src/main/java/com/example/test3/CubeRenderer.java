package com.example.test3;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {
    //绕三个轴旋转的角度
    double angle_x;
    double angle_y;
    double angle_z;
    //构造函数
    public CubeRenderer(int x,int y,int z){
        //反正切+弧度转角度
           angle_x=Math.atan2((double)y,(double)z);
           angle_x=angle_x*180/Math.PI;
           angle_y=Math.atan2((double)x,(double)z);
           angle_y=angle_y*180/Math.PI;
           angle_z=Math.atan2((double)y,(double)x);
           angle_z=angle_z*180/Math.PI;

    }
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //一般的opengl程序，首先要做的就是清屏
        gl.glMatrixMode(GL10.GL_MODELVIEW);//紧着这设置模型视图矩阵
        gl.glLoadIdentity();
        //视点变换，将相机位置设置为（0.0.3），同时指向（0.0.0）点，竖直向量为（0.1.0）指向正上方
        GLU.gluLookAt(gl, 4, 0, 0,0, 0, 0, 0, 0,1);
        //angle为旋转角度，后面的x、y、z表示布尔值，表明绕哪个轴旋转
        angle_x=0.0;
        angle_y=0.0;
        angle_z=90.0;
        gl.glRotatef((float)angle_x,1, 0, 0);//绕模型自身y轴旋转30度。
        gl.glRotatef((float)angle_y,0, 1, 0);//绕模型自身x轴旋转30度。
        gl.glRotatef((float)angle_z,0, 0, 1);//绕模型自身x轴旋转30度。
        System.out.println("绕x轴旋转："+angle_x);
        System.out.println("绕y轴旋转："+angle_y);
        System.out.println("绕z轴旋转："+angle_z);
        gl.glScalef(1.0f, 1.0f, 1.0f);//设置三方向的缩放系数
        gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
       // mCube.draw(gl,gl.GL_LINES);//渲染立方体
        mCube.draw(gl,gl.GL_TRIANGLES);//渲染立方体
    }
    //当屏幕 改变时候，比如手机横着拿，变成竖着拿，先onSurfaceCreated 在两次onSurfaceChanged 。
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口
        gl.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);//设置投影矩阵为透视投影
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);//正交投影

    }
    private Cube mCube;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);
        gl.glClearColor(1,1,1,1);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glLineWidth(4.0f);
        mCube=new Cube();
    }
}