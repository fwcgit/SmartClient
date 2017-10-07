package com.fu.smart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fu.smart.bean.DeviceStatus;
import com.fu.smart.jni.Socket;

public class MainActivity extends Activity implements Socket.Parser {
    private TextView boardTempTv;
    private TextView batteryTempTv;
    private TextView evnTempTv;
    private TextView evnHumidityTv;
    private TextView roomTempTv;
    private TextView roomHumidityTv;
    private SeekBar  seekBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        boardTempTv     = (TextView) findViewById(R.id.boardTempTv);
        batteryTempTv   = (TextView) findViewById(R.id.batteryTempTv);
        evnTempTv       = (TextView) findViewById(R.id.evnTempTv);
        evnHumidityTv   = (TextView) findViewById(R.id.evnHumidityTv);
        roomTempTv      = (TextView) findViewById(R.id.roomTempTv);
        roomHumidityTv  = (TextView) findViewById(R.id.roomHumidityTv);
        seekBar         = (SeekBar) findViewById(R.id.seekbar);

        final Socket socket = new Socket();
        socket.setParserCallback(this);
        try
        {
            socket.nativeConnectServer("192.168.4.1",333);
        }catch (Exception e){
            e.printStackTrace();
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openPower(1);
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openPower(2);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openSocket(1);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openSocket(2);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openLight(1);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openLight(2);
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openAirfan(1);
            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.openAirfan(2);
            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[3];
                data[0] = 0x3b;
                data[1] = (byte) 0xdd;
                data[2] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[4];
                data[0] = 0x3b;
                data[1] = 0x5a;
                data[2] = 0x5c;
                data[3] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[4];
                data[0] = 0x3b;
                data[1] = 0x5a;
                data[2] = 0x5b;
                data[3] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[4];
                data[0] = 0x3b;
                data[1] = 0x5a;
                data[2] = 0x5e;
                data[3] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[5];
                data[0] = 0x3b;
                data[1] = 0x6a;
                data[2] = 0x6b;
                data[3] = 0x01;
                data[4] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[5];
                data[0] = 0x3b;
                data[1] = 0x6a;
                data[2] = 0x6b;
                data[3] = 0x02;
                data[4] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[5];
                data[0] = 0x3b;
                data[1] = 0x6a;
                data[2] = 0x6c;
                data[3] = 0x01;
                data[4] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data = new byte[5];
                data[0] = 0x3b;
                data[1] = 0x6a;
                data[2] = 0x6c;
                data[3] = 0x02;
                data[4] = 0x0d;
                socket.sendDataByte(data);
            }
        });

        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TvControlActivity.class));
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                byte[] data = new byte[5];
                data[0] = 0x3b;
                data[1] = 0x5a;
                data[2] = 0x5d;
                data[3] = (byte) seekBar.getProgress();
                data[4] = 0x0d;
                socket.sendDataByte(data);
            }
        });

    }


    @Override
    public void deviceStatus(final DeviceStatus status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boardTempTv.setText(String.format("主板温度：%.2f℃",status.boardTemp));
                batteryTempTv.setText(String.format("电池温度：%.2f℃",status.ntcTemp));
                evnTempTv.setText(String.format("环境温度：%s℃",status.envTemp));
                evnHumidityTv.setText(String.format("环境湿度：%s",status.envHumidity)+"%");
                roomTempTv.setText(String.format("卫生间温度：%s℃",status.toiletRoomTemp));
                roomHumidityTv.setText(String.format("卫生间湿度：%s",status.toiletRoomHumidity)+"%");
            }
        });
    }
}
