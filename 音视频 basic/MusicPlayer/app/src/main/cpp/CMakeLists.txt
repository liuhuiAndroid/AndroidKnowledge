#声明cmake最低版本
cmake_minimum_required(VERSION 3.18.1)

#导入头文件
include_directories(include)

project("player")

add_library(
        player
        SHARED
        native-lib.cpp)

#设置系统环境变量
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")

find_library(
        log-lib
        log)

target_link_libraries(
        player
        ${log-lib}
        android
        #导入库文件，变成系统的环境变量
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        swresample
        swscale)