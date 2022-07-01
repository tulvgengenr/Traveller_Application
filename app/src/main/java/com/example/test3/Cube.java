package com.example.test3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cube {
    /**
     * 顶点位置Buffer对象
     */
    private FloatBuffer mVertexBuffer;
    /**
     * 顶点颜色Buffer对象
     */
    private FloatBuffer   mColorBuffer;
    /**
     * 实体模式下顶点索引对象
     */
    private ByteBuffer  mIndexBuffer;
    /**
     * 线框模式下顶点索引对象
     */
    private ByteBuffer mLineIndexBuffer;
    public Cube()
    {
        float one = 1.0f;
        float two= 2.0f;
        /**
         * 顶点位置坐标数组
         * 立方体有8个顶点
         */
        float vertices[] = {
                -one, -one, -one,//Vertex 0
                one, -one, -one,//1
                one,  one, -one,//2
                -one,  one, -one,//3
                -one, -one,  one,//4
                one, -one,  one,//5
                one,  one,  one,//6
                -one,  one,  one,//7
        };
        /**
         * 顶点颜色数组
         * 分别给8个顶点指定不同的颜色， RGBA 模式
         */
//        };
        float colors[] = {
                one,    0,    0, 0,//Vertex 0
                0,    0,    0,  1.0f,//1正
                0,    0,    0,  1.0f,//2正
                one,    0,    0, 0,//3
                one,    0,    0, 0,//4
                0,    0,    0,  1.0f,//5正
                0,    0,    0,  1.0f,//6正
                one,    0,    0, 0,//7
        };
        /**
         * 实体模式下，立方体有6个面，每个面由两个三角形组成
         * 这里分别指定每个面的索引。
         */
        byte indices[] = {
                0, 4, 5,    0, 5, 1,//Face 0
                1, 5, 6,    1, 6, 2,// 1
                2, 6, 7,    2, 7, 3,//2
                3, 7, 4,    3, 4, 0,//3
                4, 7, 6,    4, 6, 5,//4
                3, 0, 1,    3, 1, 2//5
        };
        /**
         * 线框模式下，立方体有8个顶点，12条边缘，
         * 这里指定画线模式所用到的12条线段
         */
        byte lineIndices[] = {
                0, 1,//Line 0
                0, 3,//1
                0, 4,//2
                1, 2,//3
                1, 5,//4
                2, 3,//5
                2, 6,//6
                3, 7,//7
                4, 5,//8
                4, 7,//9
                5, 6,//10
                6, 7//11
        };
        //根据数组来生成用于直接渲染的java.nio.Buffer

        //顶点位置Buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        //顶点颜色Buffer
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
        //实体模式下顶点索引Buffer
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        //线框模式下顶点索引Buffer
        mLineIndexBuffer = ByteBuffer.allocateDirect(lineIndices.length);
        mLineIndexBuffer.put(lineIndices);
        mLineIndexBuffer.position(0);
    }
    /**
     * 根据传入的模式来分别渲染实体模式立方体以及线框模式立方体
     * @param gl - OpenGL ES 渲染对象
     * @param mode - 渲染模式，GL10.GL_TRIANGLES 表示实体模式，GL10.GL_LINES 表示线框模式
     */
    public void draw(GL10 gl, int mode) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        if(mode == GL10.GL_TRIANGLES) {
            //如果是实体模式，则启用颜色，给每一个顶点指定一个颜色
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
            gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
            gl.glDrawElements(GL10.GL_LINES, 24, GL10.GL_UNSIGNED_BYTE, mLineIndexBuffer);
        }
        else if(mode == GL10.GL_LINES) {
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
            gl.glDrawElements(GL10.GL_LINES, 24, GL10.GL_UNSIGNED_BYTE, mLineIndexBuffer);
        }
    }
}
