package com.demo.opengl;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;

public class EGLEnv {

    private final EGLDisplay mEglDisplay;
    //  rendering context 的 handle
    private final EGLContext mEglContext;
    private final EGLSurface mEglSurface;
    private final ScreenFilter screenFilter;

    public EGLEnv(Context context, EGLContext mGlContext, Surface surface, int width, int height) {
        // EGL14 对应于 EGL 1.4，是目前使用的比较多的版本
        // 从 EGL 运行的操作系统中获取一个 Display 的 handle。
        // 函数输入 display_id：EGL_DEFAULT_DISPLAY，得到一个默认的 Display
        // 函数输出：用于显示图片绘制的 Display 的 handle
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException(
                    "Unable to get EGL14 display: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        final int[] version = new int[2];
        // 针对某 display 初始化某版本的 EGL
        if (!EGL14.eglInitialize(mEglDisplay, version, 0, version, 1)) {
            // 可以借用 eglGetError 来抓取错误代码
            throw new RuntimeException(
                    "Unable to initialize EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        // 配置需求信息
        int[] configAttributes = {
                EGL14.EGL_RED_SIZE, 8,      // 配置信息中红色分量的尺寸
                EGL14.EGL_GREEN_SIZE, 8,    // 颜色缓冲区中绿色位数
                EGL14.EGL_BLUE_SIZE, 8,     //
                EGL14.EGL_ALPHA_SIZE, 8,    //
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,  // 绘制 API 类型
                EGL14.EGL_NONE              // 结尾
        };
        // display 匹配配置信息的个数
        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        // 获取与需求匹配且某 display 支持的配置信息
        // attrib_list：需求信息
        // configs：将与需求信息符合且 display 支持的配置信息存储在这
        if (!EGL14.eglChooseConfig(mEglDisplay, configAttributes, 0, configs, 0,
                configs.length, numConfigs, 0)) {
            throw new RuntimeException(
                    "Unable to choose config EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        //  匹配好的配置信息可能有很多个,不过它们已经排好序,那么我们一般会直接取第一个
        EGLConfig mEglConfig = configs[0];
        // 需求信息
        int[] context_attrib_list = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,    // 指定该 context 是针对 OpenGL ES 的哪个版本。OpenGL ES 2.x 版本
                EGL14.EGL_NONE
        };
        // 根据需求,针对当前的绘制 API 创建一个 rendering context
        mEglContext = EGL14.eglCreateContext(mEglDisplay, mEglConfig, mGlContext, context_attrib_list, 0);
        if (mEglContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException(
                    "Unable to create context EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        // 需求信息
        int[] surface_attrib_list = {
                EGL14.EGL_NONE
        };
        // 根据需求创建一个 on-Screen 的 rendering surface,
        // surface：native window 的 handle
        mEglSurface = EGL14.eglCreateWindowSurface(mEglDisplay, mEglConfig, surface, surface_attrib_list, 0);
        if (mEglSurface == null) {
            throw new RuntimeException(
                    "Unable to create window surface EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        // 将一个指定的 context 绑定到当前的绘制 thread 上,与读、写的 surface 关联上
        if (!EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
            throw new RuntimeException(
                    "Unable to make current EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        screenFilter = new ScreenFilter(context);
        screenFilter.setSize(width, height);
    }

    public void draw(int textureId, long timestamp) {
        screenFilter.onDraw(textureId);
        // 给帧缓冲时间戳
        EGLExt.eglPresentationTimeANDROID(mEglDisplay, mEglSurface, timestamp);
        // 把 surface 中 color buffer 的内容显示出来
        // 通过这个函数,让我们看到了手机上不停变换显示的图片
        if (!EGL14.eglSwapBuffers(mEglDisplay, mEglSurface)) {
            throw new RuntimeException(
                    "Unable to swap buffers EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
    }

    public void release() {
        EGL14.eglDestroySurface(mEglDisplay, mEglSurface);
        // 把该 surface 和 context 设置为非当前使用的资源
        EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEglDisplay, mEglContext);
        // 把整个 thread 释放掉
        EGL14.eglReleaseThread();
        // 将特定 display 对应的 EGL 相关的资源释放
        EGL14.eglTerminate(mEglDisplay);
        screenFilter.release();
    }

}
