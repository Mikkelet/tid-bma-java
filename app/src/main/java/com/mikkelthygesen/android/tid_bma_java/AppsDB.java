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

    private static List<PackageInfo> appNames;
    private static List<PackageInfo> appNamesBlacklist;
    private static PackageManager packageManager;

    public static AppsDB get(Context context) {
        if (appNames == null) {
            packageManager = context.getPackageManager();
            sAppsDB = new AppsDB();
        }
        return sAppsDB;
    }

    private AppsDB (){
        appNames = new ArrayList<>();
        appNamesBlacklist = new ArrayList<>();
        createDB(packageManager);
    }

    public static List<PackageInfo> getAppNamesBlacklist() {
        return appNamesBlacklist;
    }

    public static void setAppNamesBlacklist(PackageInfo packageInfo) {
        appNamesBlacklist.add(packageInfo);
    }

    public List<PackageInfo> getAppsDB() {
        return appNames;
    }

    private void createDB(PackageManager packageManager){
        List<PackageInfo> packageInfoList = packageManager.
                getInstalledPackages(PackageManager.GET_PERMISSIONS);

        for(PackageInfo pi : packageInfoList){
            boolean systemPackage = isSystemPackage(pi);
            if(!systemPackage){
                appNames.add(pi);
            }
        }
        sortDB();
    }

    private void sortDB(){

        Collections.sort(appNames, new Comparator<PackageInfo>() {
            public int compare(PackageInfo arg0, PackageInfo arg1) {
                return
                        packageManager.getApplicationLabel(
                                arg0.applicationInfo).toString().compareTo(
                                packageManager.getApplicationLabel(
                                        arg1.applicationInfo).toString());
            }
        });
    }

    private void addApp(PackageInfo packageInfo){
            appNamesBlacklist.add(packageInfo);
            updateObservers();
    }

    public void deleteApp (PackageInfo appName){
        appNamesBlacklist.remove(appName);
        updateObservers();
    }

    public PackageInfo getApp (PackageInfo appName){
        for(PackageInfo a : appNames){
            if(!a.equals(appName)) {
            } else{
                return a;
            }
        }
        return appName;
    }
    public void blockedApps(PackageInfo packageInfo){
        if(appNamesBlacklist.contains(packageInfo)){
            deleteApp(packageInfo);
        } else{
            addApp(packageInfo);
        }
    }

    private void updateObservers(){
        this.setChanged();
        notifyObservers();
        clearChanged();
    }
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
