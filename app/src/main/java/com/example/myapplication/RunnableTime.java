package com.example.myapplication;

import android.annotation.SuppressLint;

public class RunnableTime implements Runnable{

    interface Listener{
        void displayText(String time);
    }
    private final Listener listener;
    private long millisecond =0;
    private boolean timeRunning;
    private long startTime;
    public RunnableTime(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        long endTime = System.currentTimeMillis();
        int hours =(int) millisecond/3600000;
        int minutes =(int) (millisecond/3600000)/60;
        int second =(int) (millisecond/1000)%60;

        @SuppressLint("DefaultLocale") String time = String.format("%d:%02d:%02d:%02d",hours,minutes,second,millisecond % 100);

        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        millisecond = endTime - startTime;
        listener.displayText(time);
        if (timeRunning){
            run();
        }
    }

    public void setTimeRunning(boolean b){
        timeRunning = b;
    }
    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

}
