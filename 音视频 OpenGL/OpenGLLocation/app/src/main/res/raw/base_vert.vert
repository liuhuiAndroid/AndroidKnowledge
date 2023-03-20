attribute vec4 position;
attribute vec4 inputTextureCoordinate;
varying vec2 textureCoordinate;

void main(){
    // 内置变量： 把坐标点赋值给gl_position 就Ok了。  4 个元素  gl_Position  世界坐标系 为基础
    gl_Position = position;
    textureCoordinate = inputTextureCoordinate.xy;
}