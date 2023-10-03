package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import java.lang.ref.WeakReference;

public class RunnableTime implements Runnable{
    private final DisplayTime listener;
    private long millisecond =0;
    private boolean timeRunning;
    private long startTime;

    private final WeakReference<Activity> weakActivity;
    public RunnableTime(DisplayTime listener,WeakReference<Activity> weakActivity) {
        this.listener = listener;
        this.weakActivity = weakActivity;
    }
    @Override
    public void run() {
        long endTime = System.currentTimeMillis();
        millisecond = endTime - startTime;
        int hours = (int) millisecond / 3600000;
        int minutes = (int) millisecond / 60000;
        int seconds = (int) millisecond / 1000;
        @SuppressLint("DefaultLocale") String time = String.format("%d:%02d:%02d:%03d",hours,minutes,seconds,millisecond%1000);
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        weakActivity.get().runOnUiThread(() -> listener.displayTime(time,millisecond));
        if (timeRunning){
            run();
        }
    }
    public void setTimeRunning(boolean b){
        this.timeRunning = b;
    }
    public void setStartTime(long startTime){
            this.startTime = startTime;
    }

}
