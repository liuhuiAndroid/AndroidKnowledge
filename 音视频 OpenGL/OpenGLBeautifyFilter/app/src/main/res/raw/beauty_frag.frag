precision mediump float;
//当前要采集像素的点  坐标 x  y
varying mediump vec2 aCoord;
//采样
uniform sampler2D vTexture;
vec2 blurCoordinates[20];
uniform int width;
uniform int height;
void main(){
    // 高斯模糊
    vec2 singleStepOffset=vec2(1.0/float(width), 1.0/float(height));
    //    [500,500]        [500,490 ]             [0.5,0.5]            //    [0.5,0.5] + [1/1000,-10/1000]
    blurCoordinates[0] = aCoord.xy + singleStepOffset * vec2(0.0, -10.0);
    //      [500,510]
    blurCoordinates[1] = aCoord.xy + singleStepOffset * vec2(0.0, 10.0);
    //[490, 500]
    blurCoordinates[2] = aCoord.xy + singleStepOffset * vec2(-10.0, 0.0);
    //[510, 500]
    blurCoordinates[3] = aCoord.xy + singleStepOffset * vec2(10.0, 0.0);
    //GPUIMage   magiccaerma
    blurCoordinates[4] = aCoord.xy + singleStepOffset * vec2(5.0, -8.0);
    blurCoordinates[5] = aCoord.xy + singleStepOffset * vec2(5.0, 8.0);
    blurCoordinates[6] = aCoord.xy + singleStepOffset * vec2(-5.0, 8.0);
    blurCoordinates[7] = aCoord.xy + singleStepOffset * vec2(-5.0, -8.0);
    blurCoordinates[8] = aCoord.xy + singleStepOffset * vec2(8.0, -5.0);
    blurCoordinates[9] = aCoord.xy + singleStepOffset * vec2(8.0, 5.0);
    blurCoordinates[10] = aCoord.xy + singleStepOffset * vec2(-8.0, 5.0);
    blurCoordinates[11] = aCoord.xy + singleStepOffset * vec2(-8.0, -5.0);
    blurCoordinates[12] = aCoord.xy + singleStepOffset * vec2(0.0, -6.0);
    blurCoordinates[13] = aCoord.xy + singleStepOffset * vec2(0.0, 6.0);
    blurCoordinates[14] = aCoord.xy + singleStepOffset * vec2(6.0, 0.0);
    blurCoordinates[15] = aCoord.xy + singleStepOffset * vec2(-6.0, 0.0);
    blurCoordinates[16] = aCoord.xy + singleStepOffset * vec2(-4.0, -4.0);
    blurCoordinates[17] = aCoord.xy + singleStepOffset * vec2(-4.0, 4.0);
    blurCoordinates[18] = aCoord.xy + singleStepOffset * vec2(4.0, -4.0);
    blurCoordinates[19] = aCoord.xy + singleStepOffset * vec2(4.0, 4.0);

    vec4 currentColor=texture2D(vTexture, aCoord);
    vec3 rgb=currentColor.rgb;
    for (int i = 0; i < 20; i++) {
        rgb+=texture2D(vTexture, blurCoordinates[i].xy).rgb;
    }
    // 算平均值
    vec4 blur = vec4(rgb*1.0/21.0, currentColor.a);
    // 一个完整的图片相减
    vec4 highPassColor=currentColor-blur;


    //    高斯模糊做完了  没有做人脸识别  算法写的号
    //    还原鼻子嘴巴   细节的地方  手扣
    //    两个画面90%   模糊的地方
    //     ps    美颜     美颜 蓝通道
    //    2.0 * highPassColor.r * highPassColor.r * 24.0       1  出现大于1     1   不可以 2     0  - 1
    //     0 -1  取中间的值  r抛物线
    highPassColor.r=clamp(2.0 * highPassColor.r * highPassColor.r * 24.0, 0.0, 1.0);

    highPassColor.g = clamp(2.0 * highPassColor.g * highPassColor.g * 24.0, 0.0, 1.0);
    highPassColor.b = clamp(2.0 * highPassColor.b * highPassColor.b * 24.0, 0.0, 1.0);

    // 高反差
    vec4 highPassBlur=vec4(highPassColor.rgb, 1.0);
    //    蓝色通道  作为    参考  叠加
    //    两个颜色  原图颜色     高斯模糊的颜色
    float b =min(currentColor.b, blur.b);
    //    叠加算法
    float value = clamp((b - 0.2) * 5.0, 0.0, 1.0);
    //    取rgb的最大值      蓝色的值取出来         保留细节
    float maxChannelColor = max(max(highPassBlur.r, highPassBlur.g), highPassBlur.b);
    // 磨皮程度
    float intensity = 1.0;// 0.0 - 1.0f 再大会很模糊
    // 细节的地方     不融合      痘印的地方  使劲融合
    // currentIntensity    细节的地方     值越小        黑色的地方 值 比较大
    float currentIntensity = (1.0 - maxChannelColor / (maxChannelColor + 0.2)) * value * intensity;
    //    opencv

    //    线性融合
    //    x⋅(1−a)+y⋅a    a=0  保留  原图     1  高斯模糊图  2
    //[255,0 , 0]   ⋅(1−a)   +       [56,0 , 0] *a
    //模糊
    vec3 r =mix(currentColor.rgb, blur.rgb, currentIntensity);
    gl_FragColor=vec4(r, 1.0);
    //
    //    得到周围20个像素   算法
}