LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_MODULE_DIR       := opus

LOCAL_MODULE        := aopus

MY_CPP_LIST := $(wildcard $(LOCAL_PATH)/$(MY_MODULE_DIR)/src/*.c)
MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/$(MY_MODULE_DIR)/celt/*.c)
MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/$(MY_MODULE_DIR)/silk/*.c)
MY_CPP_LIST += $(wildcard $(LOCAL_PATH)/$(MY_MODULE_DIR)/silk/fixed/*.c)
LOCAL_SRC_FILES     := $(MY_CPP_LIST) \
    me_apon_opuscodec_OpusEncoder.c \
    me_apon_opuscodec_OpusDecoder.c
    #$(subst $(ROOT_DIR)/$(MY_MODULE_DIR)/,,$(wildcard $(ROOT_DIR)/$(MY_MODULE_DIR)/src/*.c*)) \
    #$(subst $(ROOT_DIR)/$(MY_MODULE_DIR)/,,$(wildcard $(ROOT_DIR)/$(MY_MODULE_DIR)/celt/*.c*)) \
    #$(subst $(ROOT_DIR)/$(MY_MODULE_DIR)/,,$(wildcard $(ROOT_DIR)/$(MY_MODULE_DIR)/silk/*.c*)) \
    #$(subst $(ROOT_DIR)/$(MY_MODULE_DIR)/,,$(wildcard $(ROOT_DIR)/$(MY_MODULE_DIR)/silk/fixed/*.c*))


$(info $(LOCAL_SRC_FILES))

LOCAL_LDLIBS        := -lm -llog
LOCAL_C_INCLUDES    := \
    $(LOCAL_PATH)/$(MY_MODULE_DIR)/include \
    $(LOCAL_PATH)/$(MY_MODULE_DIR)/silk \
    $(LOCAL_PATH)/$(MY_MODULE_DIR)/silk/fixed \
    $(LOCAL_PATH)/$(MY_MODULE_DIR)/celt

LOCAL_CFLAGS        := -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1 -D_FILE_OFFSET_BITS=64
LOCAL_CFLAGS    += -Drestrict='' -D__EMX__ -DOPUS_BUILD -DFIXED_POINT -DUSE_ALLOCA -DHAVE_LRINT -DHAVE_LRINTF -O3 -fno-math-errno
LOCAL_CPPFLAGS      := -DBSD=1
LOCAL_CPPFLAGS          += -ffast-math -O3 -funroll-loops

include $(BUILD_SHARED_LIBRARY)

