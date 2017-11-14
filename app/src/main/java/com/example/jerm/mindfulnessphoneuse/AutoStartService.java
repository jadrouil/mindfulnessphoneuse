package com.example.jerm.mindfulnessphoneuse;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;

import java.util.concurrent.Semaphore;


/**
 * Created by jerm on 10/17/17.
 */

public class AutoStartService extends IntentService {
    private static int FOREGROUND_ID = 734;

    private static OurReceivers.StartReceiver auto_starter;
    public  AutoStartService(){
        super("mindfulness-auto-start-service");

    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d("notif", "oncreate");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(
                            "channel_id","channel title", NotificationManager.IMPORTANCE_LOW);
            mChannel.enableLights(false);
            mChannel.enableVibration(false);
            mChannel.setSound(null, null);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(this, "channel_id")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("This is necessary to detect when you turn on your screen")
                    .setSmallIcon(android.R.drawable.ic_dialog_info);


            Notification notification = builder.build();

            Log.d("notif", "starting the notification");
            startForeground(1, notification);


        } else {
            Log.d("notif", "starting the notification else");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("This is necessary to detect when you turn on your screen")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("notif", "handling the intent");
        while (true) {
            auto_starter = new OurReceivers.StartReceiver();
            IntentFilter intent_filter2 = new IntentFilter("android.intent.action.SCREEN_OFF");
            registerReceiver(auto_starter, intent_filter2);
            auto_starter.wait_until_finished();

            unregisterReceiver(auto_starter);
        }
    }


}
