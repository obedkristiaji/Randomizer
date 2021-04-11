package com.example.randomizer;

import android.util.Log;

public class RandomThread implements Runnable {
    private Thread thread = new Thread(this);
    private boolean start = false;
    private ThreadHandler handler;
    private int angka;
    private int min = 1;
    private int max = 6;

    RandomThread(ThreadHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            while(start) {
                this.angka = min + (int)(Math.random() * ((max - min) + 1));
                this.handler.setAngka(this.angka);
                Log.d("angka", String.valueOf(angka));
                this.start = false;
                this.thread.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.start = true;
        this.thread.start();
    }
}
