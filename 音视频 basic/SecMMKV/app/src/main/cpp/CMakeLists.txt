cmake_minimum_required(VERSION 3.18.1)

project("sec")

add_library(
        sec
        SHARED
        native-lib.cpp
        MMKV.cpp
        ProtoBuf.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        sec
        ${log-lib})