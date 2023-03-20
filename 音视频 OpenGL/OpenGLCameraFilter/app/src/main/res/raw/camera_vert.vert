// 插件：GLSL Support
// 把顶点坐标给这个变量，确定要画画的形状
// GPU 内定义一个变量：vec4 = 4个字节的向量
attribute vec4 vPosition;
// 接收纹理坐标，接收采样器采样图片的坐标
attribute vec4 vCoord;
// 矩阵变换
// mat4 用于保存 4*4 个 float 类型的变量,也就是保存了 16 个 float 类型的变量
uniform mat4 vMatrix;
// 传给片元着色器的变量
varying vec2 aCoord;

// 顶点着色器
// 在 Vertex shader 中 main 函数中最终得到的结果是顶点坐标 gl_Position
void main(){
    // gl_Position 是内置变量：顶点坐标
    gl_Position=vPosition;
    aCoord=(vMatrix * vCoord).xy;
}
