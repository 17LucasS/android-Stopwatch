package com.example.myapplication;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFragment extends Fragment implements View.OnClickListener,DisplayTime {
     private TextView textViewStopWatcher;
     private Button startButton,stopButton,resetButton;
    private boolean stopWatcherWasActive, isRunning;
     private RunnableTime runnableTime;
    private ExecutorService service;
    private String timeString;
    private long millisecond;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            timeString = savedInstanceState.getString("TIME");
            millisecond = savedInstanceState.getLong("MILLI");
            stopWatcherWasActive = savedInstanceState.getBoolean("WAS_ACTIVE");
            isRunning = savedInstanceState.getBoolean("IS_RUNNING");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        initVariable(layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
       if (isRunning){
           runnableTime.setTimeRunning(true);
           runnableTime.setStartTime(System.currentTimeMillis()-millisecond);
           runWatcher();
       }
       if (stopWatcherWasActive){
           textViewStopWatcher.setText(timeString);
       }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_RUNNING",isRunning);
        outState.putBoolean("WAS_ACTIVE",stopWatcherWasActive);
        outState.putString("TIME", timeString);
        outState.putLong("MILLI",millisecond);

    }

    private void runWatcher(){
        runnableTime.setTimeRunning(isRunning);
        service.execute(runnableTime);
    }
    private void startOnClick() {
        isRunning = true;
            if (stopWatcherWasActive){
                  runnableTime.setStartTime(System.currentTimeMillis()-millisecond);
            }else {
                runnableTime.setStartTime(System.currentTimeMillis());
                stopWatcherWasActive = true;
            }
        runWatcher();
    }
    private void stopOnClick(){
        if (isRunning){
            isRunning = false;
            runnableTime.setTimeRunning(isRunning);
        }
    }

    private void restartOnClick(){
        if (!isRunning){
            stopWatcherWasActive = false;
            textViewStopWatcher.setText("Reset");
            runnableTime.setTimeRunning(false);
        }
    }

    /* Interface OnClickListener  */
    @Override
    public void onClick(View view) {
       if (view.equals(startButton)) startOnClick();
       if (view.equals(stopButton)) stopOnClick();
       if (view.equals(resetButton)) restartOnClick();
    }
    /*  Interface  DisplayTime   */
    @Override
    public void displayTime(String str,long milli) {
        this.timeString = str;
        this.millisecond = milli;
        textViewStopWatcher.setText(str);
    }
    private void initVariable(View layout){
        textViewStopWatcher = layout.findViewById(R.id.textView_display_stop_watcher);
        startButton = layout.findViewById(R.id.startButton);
        startButton.setBackgroundColor(Color.GREEN);
        stopButton = layout.findViewById(R.id.stopButton);
        stopButton.setBackgroundColor(Color.GREEN);
        resetButton = layout.findViewById(R.id.resetButton);
        resetButton.setBackgroundColor(Color.GREEN);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        runnableTime = new RunnableTime(this,getActivity());
        service = Executors.newSingleThreadExecutor();
    }
}