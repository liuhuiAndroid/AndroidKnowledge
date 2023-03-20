#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <pthread.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <iostream>
#include <android/log.h>
#include <android/native_window_jni.h>

using namespace cv;

DetectionBasedTracker *tracker = 0;
static int index = 0;
ANativeWindow *window = 0;

// 检测器Adapter
class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector)
            : IDetector(), Detector(detector) {

    }

    // 适配器检测到了很多物体，交给适配器，告诉我 这些形状是不是属于这个分类，类似于RecyclerView产生了滑动，通知Adapter要拿数据过来了。
    void detect(const cv::Mat &image, std::vector<cv::Rect> &objects) {
        Detector->detectMultiScale(image, objects, scaleFactor, minNeighbours, 0, minObjSize,
                                   maxObjSize);
    }

private:
    CascadeDetectorAdapter();

    // 分类器
    cv::Ptr<cv::CascadeClassifier> Detector;
};

// extern "C"：把下面代码转换成C的代码，不写方法名加了参数无法识别
extern "C"
// JNIEXPORT void JNICALL：__attribute__ ((visibility ("default"))) void JNICALL：
// JNIEXPORT：方法可见性
// void：方法返回值
// JNICALL：空实现宏，可移除
JNIEXPORT void JNICALL
// JNIEnv *env：是每个JNI函数的第一个参数，位置不可变；内部定义了很多函数
// jobject thiz：代表调用的对象
Java_com_face_identity_MainActivity_init(JNIEnv *env, jobject thiz, jstring model_) {
    // 把路径变成C++的路径
    const char *model = env->GetStringUTFChars(model_, 0);
    if (tracker) {
        tracker->stop();
        delete tracker;
        tracker = 0;
    }
    // 通过智能指针实例化检测器，类似于 CascadeClassifier *cascadeClassifier= new CascadeClassifier();
    // Ptr：智能指针
    Ptr<CascadeClassifier> classifier = makePtr<CascadeClassifier>(model);
    // 实例化检测器
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(classifier);
    // 实例化跟踪器，检测器和跟踪器一样的
    Ptr<CascadeClassifier> classifier1 = makePtr<CascadeClassifier>(model);
    // 实例化跟踪适配器
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(classifier1);
    // 拿去用的跟踪器
    DetectionBasedTracker::Parameters detectorParams;
    tracker = new DetectionBasedTracker(mainDetector, trackingDetector, detectorParams);
    // 开启跟踪器
    tracker->run();

    env->ReleaseStringUTFChars(model_, model);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_face_identity_MainActivity_postData(JNIEnv *env, jobject thiz, jbyteArray data_, jint w,
                                             jint h, jint cameraId) {

    // nv21的数据，Java字节数组改为C++字节数组
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    Mat src(h + h / 2, w, CV_8UC1, data);
    // 颜色格式的转换，将nv21的yuv数据转成了rgba
    cvtColor(src, src, COLOR_YUV2RGBA_NV21);
//    // 测试输出图片
//    char p[100];
//    mkdir("/sdcard/identity",0777);
//    sprintf(p, "/sdcard/identity/%d.jpg", index++);
//    imwrite(p, src);
    //=====================================================
    if (cameraId == 1) {
        // 前置摄像头，需要逆时针旋转90度
        rotate(src, src, ROTATE_90_COUNTERCLOCKWISE);
        // 水平翻转镜像
        flip(src, src, 1);
    } else {
        // 顺时针旋转90度
        rotate(src, src, ROTATE_90_CLOCKWISE);
    }
    //=====================================================
    // 灰度
    Mat gray;
    cvtColor(src, gray, COLOR_RGBA2GRAY);
    // 增强对比度 (直方图均衡)
    equalizeHist(gray, gray);
    //=====================================================
    // 定位人脸N个
    tracker->process(gray);
    std::vector<Rect> faces;
    tracker->getObjects(faces);
    // 测试输出图片
//    char p[100];
//    mkdir("/sdcard/identity", 0777);
    for (Rect face: faces) {
//        sprintf(p, "/sdcard/identity/%d.jpg", index++);
//        imwrite(p, src);
//        Mat m;
//        m = gray(face).clone();
//        resize(m, m, Size(24, 24));
//        imwrite(p, m);
        // 画矩形，分别指定bgra
        rectangle(src, face, Scalar(255, 0, 255));
    }
    if (window) {
        do {
            ANativeWindow_setBuffersGeometry(window, src.cols, src.rows, WINDOW_FORMAT_RGBA_8888);
            ANativeWindow_Buffer buffer;
            if (ANativeWindow_lock(window, &buffer, 0)) {
                ANativeWindow_release(window);
                window = 0;
            }
            int srclineSize = src.cols * 4;
            // 目的数据
            int dstlineSize = buffer.stride * 4;
            // 待显示的缓冲区
            uint8_t *dstData = static_cast<uint8_t *>(buffer.bits);
            for (int i = 0; i < buffer.height; ++i) {
                memcpy(dstData + i * dstlineSize, src.data + i * srclineSize, srclineSize);
            }
            ANativeWindow_unlockAndPost(window);
        } while (0);
    }
    // 释放Java字节数组
    env->ReleaseByteArrayElements(data_, data, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_face_identity_MainActivity_setSurface(JNIEnv *env, jobject thiz, jobject surface) {
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
    window = ANativeWindow_fromSurface(env, surface);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_face_identity_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello World.";
    return env->NewStringUTF(hello.c_str());
}