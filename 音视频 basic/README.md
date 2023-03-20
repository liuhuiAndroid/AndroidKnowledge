FFmpeg 命令模式代码太老了，可参考：https://github.com/xch168/VideoEditor

ITU-T：H261、H262、H264 -> H.264(MPEG-4 AVC)(AVC)

ISO：Mepg1、Mepg2、Mepg4 -> H.264(MPEG-4 AVC)(AVC)

Google：VP8、VP9、

```java
MediaCodec.createEncoderByType("avc")
```

短视频GOP越长越好，直播GOP越短越好

PTS：音视频播放时间，单位纳秒ns

一帧图像=多个Slice=多个（Slice片头+片数据）

一个Slice=Slice片头+片数据=NALU单元

一个片数据=多个宏块

帧与帧之间间隔：0x00000001

SPS配置信息：宽高数据、编码方式、编码等级、分辨率等

PPS记录宽高数据

帧类型：标识是SPS、PPS、I帧、P帧 

67 后面5位存的是帧类型，与11111相与=7，查表代表序列参数集=SPS

68 后面5位存的是帧类型，与11111相与=8，查表代表图像参数集=PPS

65 后面5位存的是帧类型，与11111相与=5，查表代表IDR图片中的片=I帧

H.264使用的哥伦布编码，可变长编码

SPS+PPS总长度28-34字节

可以使用 Elecard HEVC Analyzer 分析H.265

工具：H264Visa

rgb通道数要求高，占内存大，推荐yuv；yuv可以和rgb相互转换；做视频都是yuv，图片才用rgb

P帧和B帧是没有SPS和PPS的，但是有分隔符；P帧主要存运动矢量数据和差异数据；B帧主要存运动矢量

MediaPlayer 傻瓜式

MediaCodec

MediaExtractor 解析完整视频

哥伦布编码，优点，缺点

-----------------------------------------------------------

H264最大支持宏块16*16

H265比H264高分辨率会小很多，H265的优势

H.265压缩主要是靠宏块，在细节表达上会更清楚；主要是压缩B帧和P帧，I帧比H.264大

```shell
ffmpeg -i h265.mkv -vcodec hevc output.h265
```

TODO：录屏输出H.264视频的代码修改下就可以输出H.265

H.265第一行 VPS + SPS +PPS

H.265 收费

--------------------------

DSP芯片

Android摄像头横着摆放的，预览前需要旋转；

从 Camera.PreviewCallback 接口回调方法 onPreviewFrame(byte[] data, Camera camera) 回调的 data 数据，为 NV21 图像数据，而Android 手机识别的是 NV12 图像数据，所以需要将 NV21 转化为 NV12 图像数据。

安装 Elecard HEVC Analyzer

----------------------------

H.265投屏，在每一个I帧前都添加VPS+SPS+PPS

-----------------------------------------

H.265视频通话：MediaProjection 换成 摄像头

花屏可能是宽高没设置正确

摄像头 ImageFormat.NV21 -> MediaCodec需要的 ImageFormat.NV12

ImageFormat.NV12 又名为 YUV420

摄像头保存H.265数据保存成文件用VLC播放

-------------------------------------

TextureView 比 SurfaceView 效率高 ？？GLSurfaceView

播放顺序由PTS决定

----------------------------

Nginx RTMP服务器搭建

```shell
ssh root@101.132.108.8
cd ~ & mkdir rtmp & cd rtmp
wget http://nginx.org/download/nginx-1.15.3.tar.gz
wget https://codeload.github.com/arut/nginx-rtmp-module/tar.gz/v1.2.1
tar zxvf v1.2.1
tar zxvf nginx-1.15.3.tar.gz
cd nginx-1.15.3
yum -y install git
# yum -y install pcre*
yum -y install pcre-devel
# yum -y install openssl*
yum -y install openssl openssl-devel
./configure --prefix=./bin --add-module=../nginx-rtmp-module-1.2.1
make install
# nginx.conf见下面
vi bin/conf/nginx.conf
cd /root/rtmp/nginx-1.15.3
# 运行nginx
./bin/sbin/nginx -c conf/nginx.conf
# 检查nginx是否重启成功
./bin/sbin/nginx -t
# 查看nginx进程
ps -ef | grep nginx
# 重启nginx
./bin/sbin/nginx -s reload
# 阿里云配置规则允许目的8080/8080，源0.0.0.0/0
# http://101.132.108.8:8080/stat 网页查看控制台
# http://101.132.108.8:8080 查看播放页面
# VLC播放直播流：
# rtmp://101.132.108.8:1935/live/mylive
ffplay -i rtmp://101.132.108.8:1935/live/mylive
```

```shell
user root;
worker_processes 1;
error_log /root/rtmp/nginx-1.15.3/bin/logs/error.log debug;
events {
		worker_connections  1024;
}
rtmp {
  server {
      listen 1935;
      application live {
          live on;
      }
  }
}
http{
    server {
        listen 8080;
        server_name localhost;
        location /stat.xsl {
            root /root/rtmp/nginx-rtmp-module-1.2.1;
        }
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }
        location /control {
            rtmp_control all;
        }
        location /rtmp-publisher {
            root /root/rtmp/nginx-rtmp-module-1.2.1/test;
        }
        location / {
            root /root/rtmp/nginx-rtmp-module-1.2.1/test/www;
        }
    }
}
```

x264交叉编译

```shell
# 下载NDK
wget  https://dl.google.com/android/repository/android-ndk-r21d-linux-x86_64.zip
yum install zip unzip -y
unzip android-ndk-r21d-linux-x86_64.zip
# 添加NDK系统环境变量
vim /etc/profile

export NDK=/root/android-ndk-r21d
export PATH=$PATH:/root/android-ndk-r21d
export SYSROOT="$NDK/toolchains/llvm/prebuilt/linux-x86_64/sysroot/"
export ANDROID_GCC="$NDK/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android24-clang"
# 生效NDK系统环境变量
source /etc/profile
echo $NDK

ssh root@101.132.108.8
git clone https://code.videolan.org/videolan/x264.git
cd x264
./configure --help
vi build.sh
chmod 777 build.sh
./build.sh
```

```shell
#!/bin/bash
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/linux-x86_64
export API=21
#armeabi-v7a
PREFIX=./armeabi-v7a
HOST=armv7a-linux-android
export TARGET=armv7a-linux-androideabi
#最新的Android不支持gcc改为clang
export CC=$TOOLCHAIN/bin/$TARGET$API-clang
export CXX=$TOOLCHAIN/bin/$TARGET$API-clang++
export CROSS_PREFIX=$TOOLCHAIN/bin/arm-linux-androideabi-

function build
{
  ./configure \
    --prefix=$PREFIX \
    --disable-cli \
    --enable-shared \
    --enable-pic \
    --host=$HOST \
    --cross-prefix=$CROSS_PREFIX \
    --sysroot=$NDK/toolchains/llvm/prebuilt/linux-x86_64/sysroot \

  make clean
  make -j8
  make install
}
build
```

如需要编译arm64-v8a架构版本，则修改以下变量：

```shell
#arm64-v8a
PREFIX=./android/arm64-v8a
HOST=aarch64-linux-android
export TARGET=aarch64-linux-android
export CC=$TOOLCHAIN/bin/$TARGET$API-clang
export CXX=$TOOLCHAIN/bin/$TARGET$API-clang++
CROSS_PREFIX=$TOOLCHAIN/bin/aarch64-linux-android-
```

faac交叉编译

```shell
ssh root@101.132.108.8
# 下载faac
wget https://nchc.dl.sourceforge.net/project/faac/faac-src/faac-1.29/faac-1.29.9.2.tar.gz
tar zxvf faac-1.29.9.2.tar.gz
cd faac-1.29.9.2
vim build_faac.sh
chmod 777 build_faac.sh
./build_faac.sh
```

```shell
#!/bin/bash 
PREFIX=/root/faac-1.29.9.2/armeabi-v7a
TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/linux-x86_64 

# gcc 编译器参数
export CFLAGS="--target=armv7-none-linux-androideabi21 --gcc-toolchain=$TOOLCHAIN -g -DANDROID -fdata-sections -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -fno-addrsig -march=armv7-a -mthumb -Wa,--noexecstack -Wformat -Werror=format-security -O0 -DNDEBUG -fPIC" 

export CC=$TOOLCHAIN/bin/armv7a-linux-androideabi21-clang 
export CXX=$TOOLCHAIN/bin/armv7a-linux-androideabi21-clang++ 

./configure --prefix=$PREFIX --with-pic=yes --host=arm-linux-androideabi --enable-shared=no --enable-static=yes -with-sysroot=$TOOLCHAIN/sy

make clean
make -j8
make install
```

