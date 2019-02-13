package com.nmeunier.eggtimer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    SeekBar timerControl;
    int timerValue;
    int count;
    boolean timerStarted;
    CountDownTimer eggCountDownTimer;

    public void updateTimer(int timeInSeconds) {
        SeekBar timerControl = (SeekBar) findViewById(R.id.timeBar);
        final TextView timerView = (TextView) findViewById(R.id.timerTextView);

        int mins = timeInSeconds / 60;
        int remainder = timeInSeconds - mins * 60;
        int secs = remainder;

        String secondsString = String.valueOf(secs);
        if (secs < 10) {
            secondsString = "0" + String.valueOf(secs);
        }
        timerView.setText(String.valueOf(mins) + ":" + secondsString);
        timerControl.setProgress(timeInSeconds);
    }

    public void onClick(View view) {
        if (timerStarted) {
            eggCountDownTimer.cancel();
            timerStarted = false;
            ImageView campfireView = (ImageView) findViewById(R.id.campfireImageView);
            campfireView.setImageResource(R.drawable.campfire_off);
        } else {
            startTimer();
        }
    }

    public void startTimer() {

        timerStarted = true;

        Log.i("Timer", "Start!");
        Log.i("Timer", "Timer value: " + Integer.toString(timerValue / 60) + "min");
        int timerInSeconds = timerValue;
        Log.i("Timer", "In Seconds: " + Integer.toString(timerInSeconds) + "sec");

        final MediaPlayer mplayer = MediaPlayer.create(this, R.raw.beep);


        eggCountDownTimer = new CountDownTimer(timerInSeconds * 1000 + 100, 1000) {
            SeekBar timerControl = (SeekBar) findViewById(R.id.timeBar);
            final TextView timerView = (TextView) findViewById(R.id.timerTextView);
            ImageView campfireView = (ImageView) findViewById(R.id.campfireImageView);

            public void onTick(long millisecondsUntilDone) {
                int secondsUntilDone = (int) millisecondsUntilDone / 1000;
                Log.i("CountDownTimer", String.valueOf(millisecondsUntilDone / 1000));
                //                int mins = secondsUntilDone / 60;
                //                int remainder = secondsUntilDone - mins * 60;
                //                int secs = remainder;
                //
                //                String secondsString = String.valueOf(secs);
                //                if (secs < 10) {
                //                    secondsString = "0" + String.valueOf(secs);
                //                }
                //                timerView.setText(String.valueOf(mins) + ":" + secondsString);
                //                timerControl.setProgress(mins+1);
                updateTimer(secondsUntilDone);

                campfireView.setImageResource(R.drawable.campfire);

            }

            public void onFinish() {
                Log.i("CountDownTimer", "Finished!");
                timerView.setTextColor(Color.RED);
                timerView.setText("0:00");
                timerControl.setProgress(0);
                //                int resourceId = getResources().getIdentifier(id, "raw", "com.nmeunier.eggtimer");

                campfireView.setImageResource(R.drawable.campfire_off);

                count = 1;

                mplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    int maxCount = 5;

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (count < maxCount) {
                            count++;
                            mplayer.seekTo(0);
                            mplayer.start();
                        }
                    }
                });

                mplayer.start();
                timerStarted = false;

            }
        }.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar timerControl = (SeekBar) findViewById(R.id.timeBar);
        final TextView timerView = (TextView) findViewById(R.id.timerTextView);
        ImageView campfireView = (ImageView) findViewById(R.id.campfireImageView);

        String default_timer = getString(R.string.default_timer);
        timerStarted = false;

        timerValue = Integer.valueOf(default_timer);

        timerControl.setMax(600);
        updateTimer(Integer.valueOf(default_timer));
        Log.i("Default Timer value", Integer.toString(R.string.default_timer));
        campfireView.setImageResource(R.drawable.campfire_off);

        timerControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("TimeBar value", Integer.toString(progress));
    //                timerView.setText(Integer.toString(progress) + ":00");
                    timerValue = progress;
                    updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                timerView.setTextColor(Color.GRAY);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timerControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (timerStarted) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
