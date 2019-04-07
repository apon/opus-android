//
// Created by Yaopeng on 2019/3/6.
//


#include "me_apon_opuscodec_OpusEncoder.h"
#include <string.h>
#include <android/log.h>
#include "opus.h"
#include <stdio.h>

char logMsg[255];
OpusEncoder *enc;

opus_int32 SAMPLING_RATE;
int CHANNELS;
int APPLICATION_TYPE = OPUS_APPLICATION_VOIP;
int FRAME_SIZE;
const int MAX_PAYLOAD_BYTES = 1500;


JNIEXPORT jboolean JNICALL Java_me_apon_opuscodec_OpusEncoder_init(JNIEnv *env, jclass obj, jint samplingRate, jint numberOfChannels, jint frameSize,jint appType){

    FRAME_SIZE = frameSize;
	SAMPLING_RATE = samplingRate;
	CHANNELS = numberOfChannels;

	int error;
	int size;

	size = opus_encoder_get_size(1);
	enc = malloc(size);
	error = opus_encoder_init(enc, SAMPLING_RATE, CHANNELS, appType);

	sprintf(logMsg, "Initialized Encoder with ErrorCode: %d", error);
	__android_log_write(ANDROID_LOG_DEBUG, "Native Code:", logMsg);

	return error;
}

JNIEXPORT jint JNICALL Java_me_apon_opuscodec_OpusEncoder_encode (JNIEnv *env, jclass obj, jshortArray in, jbyteArray out){

        jint inputArraySize = (*env)->GetArrayLength(env, in);
    	jint outputArraySize = (*env)->GetArrayLength(env, out);

    	jshort* audioSignal = (*env)->GetShortArrayElements(env, in, 0);

    	unsigned char *data = (unsigned char*)calloc(MAX_PAYLOAD_BYTES,sizeof(unsigned char));
    	int dataArraySize = opus_encode(enc, audioSignal, FRAME_SIZE, data, MAX_PAYLOAD_BYTES);

    	if (dataArraySize >=0)
    	{
    		if (dataArraySize <= outputArraySize)
    		{
    			(*env)->SetByteArrayRegion(env,out,0,dataArraySize,data);
    		}
    		else
    		{
    			sprintf(logMsg, "Output array of size: %d to small for storing encoded data.", outputArraySize);
    			__android_log_write(ANDROID_LOG_DEBUG, "Native Code:", logMsg);

    			return -1;
    		}
    	}

    	(*env)->ReleaseShortArrayElements(env,in,audioSignal,JNI_ABORT);

    	return dataArraySize;
}

JNIEXPORT jboolean JNICALL Java_me_apon_opuscodec_OpusEncoder_close (JNIEnv *evn, jclass obj){

    free(enc);

    return 1;
}
