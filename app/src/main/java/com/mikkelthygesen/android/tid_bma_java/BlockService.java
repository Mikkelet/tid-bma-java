package com.mikkelthygesen.android.tid_bma_java;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BlockService extends Service {


    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("block service", "onStartCommand: service started!");

            long startTime = System.currentTimeMillis();
            long time = startTime;
            long endTime = startTime + Database.getinstance().getTimeLeftMilliseconds();

            while(time < endTime){
                time = System.currentTimeMillis();
                Log.d("Block Service", "onStartCommand: "+((endTime-time)/1000));
                if((endTime-time)%1000 == 0)
                    Log.d("Block Service", "onStartCommand: One second passed");
            }
            Database.getinstance().deactivateBlocking();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

}
