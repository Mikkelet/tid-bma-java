package com.mikkelthygesen.android.tid_bma_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.app.BackgroundManager;


import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private StartSession mStartSessionFragment = new StartSession();

    private BottomNavigationView mBottomNavigationMenu;

    @Override
    protected void onResume() {
        super.onResume();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);

        System.out.println("---------------------------------------------------------------------\n");
        for (PackageInfo p:packages) {
            System.out.println(p.packageName);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDuolingoTime(getBaseContext());
        BackgroundListener bglistener = new BackgroundListener();
        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }else
        {
            try {
                getPackageManager().getPackageInfo("com.duolingo",PackageManager.GET_ACTIVITIES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

        startService(new Intent(this,BlockService.class));


        openFragment(mStartSessionFragment);
        mBottomNavigationMenu = findViewById(R.id.bottomNavigationView);
        mBottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_start_session:
                        openFragment(mStartSessionFragment);
                        return true;
                    case R.id.navigation_blacklist:
                        BlackList blackList = BlackList.newInstance();
                        openFragment(blackList);
                        return true;
                    case R.id.navigation_timer:
                        Timer timer = Timer.newInstance();
                        openFragment(timer);
                        return true;
                }
                return false;
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void getDuolingoTime(Context context){
        long startTime = System.currentTimeMillis() - 7200*1000; // 7200 seconds i.e. 2 hrs
        long endTime = System.currentTimeMillis();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> lUsageStatsMap = mUsageStatsManager.queryAndAggregateUsageStats(startTime,endTime);
        UsageStats duolingoTime =  lUsageStatsMap.get("com.duolingo");
        System.out.println(duolingoTime.getTotalTimeInForeground());
        Log.d("Main Activity", "getDuolingoTime: "+duolingoTime.getTotalTimeInForeground());
    }

    private class BackgroundListener extends Service{
        private static final int NOTIF_ID = 1;
        private static final String NOTIF_CHANNEL_ID = "Channel_Id";

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("Main Activity BGL", "onStartCommand: "+intent.getPackage()+", flags: "+flags+", startId: "+startId);
            startForeground();
            return super.onStartCommand(intent, flags, startId);
        }

        private void startForeground() {
            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                    NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.activate_block_circle)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntent)
                    .build());
        }
    }
}
