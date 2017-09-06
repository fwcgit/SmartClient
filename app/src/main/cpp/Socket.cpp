//
// Created by fwc on 2017/7/13.
//
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <endian.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <errno.h>
#include <string.h>
#include "Echo.cpp"
#include <pthread.h>
#include <signal.h>
#include <unistd.h>
#include <android/log.h>

#include "jnih/com_fu_smart_jni_Socket.h"

#define logD(...)  __android_log_print(ANDROID_LOG_DEBUG,"smart jni log", __VA_ARGS__)

static int socket_id = 0;
static bool is_connect = false;
static bool is_run = true;
static char* server_ip = NULL;
static int server_port = 0;

static JavaVM *gVm = NULL;
static jobject gObj = NULL;

void sendHearData();

static void* read_socket_data_thread(void*)
{


    JNIEnv *env = NULL;
    if(0 == gVm->AttachCurrentThread(&env,NULL))
    {
       char buffer[MAX_BUFFER_SIZE];
       while(!is_connect);

       while(is_run)
      {

        ssize_t  recvSize = recv(socket_id,buffer,MAX_BUFFER_SIZE,0);

        if(-1 == recvSize)
        {
            is_connect = false;
            LogMessage(env,gObj,"no recv data");
        }
        else
        {
            if(recvSize > 0)
            {
                jclass clz = env->GetObjectClass(gObj);

                if(NULL != clz)
                {
                    jmethodID method_ID = env->GetMethodID(clz,"recvData","([B)V");
                    env->DeleteLocalRef(clz);

                    if(NULL != method_ID)
                    {

                        jbyteArray ba = env->NewByteArray(recvSize);

                        jbyte *jBytes = (jbyte*)malloc(recvSize* sizeof(jbyte));

                        for(int i = 0;i < recvSize;i++)
                        {
                            *(jBytes+i) = buffer[i];
                        }

                        env->SetByteArrayRegion(ba,0,recvSize,jBytes);

                        env->CallVoidMethod(gObj,method_ID,ba);

                        free(jBytes);
                        jBytes = NULL;

                    }

                }

                logD("received %d bytes:%s",recvSize,buffer);
            }
            else{
                is_connect = false;
                logD("socket disconnected.");
            }
        }
      }

       gVm->DetachCurrentThread();

    }
    return (void *) 1;
}

static void ConnectToAddress(JNIEnv *env,jobject obj,int socketID, const char *ip, unsigned short port)
{
    logD("Connecting to %s:%u.....",ip,port);

    struct sockaddr_in address;
    memset(&address,0, sizeof(address));

    address.sin_family = PF_INET;

    if(0 == inet_aton(ip,&(address.sin_addr)))
    {
        is_connect = false;
        logD("java/io/IOException",errno);
    }
    else
    {
        address.sin_port = htons(port);
        if(-1 == connect(socketID,(struct sockaddr *)&address, sizeof(address)))
        {
            is_connect = false;
            logD("java/io/IOException",errno);
        }
        else
        {

            is_connect = true;

            logD("Connected....");
        }
    }

}

static int CreateTcpSocket(JNIEnv *env,jobject obj)
{
    logD("create new Tcp socket.....");

    int tcpSocket = socket(PF_INET,SOCK_STREAM,0);

    if(-1 == tcpSocket)
    {
        logD("java/io/IOException",errno);
    }

    return tcpSocket;
}

static void sendToSocket(JNIEnv *env,jobject obj,const char *buffer,size_t buffersize)
{
    if(is_connect)
    {
        logD("sending to the socket....");

        size_t  sendSize = send(socket_id,buffer,buffersize,0);

        if(-1 == sendSize){

            is_connect = false;
            logD("sending fail ,socket the close....");
        }
        else
        {
            if(sendSize > 0) {
                logD("send %d bytes: %s", sendSize, buffer);
            }
        }
    }
    else
    {
        logD("sending fail ,socket the close....");
    }
}

static void timer_task(int sig)
{
    if(SIGALRM == sig)
    {
        logD("check socket is_connect");

        if(gVm != NULL)
        {
            JNIEnv *jniEnv;

            gVm->GetEnv((void **)&jniEnv,JNI_VERSION_1_6);

            if(!is_connect)
            {
                if(jniEnv != NULL)
                {
                    socket_id = CreateTcpSocket(jniEnv,gObj);

                    ConnectToAddress(jniEnv,gObj,socket_id,server_ip,(unsigned short)server_port);
                }
            }else{
                sendHearData();
            }

        }

        alarm(10);

    }

    return ;
}

void sendHearData()
{

    JNIEnv *jniEnv;

    gVm->GetEnv((void **)&jniEnv,JNI_VERSION_1_6);

    char *buffer = (char *) malloc(3 * sizeof(char));
    *(buffer) = 0x3a;
    *(buffer+1) = 0xfe;
    *(buffer+2) = 0x0d;
    sendToSocket(jniEnv,gObj,buffer, sizeof(buffer));
    free(buffer);
}

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    nativeConnectServer
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_nativeConnectServer
        (JNIEnv *env, jobject obj, jstring ip, jint port)
{

    const char* ipAddr = env->GetStringUTFChars(ip,NULL);

    if(gObj == NULL)
    {
        gObj = env->NewGlobalRef(obj);

        if(gObj == NULL)
        {
            logD("create public obj fail");
        }
    }

    server_ip = (char *) malloc(strlen(ipAddr) * sizeof(char));
    strcpy(server_ip,ipAddr);
    server_port = port;

    env->ReleaseStringUTFChars(ip,ipAddr);

    signal(SIGALRM,timer_task);
    alarm(10);


    pthread_t  pthread_ID;
    if(!pthread_create(&pthread_ID,NULL,read_socket_data_thread,(void *)NULL))
    {
        logD("pthread create success....");
    }else{
        logD("pthread create fail....");
    }


}


JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_sendData
        (JNIEnv *env, jobject obj, jstring msg)
{
    const char *msgBuffer = env->GetStringUTFChars(msg,NULL);
    jsize msgSize = env->GetStringUTFLength(msg);
    sendToSocket(env,obj,msgBuffer,msgSize);
    env->ReleaseStringUTFChars(msg,msgBuffer);
}

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    sendData
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_sendData___3B
        (JNIEnv *env, jobject obj, jbyteArray data)
{

    jsize size = env->GetArrayLength(data);

    char *buff = (char*)malloc(size* sizeof(char));

    jbyte *jbyteList = env->GetByteArrayElements(data,NULL);

    for(int i = 0 ;i < size;i++)
    {
        *(buff+i) = *(jbyteList+i);
    }

    sendToSocket(env,obj,buff,size);

    env->ReleaseByteArrayElements(data,jbyteList,0);
    free(buff);
    buff = NULL;

}


/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openPower
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openPower
        (JNIEnv *env, jobject obj, jint cmd)
{

    char *data = (char*)malloc(5* sizeof(char));

    *(data) = 0x3b;
    *(data+1) = 0x4a;
    *(data+2) = 0x53;
    *(data+3) = (cmd == 1 ? 0x01:0x02);
    *(data+4) = 0x0d;

    sendToSocket(env,obj,data, sizeof(data));

    free(data);

}

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openLight
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openLight
        (JNIEnv *env, jobject obj, jint cmd)
{

    char *data = (char*)malloc(5* sizeof(char));

    *(data) = 0x3b;
    *(data+1) = 0x4a;
    *(data+2) = 0x54;
    *(data+3) = (cmd == 1 ? 0x01:0x02);
    *(data+4) = 0x0d;

    sendToSocket(env,obj,data, sizeof(data));

}

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openSocket
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openSocket
        (JNIEnv *env, jobject obj, jint cmd)
{
    char *data = (char*)malloc(5* sizeof(char));

    *(data) = 0x3b;
    *(data+1) = 0x4a;
    *(data+2) = 0x55;
    *(data+3) = (cmd == 1 ? 0x01:0x02);
    *(data+4) = 0x0d;

    sendToSocket(env,obj,data, sizeof(data));
}

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    openAirfan
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_openAirfan
        (JNIEnv *env, jobject obj, jint cmd)
{
    char *data = (char*)malloc(5* sizeof(char));

    *(data) = 0x3b;
    *(data+1) = 0x4a;
    *(data+2) = 0x56;
    *(data+3) = (cmd == 1 ? 0x01:0x02);
    *(data+4) = 0x0d;

    sendToSocket(env,obj,data, sizeof(data));

}



jint JNI_OnLoad(JavaVM *vm,void *reserved)
{
    gVm = vm;

    return JNI_VERSION_1_4;
}

/*
 * Class:     com_fu_smart_jni_Socket
 * Method:    nativeFree
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fu_smart_jni_Socket_nativeFree
        (JNIEnv *env, jobject obj)
{
    if(NULL != gObj)
    {
        env->DeleteGlobalRef(gObj);
        gObj = NULL;
    }

}



