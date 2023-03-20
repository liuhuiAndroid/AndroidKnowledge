package com.demo.opengl;

import android.content.Context;
import android.opengl.GLES20;

/**
 * 美颜
 */
public class BeautyFilter extends AbstractFboFilter {

    private int width;
    private int height;

    public BeautyFilter(Context context) {
        // super(context, R.raw.base_vert, R.raw.beauty_frag);
        super(context, R.raw.base_vert, R.raw.beauty_fragment2);
        width = GLES20.glGetUniformLocation(program, "width");
        height = GLES20.glGetUniformLocation(program, "height");
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();
        GLES20.glUniform1i(width, mWidth);
        GLES20.glUniform1i(height, mHeight);
    }

}
