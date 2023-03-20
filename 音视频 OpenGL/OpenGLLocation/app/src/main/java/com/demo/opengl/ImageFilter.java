package com.demo.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ImageFilter {

    static final float COORD1[] = {
            -1.0f, -1.0f,

            1.0f, 1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
    };
    static final float BOOK_COORD1[] = {
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
    };
    static final float TEXTURE_COORD1[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    //世界坐标系
    static final float COORD_REVERSE[] = {
            -1.0f, 1.0f,
            1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
    };

    //    纹理坐标系
    static final float TEXTURE_COORD_REVERSE[] = {
            1.0f, 0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
    };

    private String mVertexShader;
    private String mFragmentShader;

    private FloatBuffer mPositionBuffer;
    private FloatBuffer mTextureCubeBuffer;

    protected int mProgId;
    protected int mPosition;
    protected int inputTextureBuffer;
    protected int mInputTexture;

    public ImageFilter(Context context) {
        this(OpenGLUtils.readRawTextFile(context, R.raw.base_vert), OpenGLUtils.readRawTextFile(context, R.raw.base_frag));
    }

    public ImageFilter(String vertexShader, String fragmentShader) {
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
    }

    public void loadVertex() {
        float[] coord = COORD1;
        float[] texture_coord = BOOK_COORD1;
        mPositionBuffer = ByteBuffer.allocateDirect(coord.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mPositionBuffer.put(coord).position(0);
        mTextureCubeBuffer = ByteBuffer.allocateDirect(texture_coord.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureCubeBuffer.put(texture_coord).position(0);
    }

    public void initShader() {
        mProgId = OpenGLUtils.loadProgram(mVertexShader, mFragmentShader);
        mPosition = GLES20.glGetAttribLocation(mProgId, "position");
        mInputTexture = GLES20.glGetUniformLocation(mProgId, "inputImageTexture");
        inputTextureBuffer = GLES20.glGetAttribLocation(mProgId, "inputTextureCoordinate");
    }

    public int init(Bitmap bitmap) {
        loadVertex();
        initShader();
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        return initTexture(bitmap);
    }

    private int initTexture(Bitmap bitmap) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        return textures[0];
    }

    public void drawFrame(int glTextureId) {
        GLES20.glUseProgram(mProgId);
        mPositionBuffer.position(0);
        GLES20.glVertexAttribPointer(mPosition, 2, GLES20.GL_FLOAT, false, 0, mPositionBuffer);
        GLES20.glEnableVertexAttribArray(mPosition);

        mTextureCubeBuffer.position(0);
        GLES20.glVertexAttribPointer(inputTextureBuffer, 2, GLES20.GL_FLOAT, false, 0,
                mTextureCubeBuffer);
        GLES20.glEnableVertexAttribArray(inputTextureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTextureId);
        GLES20.glUniform1i(mInputTexture, 0);

        // 进行渲染
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(mPosition);
        GLES20.glDisableVertexAttribArray(inputTextureBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glDisable(GLES20.GL_BLEND);
    }

}
