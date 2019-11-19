package com.mikkelthygesen.android.tid_bma_java;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.List;

public class BMAService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    ActivityManager mActivityManager;
    public BMAService(ActivityManager activityManager) {
        mActivityManager = activityManager;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                1);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        super.onCreate();
    }



    private static class ServiceHandler extends Handler {
        private ActivityManager mActivityManager;
        ServiceHandler(ActivityManager activityManager){
            mActivityManager = activityManager;
        }
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.



            List<ActivityManager.RunningAppProcessInfo> processes = mActivityManager.getRunningAppProcesses();
            Log.d("Service Handler", "handleMessage: "+processes.get(0).processName);
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }

    class asyncTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
