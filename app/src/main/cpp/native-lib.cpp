#include <jni.h>
#include <string>
#include <libyuv.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>


extern "C" JNIEXPORT jstring JNICALL
Java_com_zzw_openglescamerademo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_zzw_openglescamerademo_CameraActivity_NV21_1T_1RGB(JNIEnv *env, jobject instance,
                                                            jint width,
                                                            jint height, jbyteArray nv21data_,
                                                            jbyteArray rgb888Data_) {
    jbyte *nv21data = env->GetByteArrayElements(nv21data_, NULL);
    jbyte *rgb888Data = env->GetByteArrayElements(rgb888Data_, NULL);


//    libyuv::NV21ToRGB24((uint8_t *) (nv21data), w,
//                        (uint8_t *) (nv21data + (w * h)), w / 2,
//                        (uint8_t *) rgb888Data, w * 3, w, h);

    if (width < 1 || height < 1 || nv21data == NULL || rgb888Data == NULL)
        return;

    cv::Mat dst(height, width, CV_8UC3, rgb888Data);
    cv::Mat src(height + height / 2, width, CV_8UC1, nv21data);
    cvtColor(src, dst, CV_YUV2BGR_NV21);

    env->ReleaseByteArrayElements(nv21data_, nv21data, 0);
    env->ReleaseByteArrayElements(rgb888Data_, rgb888Data, 0);
}