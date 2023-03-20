#include <jni.h>
#include <string>
#include <android/bitmap.h>

extern "C" {
#include "gif_lib.h"
}

#include <android/log.h>

#define  LOG_TAG    "GifTest"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  dispose(ext) (((ext)->Bytes[0] & 0x1c) >> 2)
#define  trans_index(ext) ((ext)->Bytes[3])
#define  transparency(ext) ((ext)->Bytes[0] & 1)

struct GifBean {
    // 当前帧数
    int current_frame;
    // 总帧数
    int total_frame;
    int *delays;
};

// 把字节组装成int
#define  argb(a, r, g, b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)

extern "C"
JNIEXPORT jlong JNICALL
Java_com_demo_gif_GifHandler_loadGif(JNIEnv *env, jclass clazz, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);
    // 打开成功还是失败
    int error;
    // 打开文件
    // SaveImages 里面存像素内容
    GifFileType *gifFileType = DGifOpenFileName(path, &error);
    // 初始化缓冲区
    DGifSlurp(gifFileType);
    // 声明对象，存当前帧数，总帧数
    GifBean *gifBean = static_cast<GifBean *>(malloc(sizeof(GifBean)));
    // 清空成员
    memset(gifBean, 0, sizeof(GifBean));
    // 绑定
    gifFileType->UserData = gifBean;
    // 当前帧
    gifBean->current_frame = 0;
    // 总帧数
    gifBean->total_frame = gifFileType->ImageCount;
    env->ReleaseStringUTFChars(path_, path);
    // return (jlong) (gifFileType);
    return reinterpret_cast<jlong>(gifFileType);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_demo_gif_GifHandler_getWidth(JNIEnv *env, jclass clazz, jlong gif_hander) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_hander);
    return gifFileType->SWidth;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_demo_gif_GifHandler_getHeight(JNIEnv *env, jclass clazz, jlong gif_hander) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_hander);
    return gifFileType->SHeight;
}

// 绘制 bitmap
// GIF89a
int drawFrame(GifFileType *gif, AndroidBitmapInfo info, void *pixels, bool force_dispose_1) {
    GifColorType *bg;
    GifColorType *color;
    SavedImage *frame;
    ExtensionBlock *ext = 0;
    GifImageDesc *frameInfo;
    ColorMapObject *colorMap;
    int *line;
    int width, height, x, y, j, loc, n, inc, p;
    void *px;
    GifBean *gifBean = static_cast<GifBean *>(gif->UserData);
    width = gif->SWidth;
    height = gif->SHeight;
    frame = &(gif->SavedImages[gifBean->current_frame]);
    frameInfo = &(frame->ImageDesc);
    if (frameInfo->ColorMap) {
        colorMap = frameInfo->ColorMap;
    } else {
        colorMap = gif->SColorMap;
    }
    bg = &colorMap->Colors[gif->SBackGroundColor];
    for (j = 0; j < frame->ExtensionBlockCount; j++) {
        if (frame->ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
            ext = &(frame->ExtensionBlocks[j]);
            break;
        }
    }
    // For dispose = 1, we assume its been drawn
    px = pixels;
    if (ext && dispose(ext) == 1 && force_dispose_1 && gifBean->current_frame > 0) {
        gifBean->current_frame = gifBean->current_frame - 1,
                drawFrame(gif, info, pixels, true);
    } else if (ext && dispose(ext) == 2 && bg) {
        for (y = 0; y < height; y++) {
            line = (int *) px;
            for (x = 0; x < width; x++) {
                line[x] = argb(255, bg->Red, bg->Green, bg->Blue);
            }
            px = (int *) ((char *) px + info.stride);
        }
    } else if (ext && dispose(ext) == 3 && gifBean->current_frame > 1) {
        gifBean->current_frame = gifBean->current_frame - 2,
                drawFrame(gif, info, pixels, true);
    }
    px = pixels;
    if (frameInfo->Interlace) {
        n = 0;
        inc = 8;
        p = 0;
        px = (int *) ((char *) px + info.stride * frameInfo->Top);
        for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; y++) {
            for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; x++) {
                loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
                if (ext && frame->RasterBits[loc] == trans_index(ext) && transparency(ext)) {
                    continue;
                }
                color = (ext && frame->RasterBits[loc] == trans_index(ext)) ? bg
                                                                            : &colorMap->Colors[frame->RasterBits[loc]];
                if (color)
                    line[x] = argb(255, color->Red, color->Green, color->Blue);
            }
            px = (int *) ((char *) px + info.stride * inc);
            n += inc;
            if (n >= frameInfo->Height) {
                n = 0;
                switch (p) {
                    case 0:
                        px = (int *) ((char *) pixels + info.stride * (4 + frameInfo->Top));
                        inc = 8;
                        p++;
                        break;
                    case 1:
                        px = (int *) ((char *) pixels + info.stride * (2 + frameInfo->Top));
                        inc = 4;
                        p++;
                        break;
                    case 2:
                        px = (int *) ((char *) pixels + info.stride * (1 + frameInfo->Top));
                        inc = 2;
                        p++;
                }
            }
        }
    } else {
        px = (int *) ((char *) px + info.stride * frameInfo->Top);
        for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; y++) {
            line = (int *) px;
            for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; x++) {
                loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
                if (ext && frame->RasterBits[loc] == trans_index(ext) && transparency(ext)) {
                    continue;
                }
                color = (ext && frame->RasterBits[loc] == trans_index(ext)) ? bg
                                                                            : &colorMap->Colors[frame->RasterBits[loc]];
                if (color)
                    line[x] = argb(255, color->Red, color->Green, color->Blue);
            }
            px = (int *) ((char *) px + info.stride);
        }
    }
    GraphicsControlBlock gcb;//获取控制信息
    DGifSavedExtensionToGCB(gif, gifBean->current_frame, &gcb);
    int delay = gcb.DelayTime * 10;
    LOGE("delay %d", delay);
    return delay;
}

// GIF87a
int drawFrame1(GifFileType *gifFileType, AndroidBitmapInfo info, void *pixels) {
    GifBean *gifBean = static_cast<GifBean *>(gifFileType->UserData);
    // 拿到当前帧
    SavedImage savedImage = gifFileType->SavedImages[gifBean->current_frame];
    // 图像内容包括像素 RasterBits 和描述 ImageDesc
    // 获取图像描述部分
    GifImageDesc frameInfo = savedImage.ImageDesc;
    // 获取像素字典
    ColorMapObject *colorMapObject = frameInfo.ColorMap;
//    像素
//    savedImage.RasterBits[i];
//FFmpeg  视频
//记录每一行的首地址
//bitmap
    int *px = (int *) pixels;

    // 临时行索引
    int *line;
    // 索引
    int pointPixel;
    // 压缩像素数据
    GifByteType gifByteType;
    // 操作解压
    GifColorType gifColorType;
    // y=行 x=列，只需要渲染从 Top 和 Left 开始的区域
    for (int y = frameInfo.Top; y < frameInfo.Top + frameInfo.Height; ++y) {
        // 每次遍历行，首地址传给 line
        line = px;
        for (int x = frameInfo.Left; x < frameInfo.Left + frameInfo.Width; ++x) {
            // 定位像素索引
            pointPixel = (y - frameInfo.Top) * frameInfo.Width + (x - frameInfo.Left);
            // 通过索引找到压缩像素
            gifByteType = savedImage.RasterBits[pointPixel];
            // 借助像素字典解压
            gifColorType = colorMapObject->Colors[gifByteType];
            // line 进行复制
            line[x] = argb(255, gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
        // 转到下一行，info.stride：一行的字节数
        px = (int *) ((char *) px + info.stride);
    }
    // 图形控制扩展，获取控制信息
    GraphicsControlBlock gcb;
    // 为当前帧获取扩展块
    DGifSavedExtensionToGCB(gifFileType, gifBean->current_frame, &gcb);
    int delay = gcb.DelayTime * 10;
    LOGE("delay %d", delay);
    return delay;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_demo_gif_GifHandler_updateFrame(JNIEnv *env, jclass clazz, jlong gif_point,
                                         jobject bitmap) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_point);
    // 渲染必须获取宽高，方法一：获取宽高
    int width = gifFileType->SWidth;
    int height = gifFileType->SHeight;
    // 还有另外一种方法：使用 android/bitmap.h 根据 bitmap 获取
    AndroidBitmapInfo info;
    // 获取 bitmap 信息
    AndroidBitmap_getInfo(env, bitmap, &info);
    // 定义数组
    int *pixels = NULL;
    // 把 bitmap 转换成像素二维数组。锁住当前bitmap，让其他线程无法操作 bitmap
    AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&pixels));
    // drawFrame 绘制颜色，读取每一帧的播放时间![](../assets/soogif.gif)
    // GIF87a
    // int delay = drawFrame1(gifFileType, info, pixels);
    // GIF89a
    int delay = drawFrame(gifFileType, info, pixels, false);
    // 解锁
    AndroidBitmap_unlockPixels(env, bitmap);
    GifBean *gifBean = static_cast<GifBean *>(gifFileType->UserData);
    // 播放结束重复播放
    gifBean->current_frame++;
    if (gifBean->current_frame > gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
    }
    return delay;
}
