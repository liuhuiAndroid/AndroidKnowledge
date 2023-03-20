package com.demo.opengl;

import android.content.Context;
import android.opengl.GLES20;

public class AbstractFboFilter extends AbstractFilter {

    // 保存被创建的 framebuffer object name
    private int[] frameBuffer;
    int[] frameTextures;

    public AbstractFboFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }

    // 实例化 FBO，让摄像头的数据先渲染到 FBO
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        releaseFrame();
        frameBuffer = new int[1];
        // n：生成 n 个 framebuffer object name
        // 创建 application-created framebuffer 的 name,然后再通过 API glBindFramebuffer 创建一个 FBO
        GLES20.glGenFramebuffers(1, frameBuffer, 0);

        // 生成一个纹理
        // 生成一个图层
        frameTextures = new int[1];
        // 生成 frameTextures.length 个 texture object name
        // 第二个输入参数用于保存被创建的 texture object name
        GLES20.glGenTextures(frameTextures.length, frameTextures, 0);
        // 配置纹理
        for (int frameTexture : frameTextures) {
            // 把这个 texture 与 GL_TEXTURE_2D 绑定
            // 生成一个 2D 的 texture object，然后把这个 texture object 放入刚才 enable 的那个纹理单元中
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTexture);
            // 对纹理设置参数：放大过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_NEAREST);
            // 对纹理设置参数：缩小过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            // gpu 操作完了
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }

        // 绑定操作
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTextures[0]);
        // 把准备好的数据，从 CPU 端保存的数据传递给 GPU 端，保存在指定的 texture object 中
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        // 创建一个 FBO，framebuffer object
        // 第一个参数必须是：GLES20.GL_FRAMEBUFFER，指定 framebuffer object 的类型
        // 第二个输入参数为 glGenFramebuffers 得到的 framebuffer object name
        // 之后指定某个 framebuffer object name 的时候,也就相当于指定这个 framebuffer object
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);

        // 真正发生绑定
        // 将 texture attach 到 FBO 上
        // 第一个参数必须是：GLES20.GL_FRAMEBUFFER，指定 framebuffer object 的类型
        // 第二个参数必须是FBO的3个挂载点之一，GL_COLOR_ATTACHMENT0, GL_DEPTH_ATTACHMENT or GL_STENCIL_ATTACHMENT
        // 如果第四个参数为一个非0的，而且指向一个2D的texture，那么第三个参数必须为GL_TEXTURE_2D
        // 第五个参数 level 指的是该 texture 的 mipmap，在这里必须为0
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, frameTextures[0], 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        // opengl
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void releaseFrame() {
        if (frameTextures != null) {
            // 把 texture object name 删除
            GLES20.glDeleteTextures(1, frameTextures, 0);
            frameTextures = null;
        }
        if (frameBuffer != null) {
            // 当某个 FBO 不再被需要的时候,则可以通过 glDeleteFramebuffers 这个 API 把 framebuffer object name 删除。
            GLES20.glDeleteFramebuffers(1, frameBuffer, 0);
        }
    }

    @Override
    public int onDraw(int texture) {
        // 数据渲染到 FBO 中
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        super.onDraw(texture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return frameTextures[0];
    }

}
