package com.zzw.openglescamerademo.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.zzw.openglescamerademo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RGBTexture {

    private Context context;

    //顶点坐标
    static float vertexData[] = {   // in counterclockwise order:
            -1f, -1f, 0.0f, // bottom left
            1f, -1f, 0.0f, // bottom right
            -1f, 1f, 0.0f, // top left
            1f, 1f, 0.0f,  // top right
    };

    //纹理坐标
    static float textureData[] = {   // in counterclockwise order:
            0f, 1f, 0.0f, // bottom left
            1f, 1f, 0.0f, // bottom right
            0f, 0f, 0.0f, // top left
            1f, 0f, 0.0f,  // top right
    };

    //每一次取点的时候取几个点
    static final int COORDS_PER_VERTEX = 3;

    private final int vertexCount = vertexData.length / COORDS_PER_VERTEX;
    //每一次取的总的点 大小
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    //位置
    private FloatBuffer vertexBuffer;
    //纹理
    private FloatBuffer textureBuffer;

    private int program;

    //顶点位置
    private int avPosition;
    //纹理位置
    private int afPosition;

    //数据
    private int width;
    private int height;
    private ByteBuffer rgbData;

    private int textureId;

    public RGBTexture(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureData);
        textureBuffer.position(0);
    }


    public void draw() {

        if (width > 0 && height > 0 && rgbData != null) {
            //使用程序
            GLES20.glUseProgram(program);
            //设置顶点位置值
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glVertexAttribPointer(avPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

            //设置纹理位置值
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureBuffer);

            //激活纹理0来绑定rgb数据
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            //rgb24
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
                    width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, rgbData);
            //rgba
//            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
//                    width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rgbData);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

            rgbData.clear();
            rgbData = null;

            GLES20.glDisableVertexAttribArray(avPosition);
            GLES20.glDisableVertexAttribArray(afPosition);
        }
    }


    public void setData(int width, int height, byte[] data) {
        this.width = width;
        this.height = height;
        this.rgbData = ByteBuffer.wrap(data);
    }


    public void init() {
        String vertexSource = ShaderUtil.readRawTxt(context, R.raw.vertex_shader);
        String fragmentSource = ShaderUtil.readRawTxt(context, R.raw.fragment_shader);
        program = ShaderUtil.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            //获取顶点坐标字段
            avPosition = GLES20.glGetAttribLocation(program, "av_Position");
            //获取纹理坐标字段
            afPosition = GLES20.glGetAttribLocation(program, "af_Position");

            int[] textureIds = new int[1];
            //创建3个纹理
            GLES20.glGenTextures(1, textureIds, 0);
            textureId = textureIds[0];

            //绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        }
    }

    void onDestroy() {
        GLES20.glDeleteProgram(program);
    }
}
