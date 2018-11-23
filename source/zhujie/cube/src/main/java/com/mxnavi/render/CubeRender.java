package com.mxnavi.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mxnavi.cube.R;
import com.mxnavi.util.BufferUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/11/7.
 */

public class CubeRender implements GLSurfaceView.Renderer {

    private float rotateX; //用于正方体x轴的旋转；
    private float rotateY; //用于正方体y轴的旋转；
    private float rotateZ; //用于正方体z轴的旋转；
    private boolean bAutoRotate = true;
    private float ratio;
    private float screenHeight;
    private float screenWidth;
    private float angle;
    private boolean flag = true;


    private boolean mIsTemp = false;

    public void setTemp(boolean isTemp) {
        mIsTemp = isTemp;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setRotate(float rotateX, float rotateY, boolean flag, float angle) {
        this.r_xyz[0] += rotateX;
        this.r_xyz[1]  += rotateY;
        this.flag = flag;
        this.r_xyz[2]  += angle;
    }

    private int one = 0x10000;
    //private int one = 98304;
    private int[] quarter = {
            -one, -one, one,
            one, -one, one,
            one, one, one,
            -one, one, one,

            -one, -one, -one,
            -one, one, -one,
            one, one, -one,
            one, -one, -one,

            -one, one, -one,
            -one, one, one,
            one, one, one,
            one, one, -one,

            -one, -one, -one,
            one, -one, -one,
            one, -one, one,
            -one, -one, one,

            one, -one, -one,
            one, one, -one,
            one, one, one,
            one, -one, one,

            -one, -one, -one,
            -one, -one, one,
            -one, one, one,
            -one, one, -one,
    };

//    private int[] quarter = {
//            0,0,one,
//            one,0,one,
//            one,one,one,
//            0,one,one,
//
//            0,0,0,
//            0,one,0,
//            one,one,0,
//            one,0,0,
//
//            0,one,0,
//            0,one,one,
//            one,one,one,
//            one,one,0,
//
//            0,0,0,
//            one,0,0,
//            one,0,one,
//            0,0,one,
//
//            one,0,0,
//            one,one,0,
//            one,one,one,
//            one,0,one,
//
//            0,0,0,
//            0,0,one,
//            0,one,one,
//            0,one,0,
//    };

    private int[] texCoords = {
            one, 0, 0, 0,
            0, one, one, one,

            0, 0, 0, one,
            one, one, one, 0,

            one, one, one, 0,
            0, 0, 0, one,

            0, one, one, one,
            one, 0, 0, 0,

            0, 0, 0, one,
            one, one, one, 0,

            one, 0, 0, 0,
            0, one, one, one,
    };

    // 三角形索引数据
    ByteBuffer indices1 = ByteBuffer.wrap(new byte[]{
            0, 1, 3, 2,
    });

    ByteBuffer indices2 = ByteBuffer.wrap(new byte[]{
            4, 5, 7, 6,
    });

    ByteBuffer indices3 = ByteBuffer.wrap(new byte[]{
            8, 9, 11, 10,
    });

    ByteBuffer indices4 = ByteBuffer.wrap(new byte[]{
            12, 13, 15, 14,
    });

    ByteBuffer indices5 = ByteBuffer.wrap(new byte[]{
            16, 17, 19, 18,
    });

    ByteBuffer indices6 = ByteBuffer.wrap(new byte[]{
            20, 21, 23, 22,});

    Bitmap[] bmp = new Bitmap[6];
    int[] tex_id = new int[6];

    public CubeRender(Context context) {
        bmp[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.willow);
        bmp[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.lotus);
        bmp[2] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.maple_leaves);
        bmp[3] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.snow_mountain);
        bmp[4] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.sun);
        bmp[5] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.moon);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        //设置清除屏幕时所用的颜色，参数依次为红、绿、蓝、Alpha值
        gl.glClearColor(1, 1, 1, 1);
        gl.glEnable(GL10.GL_CULL_FACE);
        //启用阴影平滑
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);//启用深度测试

        //以下是关于深度缓存的设置，非常重要
        gl.glClearDepthf(1.0f);//设置深度缓存
        gl.glDepthFunc(GL10.GL_LEQUAL);//所做深度测试的类型

        //告诉系统对透视进行修正
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        //允许2D贴图
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //gl.glGenTextures(6, tex_id, 0);
        IntBuffer textureBuffer = IntBuffer.allocate(6);
        gl.glGenTextures(6, textureBuffer);
        tex_id = textureBuffer.array();

        for (int i = 0; i < 6; i++) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[i]);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp[i], 0);

            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub
        //设置OpenGL场景大小
        //Log.i("TAG-->", "宽度: " + width + " 高度: " + height);
        Log.i("TAG-->", "surface改变");
        ratio = (float) width / height;
        GLES30.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);//设置为投影矩阵模式
        gl.glLoadIdentity();//重置
        gl.glFrustumf(-ratio, ratio, -1, 1, 2, 10);//设置视角
        gl.glMatrixMode(GL10.GL_MODELVIEW);//glPopMatrix glPushMatrix
        //gl.glPushMatrix();
        gl.glLoadIdentity();
    }

    float r_xyz[] = {0.0f, 0.0f, 0.0f};

    void myRotate(GL10 gl) {
        float[][] vec = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        for (int i = 0; i <= 2; i++) {
            gl.glRotatef(r_xyz[i], vec[i][0], vec[i][1], vec[i][2]);
        }
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        //清楚屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //重置当前的观察模型矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glLoadIdentity();
        //现将屏幕向里移动，用来画正方体
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glScalef(1.5f, 1.5f, 1.5f);
        angle = angle / 80f;

        if (rotateX > 0 && rotateY > 0) {
            rotateY = -rotateY;
        } else if (rotateX <= 0 && rotateY > 0) {
            rotateX = -rotateX;
            angle = -angle;
        } else if (rotateX > 0 && rotateY < 0) {
            rotateY = -rotateY;
        } else if (rotateX <= 0 && rotateX < 0) {
            rotateY = -rotateY;
        }

        //设置3个方向的旋转
        myRotate(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //纹理的使用与开启颜色渲染一样，需要开启纹理功能
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //设置正方体 各顶点
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, BufferUtil.iBuffer(quarter));
        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, BufferUtil.iBuffer(texCoords));

        // 绘制立方体并绑定纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[0]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices1);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[1]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices2);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[2]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices3);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[3]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices4);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[4]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices5);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[5]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices6);


        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glTranslatef(0.0f,0.0f,6.0f);
    }

    public float toGLX(float x) {
        return -1.0f * ratio + toGLWidth(x);
    }

    private float toGLWidth(float width) {
        return 2.0f * (width / screenWidth) * ratio;
    }

    public float toGLY(float y) {
        return 1.0f - toGLHeight(y);
    }

    private float toGLHeight(float height) {
        return 2.0f * (height / screenHeight);
    }

}
