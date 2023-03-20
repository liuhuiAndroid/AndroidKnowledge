#声明cmake最低版本
cmake_minimum_required(VERSION 3.4.1)

add_subdirectory(librtmp)

add_library(
        bilibili
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        bilibili
        ${log-lib}
        rtmp)