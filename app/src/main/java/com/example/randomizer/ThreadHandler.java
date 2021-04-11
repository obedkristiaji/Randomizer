package com.example.randomizer;

import android.os.Handler;
import android.os.Message;

public class ThreadHandler extends Handler {
    private MainActivity activity;
    private int MSG_SET_ANGKA = 0;

    ThreadHandler(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if(msg.what == this.MSG_SET_ANGKA) {
            int angka = (int) msg.obj;
            String s = Integer.toString(angka);
            this.activity.setAngka(s);
        }
    }

    public void setAngka(int angka) {
        Message msg = new Message();
        msg.what = MSG_SET_ANGKA;
        msg.obj = angka;

        this.sendMessage(msg);
    }
}
