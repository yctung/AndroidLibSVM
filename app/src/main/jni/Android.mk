LOCAL_PATH:=$(call my-dir)

include $(CLEAR_VARS)

#include ../../OpenCV-2.4.7-android-sdk/sdk/native/jni/OpenCV.mk
#include /Users/eddyxd/Documents/workspace/OpenCV-2.4.7-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE	:= jnilibsvm
#LOCAL_CFLAGS	:= -Werror
LOCAL_CFLAGS    := -DDEV_NDK=1
#LOCAL_SRC_FILES := common.cpp acoustic_detection.cpp
LOCAL_SRC_FILES := \
	common.cpp jnilibsvm.cpp \
	libsvm/svm-train.cpp \
	libsvm/svm-predict.cpp \
	libsvm/svm.cpp

LOCAL_LDLIBS	+= -llog -ldl

include $(BUILD_SHARED_LIBRARY)

