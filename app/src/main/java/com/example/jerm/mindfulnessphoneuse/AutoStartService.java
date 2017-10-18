package com.example.jerm.mindfulnessphoneuse;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jerm on 10/17/17.
 */

public class AutoStartService extends IntentService {

    public  AutoStartService(){
        super("mindfulness-auto-start-service");
    }

    @Override
    protected void onHandleIntent(Intent intent){

        Log.d("servicer", "service started");
    }
}
