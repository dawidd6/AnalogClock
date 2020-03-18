package com.example.analogclock;

import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalogClock clock = findViewById(R.id.clock);
        clock.startRealTime();
    }
}
