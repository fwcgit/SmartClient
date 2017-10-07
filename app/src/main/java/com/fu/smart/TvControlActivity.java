package com.fu.smart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fu.smart.jni.Socket;

/**
 * Created by fwc on 2017/10/6.
 */

public class TvControlActivity extends Activity {

    boolean isStu =false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tv_control_layout);

        findButton((ViewGroup) getWindow().getDecorView().getRootView());
    }

    private void findButton(ViewGroup group){
        for(int i = 0 ; i < group.getChildCount();i++){
            View childView = group.getChildAt(i);
            if(childView instanceof Button){
                bindButtonListener((Button) childView);
            }else if(childView instanceof ViewGroup){
                findButton((ViewGroup) childView);
            }
        }
    }

    private void bindButtonListener(final Button btn){
        if(null != btn){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if(btn.getTag().toString().equals("stu")){
                            if(!isStu){
                                btn.setText("结束学习");
                            }else{
                                stopCmd();
                                btn.setText("开始学习");
                            }
                            isStu = !isStu;

                        }else {
                            sendCmd(Integer.valueOf(btn.getTag().toString()));
                        }
                }
            });
        }
    }

    private void sendCmd(int val){
        byte[] data = new byte[5];
        data[0] = 0x3b;
        data[1] = (byte)0xe0;
        data[2] = (isStu) ? (byte)0xfa : (byte)0xfb;
        data[3] = (byte) val;
        data[4] = 0x0d;

        Socket.getInstance().sendDataByte(data);
    }

    private void stopCmd(){
        byte[] data = new byte[5];
        data[0] = 0x3b;
        data[1] = (byte)0xe0;
        data[2] = (byte)0xff;
        data[3] = 0;
        data[4] = 0x0d;

        Socket.getInstance().sendDataByte(data);
    }
}
