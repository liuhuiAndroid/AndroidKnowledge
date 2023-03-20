# Sets the minimum version of CMake required to build the native library.
# 设置cmake版本号
cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

set(jnilibs ${CMAKE_SOURCE_DIR}/../jniLibs)
set(libname learn-ffmpeg)

include_directories(
        include
        ${CMAKE_SOURCE_DIR}/util
)

link_directories(
        ${jnilibs}/${ANDROID_ABI})

file(GLOB src-files
        ${CMAKE_SOURCE_DIR}/*.cpp)

#add_library可以根据c文件产生动态库
add_library(
        # Sets the name of the library.
        # 库的名字
        ${libname}
        # Sets the library as a shared library.
        # 动态还是静态
        SHARED
        # Provides a relative path to your source file(s).
        # 由哪个文件产生动态库
        ${src-files}
)
# find_library查找系统的库
#find_library(log-lib log)
#find_library(android-lib android)

set(third-party-libs
        avformat
        avcodec
        avfilter
        swresample
        swscale
        avutil
        )

set(native-libs
        android
        EGL
        GLESv3
        OpenSLES
        log
        m
        z
        )

# 链接库
target_link_libraries(
        # Specifies the target library.
        ${libname}

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${third-party-libs}
        ${native-libs}
)
