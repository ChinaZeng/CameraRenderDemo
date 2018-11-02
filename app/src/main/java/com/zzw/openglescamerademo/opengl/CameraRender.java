package com.zzw.openglescamerademo.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer {
    private Context context;
    private RGBTexture rgbTexture;

    public CameraRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        rgbTexture = new RGBTexture(context);
        rgbTexture.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清空颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //设置背景颜色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        rgbTexture.draw();
    }

    public void setRenderData(int width, int height, byte[] rgbData) {
        if (rgbTexture != null) {
            rgbTexture.setData(width, height, rgbData);
        }
    }


}
