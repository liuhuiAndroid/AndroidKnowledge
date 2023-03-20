// 引入extension
// 当 behavior 为 required 的时候,就是指当前 shader 需要用到某个 extension, 如果指定的 extension 不支持,返回 error。
#extension GL_OES_EGL_image_external : require
// 所有float类型数据的精度是lowp
// varying：从顶点着色器输出的数据
// vec2：两个 float 类型的值保存在了一起
varying vec2 aCoord;
// 采样器
uniform samplerExternalOES vTexture;

// 片元着色器
// 在 main 函数中计算得到的最终结果是像素点的颜色值 gl_FragColor
void main(){
    // Opengl 自带函数，纹理贴图
    vec4 rgba = texture2D(vTexture, aCoord);
    // gl_FragColor：片元颜色值
    gl_FragColor=vec4(rgba.r, rgba.g, rgba.b, rgba.a);
}