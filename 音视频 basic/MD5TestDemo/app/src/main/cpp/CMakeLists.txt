cmake_minimum_required(VERSION 3.18.1)

project("md5")

add_library(
        md5
        SHARED
        native-lib.cpp
        md5.cpp )

find_library(
        log-lib
        log)


target_link_libraries(
        md5
        ${log-lib})