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
            stopWatcherWasActive = savedInstanceState.getBoolean("RUNNING");
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
    public void onStart() {
        super.onStart();
        if (stopWatcherWasActive){
            runStopWatcher((stopWatcherWasActive));
        }else if (timeString!=null && !timeString.equals("time")) textViewStopWatcher.setText(timeString);
    }
    private void runStopWatcher(boolean b){
        service = Executors.newSingleThreadExecutor();
        runnableTime.setTimeRunning(b);
             if (b) runnableTime.setStartTime(System.currentTimeMillis()-millisecond);
             else runnableTime.setStartTime(System.currentTimeMillis());
        service.execute(runnableTime);
    }
    public void startOnClick() {
        isRunning = true;
        runStopWatcher(true);
    }
    public void stopOnClick(){
        runnableTime.setTimeRunning(false);
        service.shutdown();
        isRunning = false;
    }
                         /* Interface OnClickListener  */
    @Override
    public void onClick(View view) {
       if (view.equals(startButton)) startOnClick();
       if (view.equals(stopButton)) stopOnClick();
    }
                        /*  Interface  DisplayTime   */
    @Override
    public void displayTime(String str,long milli) {
        this.timeString = str;
        this.millisecond = milli;
        textViewStopWatcher.setText(str);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("ON SAVE" ,"SAVE");
        outState.putString("TIME", timeString);
        outState.putLong("MILLI",millisecond);
        outState.putBoolean("RUNNING",stopWatcherWasActive);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.v("ON STOP" ,"PAUSE");
        stopWatcherWasActive = isRunning;
    }
    private void initVariable(View layout){
        textViewStopWatcher = layout.findViewById(R.id.textView_display_stop_watcher);
        startButton = layout.findViewById(R.id.startButton);
        stopButton = layout.findViewById(R.id.stopButton);
        resetButton = layout.findViewById(R.id.resetButton);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        runnableTime = new RunnableTime(this,getActivity());
    }
}