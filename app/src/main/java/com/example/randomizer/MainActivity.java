package com.example.randomizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.example.randomizer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityMainBinding binding;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private float[] accelerometerReading = new float[3];
    private int shakeThreshold = 1200;
    private long lastUpdate = 0;
    private ThreadHandler handler;
    private RandomThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.accelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.handler = new ThreadHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.accelerometer != null) {
            this.mSensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastUpdate > 100) {
                long diffTime = curTime - lastUpdate;
                lastUpdate = curTime;
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float speed = Math.abs(x + y + z - accelerometerReading[0] - accelerometerReading[1] - accelerometerReading[2]) / diffTime * 10000;
                if (speed > this.shakeThreshold) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(200);
                    }
                    this.startThread();
                }
                accelerometerReading[0] = x;
                accelerometerReading[1] = y;
                accelerometerReading[2] = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //
    }

    public void setAngka(String s) {
        this.binding.tvAngka.setText(s);
    }

    public void startThread() {
        this.thread = new RandomThread(this.handler);
        this.thread.start();
    }
}