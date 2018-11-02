package com.zzw.openglescamerademo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class CameraGLSurfaceView extends GLSurfaceView {

    private CameraRender cameraRender;

    public CameraGLSurfaceView(Context context) {
        this(context, null);
    }

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        cameraRender = new CameraRender(context);
        setRenderer(cameraRender);
        //mode=GLSurfaceView.RENDERMODE_WHEN_DIRTY之后  调用requestRender()触发Render的onDrawFrame函数
        //mode=GLSurfaceView.RENDERMODE_CONTINUOUSLY之后  自动调用onDrawFrame  60fps左右
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    public void putRenderData(int w, int h, byte[] rgbData) {
        if (cameraRender != null) {
            cameraRender.setRenderData(w, h, rgbData);
            requestRender();
        }
    }
}
