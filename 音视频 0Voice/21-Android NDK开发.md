#### Makefile

Makefile 其实只是⼀个指示 make 程序（后⾯简称 make 或有时称之为 make 命令）如何为我们⼯作的 命令⽂件，我们说 Makefile 其实是在说 make，这⼀点要有很清晰的认识。⽽对于我们的项⽬来说， Makefile 是指软件项⽬的编译环境。

案例：https://github.com/wangbojing/NtyCo

书籍：驾驭Makefile

```shell
# 语法
targets : prerequisites
		command
```

targets：可以有多个，默认的targets是第一个

command：需要以tab空格开始

#### CMake：能用就用，本质是基于Makefile

案例：https://github.com/ZLMediaKit/ZLToolKit

案例：https://github.com/ZLMediaKit/ZLMediaKit

```shell
# 第一种编译方式，生成的工程文件就在当前工程里面，不推荐
cmake .
# 第二种编译方式
mkdir build
cd build
cmake ..
```

#### autotools

#### GDB调试器

官方参考文档：http://www.gnu.org/software/gdb/documentation/



#### JNI（Java Native Interface）Java本地接⼝

#### Android NDK 即 Native Development Kit

库⽂件类型：静态库.a 、动态库.so 、动态库.dll 

so 为共享库,是shared object

[为何大厂APP如微信、支付宝等只适配了armeabi-v7a/armeabi？](https://mp.weixin.qq.com/s/jnZpgaRFQT5ULk9tHWMAGg)

32位处理器与64位处理器区别：64位和32位是指CPU的通⽤寄存器数据宽度。 

.so库是什么，NDK编译出来的动态链接库。

⼀些重要的加密算法或者核⼼协议⼀般都⽤c/c++写然后给java调⽤。这样可以避免反编译后查看到应⽤的源码。

[Android：So库适配简单总结](https://demon.blog.csdn.net/article/details/108149890)



build.gradle配置C++标准

```groovy
externalNativeBuild { 
	cmake { 
		cppFlags ”-fexceptions -std=c++14"
    // C++标准库默认是静态的，配置C++标准库位动态
		arguments '-DANDROID_STL=c++_shared'
	}
}
```

配置库⽂件的输出⽬录

CMAKE_ARCHIVE_OUTPUT_DIRECTORY ：默认存放静态库的⽂件夹位置；

CMAKE_LIBRARY_OUTPUT_DIRECTORY ：默认存放动态库的⽂件夹位置；

LIBRARY_OUTPUT_PATH ：默认存放库⽂件的位置，如果产⽣的是静态库并且没有指定 CMAKE_ARCHIVE_OUTPUT_DIRECTORY 则存放在该⽬录下，动态库也类似；

- 配置库⽣成路径# CMAKE_CURRENT_SOURCE_DIR是指 cmake库的源路径，通常是 build/.../cmake/

-  /../jniLibs/是指与CMakeList.txt所在⽬录的同级⽬录：jniLibs (如果没有会新建) 

- ANDROID_ABI ⽣成库⽂件时，采⽤gradle配置的ABI策略（即：⽣成哪些平台对应的库⽂件） 

  set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}) 

  or? set( LIBRARY_OUTPUT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

1. 默认/app/build/intermediates/cmake/.. 

2. 安卓项⽬默认的JNI库⽬录：jniLibs 

3. 可选配置本地jni库的⽬录： 

   ```groovy
   sourceSets { 
     main { 
       jniLibs.srcDirs = ['src/main/libs'] 
     } 
   }
   ```

 

⽣成指定cpu平台对应的so库⽂件

```groovy
externalNativeBuild {
	cmake {
    abiFilters "arm64-v8a”
  }
}

// 或者
ndk{
  abiFilters "arm64-v8a”
}
```



#### JavaVM 和 Env 的关系

1. JavaVm是虚拟机在jni层的代表，⼀个进程只有⼀个JavaVm，所有线程共⽤⼀个JavaVM。
2. JNIEnv 是⼀个线程相关的结构体，它代表了java的运⾏环境 。每⼀个线程都会有⼀个，不同的线程中不能相互调⽤，每个JNIEnv都是线程专有的。 jni中可以拥有很多个JNIEnv，可以使⽤它来进⾏java层和native层的调⽤。 
3. JNIEnv 是⼀个指针，指向⼀个线程相关的结构，线程相关结构指向了JNI函数指针数组。这个数组⾥⾯定义了⼤量的JNI函数指针。 
4. 在同⼀个线程中，多次调⽤JNI层⽅法，传⼊的JNIEnv都是相同的。 
5.  在java层定义的本地⽅法，可以在不同的线程中调⽤，因此是可以接受不同的JNIEnv。 



#### 编译FFmpeg

1. download ffmpeg 4.2.1
2. download NDKr22
3. 编译fdk-aac
4. 编译x264
5. 编译libmp3lame
6. 编译FFmpeg



#### 编译[ijkplayer](https://github.com/bilibili/ijkplayer)

1. 安装jdk
2. 下载sdk
3. 配置ndk
4. 下载ijkplayer
5. 编译ffmpeg
6. 编译ijkplayer
