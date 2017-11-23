package com.example.jerm.mindfulnessphoneuse;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.app.Notification;
import android.widget.TextView;


import java.util.concurrent.Semaphore;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */


public class GappScreen extends ExecutableActivity {

    private static Boolean runningInBackground = false;
    private static Boolean check_for_old_preferences = true;
    private static Boolean mEnabled = false;
    private ProgressBarAnimation mExpireAnimation;
    private static int mLifetimeInMS;
    private static String mMindfulPrompt;
    private static String my_prefs_file = "REFLECTPREFS";

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView =  getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gapp_screen);
        fetchPreferences(this);
        hideSystemUI();

        mInterval = 500;

        TextView mindfulPrompt = (TextView) findViewById(R.id.tv_mp);
        if (mMindfulPrompt.isEmpty()){
            mindfulPrompt.setText(R.string.mindful_prompt);
        }
        else{
            mindfulPrompt.setText(mMindfulPrompt);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.timeToExecutionBar);
        mProgressBar.setMax(mLifetimeInMS);


        mExpireAnimation = new ProgressBarAnimation(mProgressBar, 0, mLifetimeInMS);
        mExpireAnimation.setDuration(mLifetimeInMS);

        launchForegroundService(this);
    }





    @Override
    protected void onPause(){
        super.onPause();
        mProgressBar.clearAnimation();
    }

    @Override
    protected void onResume(){
        super.onResume();
        hideSystemUI();

        mProgressBar.startAnimation(mExpireAnimation);
    }


    public class ProgressBarAnimation extends Animation{
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
            this.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }


    }

    public static void launchForegroundService(Context context){
        fetchPreferences(context);
        if (runningInBackground || !mEnabled){
            return;
        }
        runningInBackground = true;

        Intent intent = new Intent(context, AutoStartService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }
        else {
            context.startService(intent);
        }
    }

    public static void stopForegroundService(){
        if (!runningInBackground)
            return;
        runningInBackground = false;
        AutoStartService.stop();
    }

    public static void disable(Context context){
        mEnabled = false;
        updatePreferences(context);
    }

    private static void updatePreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences(my_prefs_file, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sleepTime", mLifetimeInMS);
        editor.putString("mindfulString", mMindfulPrompt);
        editor.putBoolean("enabled", mEnabled);
        editor.commit();
    }

    private static void fetchPreferences(Context context){
        if (check_for_old_preferences) {
            SharedPreferences settings = context.getSharedPreferences(my_prefs_file, 0);
            mLifetimeInMS = settings.getInt("sleepTime", 4);
            mMindfulPrompt = settings.getString("mindfulString", context.getString(R.string.mindful_prompt));
            mEnabled = settings.getBoolean("enabled", false);
        }
    }

    public static void updateTime(int newTime, Context context){
        mLifetimeInMS = newTime * 1000;
        mEnabled = true;
        updatePreferences(context);

    }

    public static void updateMessage(String prompt, Context context){
        mMindfulPrompt = prompt;
        mEnabled = true;
        updatePreferences(context);
    }
}

