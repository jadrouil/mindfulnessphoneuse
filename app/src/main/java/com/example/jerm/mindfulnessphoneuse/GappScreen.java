package com.example.jerm.mindfulnessphoneuse;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */


public class GappScreen extends ExecutableActivity {


    private ProgressBarAnimation mExpireAnimation;

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
        hideSystemUI();

        int mLifetimeInMS = 7000;
        mInterval = 500;

        mProgressBar = (ProgressBar) findViewById(R.id.timeToExecutionBar);
        mProgressBar.setMax(mLifetimeInMS);



        mExpireAnimation = new ProgressBarAnimation(mProgressBar, 0, mLifetimeInMS);
        mExpireAnimation.setDuration(mLifetimeInMS);

        Intent intent = new Intent(this, AutoStartService.class);
        startService(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mProgressBar.clearAnimation();
    }

    @Override
    protected void onResume(){
        super.onResume();
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


}

