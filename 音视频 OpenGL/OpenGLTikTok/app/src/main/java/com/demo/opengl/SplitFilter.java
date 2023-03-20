package com.demo.opengl;

import android.content.Context;

/**
 * 分屏特效
 */
public class SplitFilter extends AbstractFboFilter {

    public SplitFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.split3_screen);
    }

    public int onDraw(int texture) {
        super.onDraw(texture);
        return frameTextures[0];
    }
}