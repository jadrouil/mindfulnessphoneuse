package com.example.jerm.mindfulnessphoneuse;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;



/**
 * Created by jerm on 10/17/17.
 */

public class AutoStartService extends IntentService {
    private static int FOREGROUND_ID = 734;


    public  AutoStartService(){
        super("mindfulness-auto-start-service");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        startForeground(FOREGROUND_ID, buildForegroundNotification("foregroundnotif"));
        Log.d("service", "starting");

        while (true) {
            OurReceivers.StartReceiver auto_starter = new OurReceivers.StartReceiver();
            IntentFilter intent_filter2 = new IntentFilter("android.intent.action.SCREEN_OFF");
            registerReceiver(auto_starter, intent_filter2);
            auto_starter.wait_until_finished();
            unregisterReceiver(auto_starter);

//            stopForeground(false);
        }
    }


    private Notification buildForegroundNotification(String filename) {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true);

        b.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setTicker("Ticker")
                .setVisibility(NotificationCompat.VISIBILITY_SECRET);

        return(b.build());
    }
}
