package com.example.analogclock;

import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private AnalogClock clock;

    class Task extends Thread {
        @Override
        public void run() {
            while(!isInterrupted() && isAlive()) {
                try {
                    int hour = Calendar.getInstance().get(Calendar.HOUR);
                    int minute = Calendar.getInstance().get(Calendar.MINUTE);
                    int second = Calendar.getInstance().get(Calendar.SECOND);
                    Log.d("TIME", hour+":"+minute+":"+second);
                    clock.setTime(hour, minute, second);
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clock = findViewById(R.id.clock);
        //clock.setTime(15, 30, 45);

        new Task().start();
    }
}
