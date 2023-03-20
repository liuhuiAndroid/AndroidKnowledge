SDL官网：https://www.libsdl.org/ 

SDL文档：http://wiki.libsdl.org/Introduction

#### Linux环境搭建

下载地址：https://github.com/libsdl-org/SDL/releases/tag/release-2.24.0

1. 下载SDL源码库，SDL2-2.0.10.tar.gz

2. 解压，然后依次执行命令

   ```shell
   ./configure
   make
   sudo make install
   ```

3. 如果出现Could not initialize SDL - No available video device (Did you set the DISPLAY variable?)错误。说明系统中没有安装x11的库文件，因此编译出来的SDL库实际上不能用。

   ```shell
   sudo apt-get install libx11-dev
   sudo apt-get install xorg-dev
   
   
   ```

#### SDL子系统

SDL_INIT_TIMER：定时器

SDL_INIT_AUDIO：音频

SDL_INIT_VIDEO：视频

SDL_INIT_EVENTS：事件
