package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

public class RunnableTime implements Runnable{
    private final DisplayTime listener;
    private long millisecond =0;
    private boolean timeRunning;
    private long startTime;
    private final Activity  activity;
    public RunnableTime(DisplayTime listener,Activity activity) {
        this.listener = listener;
        this.activity = activity;
    }
    @Override
    public void run() {
        long endTime = System.currentTimeMillis();
        millisecond = endTime - startTime;
        int hours =(int) millisecond/3600000;
        int minutes =(int) (millisecond/3600000)/60;
        int second =(int) (millisecond/1000)%60;
        @SuppressLint("DefaultLocale") String time = String.format("%d:%02d:%02d:%02d",hours,minutes,second,millisecond % 100);
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        activity.runOnUiThread(() -> listener.displayTime(time,millisecond));
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
