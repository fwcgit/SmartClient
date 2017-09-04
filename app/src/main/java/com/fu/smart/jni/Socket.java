package com.fu.smart.jni;

import android.util.Log;

/**
 * Created by fu on 2017/7/13.
 */

public class Socket {
    static {
        System.loadLibrary("Socket");
    }

    public void logMessage(String msg){
        Log.d("JNI_log",msg);
    }


    public native void nativeFree();

    public native void nativeConnectServer(String ip,int port);

    public native void sendData(String msg);

    public native void sendData(byte[] data);

    public native void openPower(int i);
    public native void openLight(int i);
    public native void openSocket(int i);
    public native void openAirfan(int i);


    public void recvData(byte[] data){
        for(int i = 0 ;i < data.length;i++){
            Log.d("JNI_log",Integer.toHexString(data[i]));
        }
    }

}

