package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

public class AppsDB extends Observable {
    private static AppsDB sAppsDB;

    private static List<PackageInfo> mAllAppsOnPhone;
    private static List<PackageInfo> mBlockedApps;
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
        mBlockedApps = new ArrayList<>();
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

    public PackageInfo getOneApp(int position, boolean blocked) {
        if (blocked) {
            if (!mBlockedApps.isEmpty()) {
                return mBlockedApps.get(position);
            } else {
                return null;
            }
        } else {
            return mAllAppsOnPhone.get(position);
        }
    }

    public PackageManager getPackageManager(){
        return packageManager;
    }

    public int getSize(boolean blocked) {
        if (blocked) {
            if (!mBlockedApps.isEmpty()) {
                return mBlockedApps.size();
            } else {
                return 0;
            }
        } else {
            return mAllAppsOnPhone.size();
        }
    }

    public void blockedApps(int position, boolean blocked){
        if(blocked){
            deleteAppFromBlockedList(mBlockedApps.get(position));
        } else{
            updateBlockedApps(position);
        }
    }
    private void addAppToBlockedList(PackageInfo packageInfo){
        mBlockedApps.add(packageInfo);
        updateObservers();
    }

    private void deleteAppFromBlockedList(PackageInfo appName){
        mBlockedApps.remove(appName);
        updateObservers();
    }

    private void updateBlockedApps(int position){
        PackageInfo packageInfo = mAllAppsOnPhone.get(position);
        if(mBlockedApps.contains(packageInfo)){
            deleteAppFromBlockedList(packageInfo);
        } else{
            addAppToBlockedList(packageInfo);
        }
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
}
