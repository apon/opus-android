//
// Created by Yaopeng on 2019/3/6.
//
#include "me_apon_opuscodec_OpusDecoder.h"
#include <string.h>
#include <android/log.h>
#include "opus.h"
#include <stdio.h>

char logMsg[255];
OpusDecoder *dec;

opus_int32 SAMPLING_RATE;
int CHANNELS;
int FRAME_SIZE;


JNIEXPORT jboolean JNICALL Java_me_apon_opuscodec_OpusDecoder_init(JNIEnv *env, jclass obj, jint samplingRate, jint numberOfChannels, jint frameSize){

        FRAME_SIZE = frameSize;
    	SAMPLING_RATE = samplingRate;
    	CHANNELS = numberOfChannels;

    	int size;
    	int error;

    	size = opus_decoder_get_size(CHANNELS);
    	dec = malloc(size);
    	error = opus_decoder_init(dec, SAMPLING_RATE, CHANNELS);

    	sprintf(logMsg, "Initialized Decoder with ErrorCode: %d", error);
    	__android_log_write(ANDROID_LOG_DEBUG, "Native Code:", logMsg);

    	return error;
}



JNIEXPORT jint JNICALL Java_me_apon_opuscodec_OpusDecoder_decode (JNIEnv *env, jclass obj, jbyteArray in, jshortArray out){

        jint inputArraySize = (*env)->GetArrayLength(env, in);
    	jint outputArraySize = (*env)->GetArrayLength(env, out);

    	jbyte* encodedData = (*env)->GetByteArrayElements(env, in, 0);
    	opus_int16 *data = (opus_int16*)calloc(outputArraySize,sizeof(opus_int16));
    	int decodedDataArraySize = opus_decode(dec, encodedData, inputArraySize, data, FRAME_SIZE, 0);

    	if (decodedDataArraySize >=0)
    	{
    		if (decodedDataArraySize <= outputArraySize)
    		{
    			(*env)->SetShortArrayRegion(env,out,0,decodedDataArraySize,data);
    		}
    		else
    		{
    			sprintf(logMsg, "Output array of size: %d to small for storing encoded data.", outputArraySize);
    			__android_log_write(ANDROID_LOG_DEBUG, "Native Code:", logMsg);

    			return -1;
    		}
    	}

    	(*env)->ReleaseByteArrayElements(env,in,encodedData,JNI_ABORT);

    	return decodedDataArraySize;
}


JNIEXPORT jboolean JNICALL Java_me_apon_opuscodec_OpusDecoder_close (JNIEnv *env, jclass obj){

    free(dec);

    return 1;
}
