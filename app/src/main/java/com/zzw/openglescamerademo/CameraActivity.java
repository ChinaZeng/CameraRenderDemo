package com.zzw.openglescamerademo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zzw.openglescamerademo.cameraview.CameraImpl;
import com.zzw.openglescamerademo.cameraview.CameraView;
import com.zzw.openglescamerademo.opengl.CameraGLSurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;

public class CameraActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    CameraGLSurfaceView glSurfaceView;
    CameraView cameraview;
//    Bitmap bitmap;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {

                    cameraview.start();
//                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private Mat mRgba;
    private Mat mGray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        glSurfaceView = findViewById(R.id.glsurfaceview);
        cameraview = findViewById(R.id.cameraview);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        cameraview.addCallback(new CameraImpl.Callback() {
            @Override
            public void onPicturePreview(CameraImpl camera, int w, int h, byte[] data) {
                super.onPicturePreview(camera, w, h, data);
//                Log.e("zzz", "data.length =" + data.length + "  w= " + w + " h= " + h + " w*h*3/2 = " + (w * h * 3 / 2));


                long a = System.currentTimeMillis();
                byte[] rgb = new byte[w * h * 3];
                NV21_T_RGB(w, h, data, rgb);


//                if (mGray == null) {
//                    mGray = new Mat(w, h + h / 2, CvType.CV_8UC1);
//                }
//                if (mRgba == null) {
//                    mRgba = new Mat(w, h, CvType.CV_8UC4);
//                }
//                mGray.put(0, 0, data);
//                Imgproc.cvtColor(mGray, mRgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);
//                byte[] d = new byte[w * h * 4];
//                int i = mRgba.get(0, 0, d);

                long b = System.currentTimeMillis();
                Log.e("zzz", "time = " + (b - a));
                glSurfaceView.putRenderData(w, h, rgb);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        cameraview.start();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    protected void onPause() {
        cameraview.stop();
        super.onPause();
    }

    /**
     * @方法描述 Bitmap转RGB
     */
    public static byte[] bitmap2RGB(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();  //返回可用于储存此位图像素的最小字节数

        ByteBuffer buffer = ByteBuffer.allocate(bytes); //  使用allocate()静态方法创建字节缓冲区
        bitmap.copyPixelsToBuffer(buffer); // 将位图的像素复制到指定的缓冲区

        byte[] rgba = buffer.array();
        byte[] pixels = new byte[(rgba.length / 4) * 3];

        int count = rgba.length / 4;

        //Bitmap像素点的色彩通道排列顺序是RGBA
        for (int i = 0; i < count; i++) {
            pixels[i * 3] = rgba[i * 4];        //R
            pixels[i * 3 + 1] = rgba[i * 4 + 1];    //G
            pixels[i * 3 + 2] = rgba[i * 4 + 2];       //B
        }
        return pixels;
    }

    public native void NV21_T_RGB(int w, int h, byte[] nv21data, byte[] rgb888Data);
}
