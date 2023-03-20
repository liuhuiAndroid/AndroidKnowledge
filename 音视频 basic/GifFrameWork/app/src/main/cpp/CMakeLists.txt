cmake_minimum_required(VERSION 3.18.1)

project("gif")

add_library(
        gif
        SHARED
        native-lib.cpp
        dgif_lib.c
        gifalloc.c
        demo.cpp)

find_library(
        log-lib
        log)

# 引入系统库
find_library(
        jnigraphics-lib
        jnigraphics)

target_link_libraries(
        gif
        ${log-lib}
        ${jnigraphics-lib})