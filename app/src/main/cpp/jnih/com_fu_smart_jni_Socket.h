/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_fu_smart_jni_Socket */

#ifndef _Included_com_fu_smart_jni_Socket
#define _Included_com_fu_smart_jni_Socket
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    nativeFree
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_nativeFree
  (JNIEnv *, jobject);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    nativeConnectServer
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_nativeConnectServer
  (JNIEnv *, jobject, jstring, jint);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    sendData
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_sendData
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    sendDataByte
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_sendDataByte
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openPower
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openPower
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openLight
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openLight
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openSocket
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openSocket
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openAirfan
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openAirfan
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
