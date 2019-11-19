package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.mikkelthygesen.android.tid_bma_java.Storage.BlockedApp;
import com.mikkelthygesen.android.tid_bma_java.Storage.BlockedAppDao;
import com.mikkelthygesen.android.tid_bma_java.Storage.DatabaseSingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

public class AppsDB extends Observable {
    private static AppsDB sAppsDB;

    private static List<PackageInfo> mAllAppsOnPhone;

    private static PackageManager packageManager;

    public static AppsDB get(Context context) {
        if (mAllAppsOnPhone == null) {
            packageManager = context.getPackageManager();
            sAppsDB = new AppsDB();
        }
        return sAppsDB;
    }

    private AppsDB (){
        mAllAppsOnPhone = new ArrayList<>();

        createDB(packageManager);
    }

    private void createDB(PackageManager packageManager){
        List<PackageInfo> packageInfoList = packageManager.
                getInstalledPackages(PackageManager.GET_PERMISSIONS);

        for(PackageInfo pi : packageInfoList){
            boolean systemPackage = isSystemPackage(pi);
            if(!systemPackage){
                mAllAppsOnPhone.add(pi);
            }
        }
        sortDB(mAllAppsOnPhone);
    }

    private void sortDB(List<PackageInfo> list){

        Collections.sort(list, new Comparator<PackageInfo>() {
            public int compare(PackageInfo arg0, PackageInfo arg1) {
                return
                        packageManager.getApplicationLabel(
                                arg0.applicationInfo).toString().compareTo(
                                packageManager.getApplicationLabel(
                                        arg1.applicationInfo).toString());
            }
        });
    }



    public PackageManager getPackageManager(){
        return packageManager;
    }


    public List<PackageInfo> loadBlockedApps(){
        List<PackageInfo> allApps = new ArrayList<>();
        List<BlockedApp> list = DatabaseSingleton.getInstance().blockedAppDao().getAll();
        for (PackageInfo packageInfo : mAllAppsOnPhone) {
            for (BlockedApp blockedApp : list) {
                if (packageInfo.packageName.equals(blockedApp.packageName)) {
                    allApps.add(packageInfo);
                    break;
                }
            }
        }
        return allApps;
    }


    private void updateObservers(){
        sortDB(mBlockedApps);
        this.setChanged();
        notifyObservers();
        clearChanged();
    }
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public void toggleApp(final boolean isChecked, final PackageInfo packageInfo) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isChecked) {
                    BlockedAppDao dao = DatabaseSingleton.getInstance().blockedAppDao();
                    BlockedApp blockedApp = new BlockedApp();
                    blockedApp.packageName = packageInfo.packageName;
                    dao.insertAll(blockedApp);
                } else {
                    BlockedAppDao dao = DatabaseSingleton.getInstance().blockedAppDao();
                    dao.deleteByPackageName(packageInfo.packageName);
                }
            }
        });
        thread.start();


    }
}
