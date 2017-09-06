package com.fu.smart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fu.smart.jni.Socket;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Socket socket = new Socket();
        try
        {
            socket.nativeConnectServer("192.168.4.1",34808);
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
    }
}
