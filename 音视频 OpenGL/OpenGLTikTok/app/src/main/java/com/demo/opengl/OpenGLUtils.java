package com.demo.opengl;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OpenGLUtils {

    private static final String TAG = OpenGLUtils.class.getSimpleName();

    public static final float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    public static final float[] TEXURE = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public static final float[] TEXTURE_NO_ROTATION = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public static void glGenTextures(int[] textures) {
        // 生成 frameTextures.length 个texture object name
        // 第二个输入参数用于保存被创建的texture object name
        GLES20.glGenTextures(textures.length, textures, 0);
        for (int i = 0; i < textures.length; i++) {
            // 创建一个 texture object
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);

            /*设置纹理缩放过滤*/
            // GL_NEAREST: 使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            // GL_LINEAR:  使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            // 后者速度较慢，但视觉效果好
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);//放大过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);//缩小过滤

            /**
             * 可选：设置纹理环绕方向
             */
            //纹理坐标的范围是0-1。超出这一范围的坐标将被OpenGL根据GL_TEXTURE_WRAP参数的值进行处理
            //GL_TEXTURE_WRAP_S, GL_TEXTURE_WRAP_T 分别为x，y方向。
            //GL_REPEAT:平铺
            //GL_MIRRORED_REPEAT: 纹理坐标是奇数时使用镜像平铺
            //GL_CLAMP_TO_EDGE: 坐标超出部分被截取成0、1，边缘拉伸
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
//                    GLES20.GL_CLAMP_TO_EDGE);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
//                    GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    public static String readRawTextFile(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int loadProgram(String vSource, String fSource) {
        // 顶点着色器
        // 创建一个 shader 的 handle，参数为 shader type
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vShader == 0) {
            throw new RuntimeException(
                    "Unable to create shader: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        // 往一个 shader 的 handle 中传递 shader source
        GLES20.glShaderSource(vShader, vSource);
        // 把一个已经包含 shader source 内容的 shader 发给 GPU 进行编译
        GLES20.glCompileShader(vShader);
        // 查看配置是否成功
        int[] status = new int[1];
        GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException(
                    "load vertex shader:" + GLES20.glGetShaderInfoLog(vShader));
        }

        // 片元着色器
        // 创建一个 shader 的 handle，参数为 shader type
        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fShader == 0) {
            throw new RuntimeException(
                    "Unable to create shader: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        // 往一个 shader 的 handle 中传递 shader source
        GLES20.glShaderSource(fShader, fSource);
        // 把一个已经包含 shader source 内容的 shader 发给 GPU 进行编译
        GLES20.glCompileShader(fShader);
        // 查看配置是否成功
        GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException(
                    "load fragment shader:" + GLES20.glGetShaderInfoLog(vShader));
        }

        // 创建一个 program object
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            throw new RuntimeException(
                    "Unable to create program: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        // 把一个 shader 关联到该 program object 上
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        // 把 program 封住,使得其中的 vertex shader 和 fragment shader 组成一对,形成一个新的完整的个体
        GLES20.glLinkProgram(program);

        //获得状态
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("link program:" + GLES20.glGetProgramInfoLog(program));
        }
        // 把 shader 删除
        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
        return program;
    }


    public static void copyAssets2SdCard(Context context, String src, String dst) {
        try {
            File file = new File(dst);
            if (!file.exists()) {
                InputStream is = context.getAssets().open(src);
                FileOutputStream fos = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[2048];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
