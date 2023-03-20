package com.demo.camera;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制三角形
 */
public class TriangleRender implements GLSurfaceView.Renderer {

    private int mProgram;
    private FloatBuffer vertexBuffer;
    // 顶点程序
    private final String vertexShaderCode =
            "attribute vec4 vPosition; " +
                    "void main() {" +
                    "gl_Position = vPosition;" +
                    "}";
    // 片元程序：目的是为了上色
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // 顶点形状
    static float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = {1.0f, 0f, 0f, 1.0f};

    public int loadShader(int type, String shaderCode) {
        // 根据 type 创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        // 将资源加入到着色器中，并编译
        // 将源码传给 GPU
        GLES20.glShaderSource(shader, shaderCode);
        // 编译
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        // 申请底层空间，一个坐标四个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        // 内存重新整理排序，方便使用
        bb.order(ByteOrder.nativeOrder());
        // 将坐标数据转换为 FloatBuffer，用以传入 OpenGL ES 程序
        // 到 GPU 申请内存
        vertexBuffer = bb.asFloatBuffer();
        // 放顶点数组
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        // 创建顶点程序
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        // 创建片元程序
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        // 创建一个空的 OpenGLES 程序
        mProgram = GLES20.glCreateProgram();
        // 将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);
        // 将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        // 链接到着色器程序
        // 链接生成可执行程序
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    private int mPositionHandle;
    private int mColorHandle;

    // 顶点个数
    @Override
    public void onDrawFrame(GL10 gl) {
        // 将程序加入到 OpenGLES2.0 环境
        GLES20.glUseProgram(mProgram);
        // 获取顶点着色器的 vPosition 成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // 准备三角形的坐标数据
        // indx：引用
        // size：顶点个数
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
        // 获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        // 禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
