package com.example.myapplication;
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
    TextView textViewStopWatcher;
    Button startButton,stopButton,resetButton;
    private boolean stopWatcherWasActive;
    private boolean isRunning;
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
            stopWatcherWasActive = savedInstanceState.getBoolean("RUNNING");
            Log.v("SAVE INSTANCE","NOT NULL");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("ON START",String.valueOf(stopWatcherWasActive));
        if (stopWatcherWasActive){
            runStopWatcher();
            runnableTime.setStartTime(System.currentTimeMillis()-millisecond);
            runnableTime.setTimeRunning(true);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TIME", timeString);
        outState.putLong("MILLI",millisecond);
        outState.putBoolean("RUNNING",stopWatcherWasActive);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        textViewStopWatcher = layout.findViewById(R.id.textView_display_stop_watcher);
        startButton = layout.findViewById(R.id.startButton);
        stopButton = layout.findViewById(R.id.stopButton);
        resetButton = layout.findViewById(R.id.resetButton);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        runnableTime = new RunnableTime(this,getActivity());
        return layout;
    }

    private void runStopWatcher(){
        service = Executors.newSingleThreadExecutor();
        service.execute(runnableTime);
    }

    public void startOnClick() {
        if (service==null){
            runnableTime.setTimeRunning(true);
            runnableTime.setStartTime(System.currentTimeMillis());
            isRunning = true;
            runStopWatcher();
            Log.v("SERVICE","NULL");
        }
        if (service.isShutdown()){
            runnableTime.setTimeRunning(true);
            runnableTime.setStartTime(System.currentTimeMillis()-millisecond);
            isRunning = true;
            runStopWatcher();
            Log.v("SERVICE","SHUTDOWN");
        }

    }

    public void stopOnClick(){
        runnableTime.setTimeRunning(false);
        service.shutdown();
        isRunning = false;
    }

    // Interface OnClickListener
    @Override
    public void onClick(View view) {
       if (view.equals(startButton)){
           startOnClick();
           runnableTime.setTimeRunning(true);
       }
       if (view.equals(stopButton)){
           stopOnClick();
       }

    }


    // Interface  DisplayTime
    @Override
    public void displayTime(String str,long milli) {
        this.timeString = str;
        this.millisecond = milli;

        textViewStopWatcher.setText(str);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("ON STOP","TAK");
        stopWatcherWasActive = isRunning;
    }
}