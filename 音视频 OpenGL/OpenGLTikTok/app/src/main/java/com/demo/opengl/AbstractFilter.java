package com.demo.opengl;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class AbstractFilter {

    //    顶点着色器
//    片元着色器
    public int program;
    //句柄  gpu中  vPosition
    private int vPosition;
    FloatBuffer textureBuffer; // 纹理坐标
    private int vCoord;
    private int vTexture;
    private int vMatrix;
    private int mWidth;
    private int mHeight;
    private float[] mtx;
    //gpu顶点缓冲区
    FloatBuffer vertexBuffer; //顶点坐标缓存区
    float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };
    //定死的值 1  不是 2
    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    public AbstractFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(TEXTURE);

//
        String vertexSharder = OpenGLUtils.readRawTextFile(context, vertexShaderId);
//  先编译    再链接   再运行  程序
        String fragSharder = OpenGLUtils.readRawTextFile(context, fragmentShaderId);
//cpu 1   没有用  索引     program gpu
        program = OpenGLUtils.loadProgram(vertexSharder, fragSharder);

        // glGetAttribLocation：获取指定 VS 中某个 attribute 的位置
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        // 接收纹理坐标，接收采样器采样图片的坐标
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        // 采样点的坐标
        // glGetUniformLocation：获取 uniform 的 location 的
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;

    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    //摄像头数据  渲染   摄像  开始渲染
    public int onDraw(int texture) {
        // 设定绘制区域
        GLES20.glViewport(0, 0, mWidth, mHeight);
//        使用程序
        GLES20.glUseProgram(program);
//        从索引位0的地方读
        vertexBuffer.position(0);
        //     index   指定要修改的通用顶点属性的索引。
//     size  指定每个通用顶点属性的组件数。
        //        type  指定数组中每个组件的数据类型。
        //        接受符号常量GL_FLOAT  GL_BYTE，GL_UNSIGNED_BYTE，GL_SHORT，GL_UNSIGNED_SHORT或GL_FIXED。 初始值为GL_FLOAT。
//      normalized    指定在访问定点数据值时是应将其标准化（GL_TRUE）还是直接转换为定点值（GL_FALSE）。
        // 从 OpenGL ES 向 VS 中传输数据
        GLES20.glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, vertexBuffer);
        // 对这个 attribute 进行 enable
        GLES20.glEnableVertexAttribArray(vPosition);


        textureBuffer.position(0);
        // 从 OpenGL ES 向 VS 中传输数据
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        // 对这个 attribute 进行 enable
        GLES20.glEnableVertexAttribArray(vCoord);

//        形状就确定了

//         32  数据
//gpu    获取读取    //使用第几个图层
        GLES20.glActiveTexture(GL_TEXTURE0);

        //生成一个采样器
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(vTexture, 0);
//        模板方法
        beforeDraw();
        // 告知GPU，使用哪几个顶点进行绘制，而且把用于绘制的点，如何进行图元装配
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return texture;
    }

    public void beforeDraw() {
    }

    public void release() {
        GLES20.glDeleteProgram(program);
    }
}
