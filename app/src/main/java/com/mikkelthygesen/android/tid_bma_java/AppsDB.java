package com.mikkelthygesen.android.tid_bma_java;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

public class AppsDB extends Observable {
    private static AppsDB sAppsDB;

    //private Context mContext;
    //private SQLiteDatabase mDatabase;
    private static List<PackageInfo> appNames;
    private static PackageManager packageManager;

    public static AppsDB get(Context context) {
        if (appNames == null) {
            sAppsDB = new AppsDB();
            packageManager = context.getPackageManager();
        }
        return sAppsDB;
    }

    private AppsDB (){
        appNames = new ArrayList<>();
    }

    public List<PackageInfo> getAppsDB() {
        return appNames;
    }

    public void sortDB(){

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

    public boolean addApp(PackageInfo packageInfo){
            appNames.add(packageInfo);
            updateObservers();
            return true;
    }

    public  boolean deleteApp (PackageInfo appName){
        appNames.remove(appName);
        return true;
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

    private void updateObservers(){
        this.setChanged();
        notifyObservers();
        clearChanged();
    }
}
