package com.example.jerm.mindfulnessphoneuse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

import java.util.concurrent.Semaphore;


/**
 * Created by jerm on 10/25/17.
 */

public class OurReceivers {

    public static class BootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            GappScreen.launchForegroundService(context);
        }

    }


    public static class StartReceiver extends BroadcastReceiver {
        private Semaphore finished;
        private Boolean stopRequested;
        public StartReceiver() {
            //when this is acquirable the receiver is done
            stopRequested = false;
            finished = new Semaphore(1);
            finished.acquireUninterruptibly();
        }


        @Override
        public void onReceive(Context context, Intent intent) {

            if (!stopRequested) {
                Intent start_intent = new Intent(context, GappScreen.class);
                context.startActivity(start_intent);
            }
            finished.release();
        }

        public void wait_until_finished() {
            //this lock will never be acquired since the receiver never lets go
            OurReceivers.betterAcquire(finished);
            finished.release();
        }

        public void stop(){
            stopRequested = true;
        }
    }

    public static void betterAcquire(Semaphore lock){
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
    }


}
