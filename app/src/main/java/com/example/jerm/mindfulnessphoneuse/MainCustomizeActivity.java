package com.example.jerm.mindfulnessphoneuse;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Button;

public class MainCustomizeActivity extends AppCompatActivity {
    private NumberPicker mNumberPicker;
    private EditText mEditText;

    private void updateColor(Button b, int color_id){
        if(Build.VERSION.SDK_INT >= 18) {
            b.setBackgroundColor(getColor(color_id));
        }
        else{
            b.setBackgroundColor(getResources().getColor(color_id));
        }

    }

    private void setUpNumberPicker(){
        mNumberPicker = (NumberPicker) findViewById(R.id.edit_time);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(10);
        mNumberPicker.setValue(4);
    }
    private void setUpPromptEditer(){
        mEditText = (EditText) findViewById(R.id.reflect_prompt_edit);
    }

    private void setUpOnTouchButton(final Button b){
        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        updateColor(b, R.color.colorPrimary);
                        return true;
                    case MotionEvent.ACTION_UP:
                        updateColor(b, R.color.colorPrimaryDark);
                        v.callOnClick();
                        return true;
                }
                return false;
            }
        });
    }

    private void setUpDisableButton(){
        final Button disableButton = (Button) findViewById(R.id.disable_button);

        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GappScreen.stopForegroundService();
            }
        });
        setUpOnTouchButton(disableButton);



    }
    private void setUpEnableButton(){
        Button enableButton = (Button) findViewById(R.id.enable_button);

        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GappScreen.updateTime(mNumberPicker.getValue());
                GappScreen.updateMessage(mEditText.getText().toString());
                GappScreen.launchForegroundService(getApplicationContext());
            }
        });

        setUpOnTouchButton(enableButton);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customize);

        setUpNumberPicker();
        setUpPromptEditer();
        setUpDisableButton();
        setUpEnableButton();



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getString(R.string.customize_title));
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
