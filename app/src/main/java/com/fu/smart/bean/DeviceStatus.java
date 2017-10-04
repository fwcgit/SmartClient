package com.fu.smart.bean;

/**
 * Created by fwc on 2017/10/1.
 */

public class DeviceStatus {
    public int hall;
    public int battery;
    public int ntc;
    public double ntcTemp;
    public float boardTemp;
    public int envTemp;
    public int envHumidity;
    public int toiletRoomTemp;
    public int toiletRoomHumidity;
    public int refVol;

    public void getNtcTemp()
    {
        double temp;
        double t2 ;
        float ntc_r;
        double t_25;
        double ntc_ln;
        double ln ;
        float res_v;

        res_v = ntc * 248.0f / refVol-5.0f;
        ntc_r = (5.0f - res_v/100.0f) / (res_v/100.0f/10.0f);

        t_25 =  Math.log10(10);
        ntc_ln =Math.log10(ntc_r);
        ln = t_25 - ntc_ln;

        ln = ln / 3950.0f;
        t2 = 1.0f/298.15f;
        t2 = t2-ln;
        temp = 1.0f/t2;
        temp = temp - 273.15f;

        ntcTemp = temp;
    }
}
