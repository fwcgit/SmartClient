package com.fu.smart.jni;

import android.util.Log;

import com.fu.smart.bean.DeviceStatus;

/**
 * Created by fu on 2017/7/13.
 */

public class Socket {
    static {
        System.loadLibrary("Socket");
    }

    public interface Parser{
        void deviceStatus(DeviceStatus status);
    }

    private Parser parser;
    public void setParserCallback(Parser parser){
        this.parser = parser;
    }
    public void logMessage(String msg){
        Log.d("JNI_log",msg);
    }

    public native void nativeFree();

    public native void nativeConnectServer(String ip,int port);

    public native void sendData(String msg);

    public native void sendDataByte(byte[] data);

    public native void openPower(int i);
    public native void openLight(int i);
    public native void openSocket(int i);
    public native void openAirfan(int i);


    public void recvData(byte[] data){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ;i < data.length;i++){
            if(i < data.length -1){
                sb.append(Integer.toHexString(data[i])+":");
            }else{
                sb.append(Integer.toHexString(data[i]));
            }
        }
        Log.d("java_JNI_log",sb.toString());

        parserDevice(data);
    }

    private void parserDevice(byte[] data){
        DeviceStatus deviceStatus = new DeviceStatus();
        if(data[0] == 0x3a && data[1] == 0x53 ){

            int ref = data[8];
            ref = ref << 8;
            ref = ref | (data[9]&0xff);
            deviceStatus.refVol = ref;

            int hall = data[2];
            hall = hall << 8;
            hall = hall | (data[3]&0xff);
            hall = hall * 248 / ref;
            deviceStatus.hall = hall;

            int battery = data[4];
            battery = battery << 8;
            battery = battery | (data[5]&0xff);
            battery = battery * 248 / ref;
            deviceStatus.battery = battery;

            int ntc = data[6];
            ntc = ntc << 8;
            ntc = ntc | (data[7]&0xff);;
            deviceStatus.ntc = ntc;
            deviceStatus.getNtcTemp();

            int board = data[11];
            board = board << 8;
            board = board | (data[12]&0xff);
            deviceStatus.boardTemp = board * 0.0625f;


            deviceStatus.envHumidity = data[13] &0xff;
            deviceStatus.envTemp = data[15]&0xff;

            deviceStatus.toiletRoomTemp = data[17]&0xff;
            deviceStatus.toiletRoomHumidity = data[19]&0xff;

            if(null != parser){
                parser.deviceStatus(deviceStatus);
            }
        }
    }

}

