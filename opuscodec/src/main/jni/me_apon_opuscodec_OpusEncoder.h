/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class me_apon_opuscodec_OpusEncoder */

#ifndef _Included_me_apon_opuscodec_OpusEncoder
#define _Included_me_apon_opuscodec_OpusEncoder
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     me_apon_opuscodec_OpusEncoder
 * Method:    init
 * Signature: (III)Z
 */
JNIEXPORT jboolean JNICALL Java_me_apon_opuscodec_OpusEncoder_init
  (JNIEnv *, jclass, jint, jint, jint, jint);

/*
 * Class:     me_apon_opuscodec_OpusEncoder
 * Method:    encode
 * Signature: ([S[B)I
 */
JNIEXPORT jint JNICALL Java_me_apon_opuscodec_OpusEncoder_encode
  (JNIEnv *, jclass, jshortArray, jbyteArray);

/*
 * Class:     me_apon_opuscodec_OpusEncoder
 * Method:    close
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_me_apon_opuscodec_OpusEncoder_close
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
