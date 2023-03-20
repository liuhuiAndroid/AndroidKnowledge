#声明cmake最低版本
cmake_minimum_required(VERSION 3.18.1)
#导入头文件
include_directories(include)

project("identity")

#添加文件夹
#include_directories()

#将native-lib.cpp编译生成动态库identity_t
add_library(
        identity_t
        SHARED
        native-lib.cpp)

#设置系统环境变量
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")
message(--------------------------)
message(${CMAKE_SOURCE_DIR})
message(--------------------------)
message(${PROJECT_SOURCE_DIR})
message(--------------------------)

#在系统库中查找log->liblog.so赋值给log-lib
find_library(
        log-lib
        log)

#链接动态库，最终生成identity.so
target_link_libraries(
        identity_t
        #导入库文件，变成系统的环境变量
        opencv_java4
        #系统内置库,$代表去系统目录去取
        ${log-lib}
        jnigraphics
        android)