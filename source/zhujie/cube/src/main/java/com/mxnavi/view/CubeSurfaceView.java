package com.mxnavi.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.mxnavi.render.CubeRender;

/**
 * Created by Administrator on 2018/11/7.
 */

public class CubeSurfaceView extends GLSurfaceView {

    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 640;
    private CubeRender cubeRender;
    private boolean flag = true;
    private float anglex = 0f;
    private float angley = 0f;
    private float anglez = 0f;

    private float lastX = 0f;
    private float lastY = 0f;

    public CubeSurfaceView(Context context) {
        super(context);
        cubeRender = new CubeRender(context);
        setRenderer(cubeRender);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
//        rotateX += event.getX() * TRACKBALL_SCALE_FACTOR;
//        rotateY += event.getY() * TRACKBALL_SCALE_FACTOR;
//        cubeRender.setRotate(rotateX, rotateY);
//        requestRender();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curY = event.getRawY();
                float curX = event.getRawX();
                float vecX = curX - lastX;
                float vecY = curY - lastY;
                lastY = curY;
                lastX = curX;
                float rx = 0f;
                float ry = 0f;
                float rz = 0f;
                float absvx = Math.abs(vecX);
                float absvy = Math.abs(vecY);
                if (vecX > 0 && vecY > 0 && absvx > absvy) {
                    rx = 0f;
                    ry = 3f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX > 0 && vecY > 0 && absvx < absvy) {
                    rx = 3f;
                    ry = 0f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX < 0 && vecY < 0 && absvx < absvy) {
                    rx = -3f;
                    ry = 0f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX < 0 && vecY < 0 && absvx > absvy) {
                    rx = 0f;
                    ry = -3f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX > 0 && vecY < 0 && absvx > absvy) {
                    rx = 0f;
                    ry = 3f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX > 0 && vecY < 0 && absvx < absvy) {
                    rx = -3f;
                    ry = 0f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX < 0 && vecY > 0 && absvx > absvy) {
                    rx = 0f;
                    ry = -3f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                } else if (vecX < 0 && vecY > 0 && absvx < absvy) {
                    rx = 3f;
                    ry = 0f;
                    rz = (float)Math.cos(vecX*vecX + vecY*vecY);
                }

                cubeRender.setRotate(rx, ry, flag, rz);

                requestRender();
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return true;
    }

}
