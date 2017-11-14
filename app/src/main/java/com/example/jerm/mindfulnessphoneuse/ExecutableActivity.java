package com.example.jerm.mindfulnessphoneuse;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by jerm on 10/14/17.
 */

public class ExecutableActivity extends AppCompatActivity {

    private boolean active = false;
    protected ProgressBar mProgressBar;
    protected int mInterval;
    protected int mTicks;

    @Override
    public void onStart(){
        active = true;
        super.onStart();
    }

    @Override
    public void onStop(){
        active = false;
        super.onStop();
    }
    public void execute(){
        if (active)
            finish();
    }

}