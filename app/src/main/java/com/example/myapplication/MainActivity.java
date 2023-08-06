package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState==null){
            MainFragment fragment = new MainFragment();
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.add(R.id.fragment_main_stop_watcher,fragment);
            tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            tr.commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

}