package com.example.happycodinggirl.testthreadpool;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    ExecutorService executorService;
    boolean willstop=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button= (Button) findViewById(R.id.button);
        final Button stop= (Button) findViewById(R.id.stop);
        Log.v("TAG","----main  thread IS "+Thread.currentThread().getName());
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                willstop=true;
            }
        };
        Timer timer=new Timer();
        timer.schedule(timerTask,30*1000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               executorService= Executors.newSingleThreadExecutor();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                    Log.v("TAG","----SINGLETHREAD IS "+Thread.currentThread().getName());
                       for (;;) {
                           if (!willstop) {
                              // Log.v("TAG", "----I AM SINGLETHREADExecutor");
                           }
                       }
                    }
                });
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        executorService.shutdownNow();
                        try {
                            if(executorService.awaitTermination(10L, TimeUnit.SECONDS)){
                                Log.v("TAG","----awaitTermination return true");
                            }else{
                                Log.v("TAG","---awaitTermination return false");
                            }

                        } catch (InterruptedException e) {
                            Log.v("TAG","----EXCEPTION IS "+e.getMessage());
                        }
                    }
                }).start();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
