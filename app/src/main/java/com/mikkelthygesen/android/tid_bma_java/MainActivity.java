package com.mikkelthygesen.android.tid_bma_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.app.BackgroundManager;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

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


        mBottomNavigationMenu = findViewById(R.id.bottomNavigationView);
        mBottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_start_session:
                        StartSession startSession = StartSession.newInstance();
                        openFragment(startSession);
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
}
