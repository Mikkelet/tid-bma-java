package com.mikkelthygesen.android.tid_bma_java.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.Nullable;

import com.mikkelthygesen.android.tid_bma_java.data.Database;

/**
 * Service for when you can browse your blocked apps
 */
public class FunService extends Service {



    public FunService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long startTime = System.currentTimeMillis();
        long time = startTime;
        long endTime = startTime + Database.getinstance().getFunTime() * 1000;

        // Timer
        while(time<endTime)
        {
            time = System.currentTimeMillis();
            Log.d("Exercise Service", "onStartCommand: " + ((endTime - time) / 1000));
            if ((endTime - time) % 1000 == 0)
                Log.d("Block Service", "onStartCommand: One second passed");
        }
        // activates block once timer is finished
        Database.getinstance().activateBlocking();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
