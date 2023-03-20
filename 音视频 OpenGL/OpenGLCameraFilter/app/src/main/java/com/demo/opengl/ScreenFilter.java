package com.demo.opengl;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 滤镜
 */
public class ScreenFilter {

    // 顶点着色器
    // 片元着色器
    private int program;
    //句柄  gpu中  vPosition
    private int vPosition;
    FloatBuffer textureBuffer; // 纹理坐标
    private int vCoord;
    private int vTexture;
    private int vMatrix;
    private int mWidth;
    private int mHeight;
    private float[] mtx;
    // gpu 内创建顶点缓冲区，提供给 CPU
    FloatBuffer vertexBuffer;

    // CPU 内的坐标
    float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    // 纹理坐标
    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    public ScreenFilter(Context context) {
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(TEXTURE);

        // 读取顶点着色器程序
        String vertexShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_vert);
        String fragShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag1);
        // 将 vertexShader 和 fragShader 先编译，再链接，再运行程序
        program = OpenGLUtils.loadProgram(vertexShader, fragShader);

        // 获取 GPU 中的 vPosition
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        // 接收纹理坐标，接收采样器采样图片的坐标
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        // 采样点的坐标
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
        // 变换矩阵，需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    // 摄像头数据开始渲染
    public void onDraw(int texture) {
        // View 的大小，报告范围
        GLES20.glViewport(0, 0, mWidth, mHeight);
        // 使用程序
        GLES20.glUseProgram(program);
        // 从索引位0的地方读
        vertexBuffer.position(0);
        // index 指定要修改的通用顶点属性的索引。
        // size 指定每个通用顶点属性的组件数。
        // type 指定数组中每个组件的数据类型。
        // 接受符号常量GL_FLOAT，GL_BYTE，GL_UNSIGNED_BYTE，GL_SHORT，GL_UNSIGNED_SHORT或GL_FIXED。 初始值为GL_FLOAT。
        // normalized 指定在访问定点数据值时是应将其标准化（GL_TRUE）还是直接转换为定点值（GL_FALSE）。
        GLES20.glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, vertexBuffer);
        // 生效
        GLES20.glEnableVertexAttribArray(vPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        // CPU传数据到GPU，默认情况下着色器无法读取到这个数据。 需要我们启用一下才可以读取
        GLES20.glEnableVertexAttribArray(vCoord);
        // 激活图层
        GLES20.glActiveTexture(GL_TEXTURE0);

        // 生成一个采样
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
        // 通知画画，
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

}
