package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFragment extends Fragment implements View.OnClickListener,RunnableTime.Listener {
    TextView textViewStopWatcher;
    Button startButton,stopButton;
    private boolean timeRunning;
    private boolean stopWatcherActive;
    private long systemTime;
    RunnableTime runnableTime;
    ExecutorService service;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textViewStopWatcher = view.findViewById(R.id.textView_display_stop_watcher);
        startButton = view.findViewById(R.id.button);
        stopButton = view.findViewById(R.id.stopButton);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        runnableTime = new RunnableTime(this);
        stopWatcherActive = true;

        return view;
    }

    private void runStopWatcher(){
        service = Executors.newSingleThreadExecutor();
        service.execute(runnableTime);
    }

    public void startTimeButton() {
        if (stopWatcherActive){
            runStopWatcher();
            stopWatcherActive = false;
            runnableTime.setTimeRunning(true);
            if (systemTime==0){
                systemTime = System.currentTimeMillis();
            }
            runnableTime.setStartTime(systemTime);
            Toast.makeText(getContext(),"Działa",Toast.LENGTH_SHORT).show();

        }
    }

    // Interface OnClickListener
    @Override
    public void onClick(View view) {
       if (view.equals(startButton)){
           startTimeButton();
           runnableTime.setTimeRunning(true);
       }
       if (view.equals(stopButton)){
           runnableTime.setTimeRunning(false);
           service.shutdown();
           stopWatcherActive = true;
       }

    }


    // Interface from RunnableClass return time to display
    @Override
    public void displayText(String time) {
        textViewStopWatcher.setText(time);

    }
}