cmake_minimum_required(VERSION 3.4.1) #指定cmake版本

set(jniLibs "${CMAKE_SOURCE_DIR}/src/main/jniLibs")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -fexceptions -frtti")

add_library( #生成函数库的名字
             jnilibsvm
             SHARED  #生成动态函数看
             src/main/jni/common.cpp
             src/main/jni/jnilibsvm.cpp
             src/main/jni/libsvm/svm-train.cpp
             src/main/jni/libsvm/svm-predict.cpp
             src/main/jni/libsvm/svm-scale.cpp
             src/main/jni/libsvm/svm.cpp ) #依赖的cpp文件

#set_target_properties(jnilibsvm PROPERTIES LIBRARY_OUTPUT_DIRECTORY ${jniLibs}/${ANDROID_ABI})

find_library( #设置path变量的名称
              log-lib
              #指定要查询库的名字
              log ) #在ndk开发包中查询liblog.so函数库(默认省略lib和.so),路径赋值给log-lib

target_link_libraries( #目标库,和上面生成的函数库名字一至
                       jnilibsvm
                       android log
                        #连接的库,根据log-lib变量对应liblog.so函数库
                       ${log-lib} )