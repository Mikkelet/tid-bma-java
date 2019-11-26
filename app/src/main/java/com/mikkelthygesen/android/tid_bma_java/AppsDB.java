package com.mikkelthygesen.android.tid_bma_java;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AppsDB extends Fragment {
    private static AppsDB sAppsDB;

    private static List<PackageInfo> mAllAppsOnPhone;
    private static List<PackageInfo> mBlockedApps;
    private static List<PackageInfo> mTemp;
    private static PackageManager packageManager;




    public static AppsDB get(Context context) {
        if (sAppsDB == null) {
            packageManager = context.getPackageManager();
            sAppsDB = new AppsDB();
        }

        return sAppsDB;
    }

    private AppsDB() {
        mAllAppsOnPhone = new ArrayList<>();
        mBlockedApps = new ArrayList<>();
        mTemp = new ArrayList<>();
        collectAllApplicationsOnPhone(packageManager);
    }

    private void collectAllApplicationsOnPhone(PackageManager packageManager) {
        List<PackageInfo> packageInfoList = packageManager.
                getInstalledPackages(PackageManager.GET_PERMISSIONS);

        for (PackageInfo pi : packageInfoList) {
            boolean systemPackage = isSystemPackage(pi);
            if (!systemPackage) {
                mAllAppsOnPhone.add(pi);
            }
        }
        sortDB(mAllAppsOnPhone);
    }

    private void sortDB(List<PackageInfo> list) {

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

    //Used when generating the recyclerable views
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


    public PackageManager getPackageManager() {
        return packageManager;
    }

    public int getSize(boolean blocked) {
        if (blocked) {
            return mBlockedApps.size();
        } else {
            return mAllAppsOnPhone.size();
        }
    }


    //Add or remove depending on state
    public void updateBlockedApps() {
        if (mBlockedApps.size() == 0) {
            mBlockedApps.addAll(mTemp);
        } else {
            HashSet singleMaker = new HashSet(mBlockedApps);
            singleMaker.addAll(mTemp);
            mBlockedApps.clear();
            mBlockedApps.addAll(singleMaker);


        }
        blockOrUnBlock();
        mTemp.clear();
        updateObservers();
    }

    private void blockOrUnBlock() {
        for (PackageInfo packageInfo : mTemp) {
            BlockedAppItem blockedAppItem = new BlockedAppItem("", "b");
            blockedAppItem.setName(packageInfo.packageName);
            if (blockedAppItem.getIsitblocked() == "b") {
                mBlockedApps.remove(blockedAppItem);


            } else {
                blockedAppItem.setIsitblocked("u");
            }
        }
    }

    //Functionality for when user is interacting with recyclerable views
    public void isItBlocked(int position, boolean blocked) {
        if (blocked) {
            PackageInfo packageInfo = mBlockedApps.get(position);
            updateBlockedApps(packageInfo);
        } else {
            PackageInfo packageInfo = mAllAppsOnPhone.get(position);
            updateBlockedApps(packageInfo);
        }
    }

    private void updateBlockedApps(PackageInfo packageInfo) {
        if (mTemp.contains(packageInfo)) {
            mTemp.remove(packageInfo);
        } else {
            mTemp.add(packageInfo);
        }
    }

    //Remove button's method
    public void removeBlockedApps() {
        mBlockedApps.removeAll(mTemp);
        mTemp.clear();
        updateObservers();
    }

    //Checkbox checked or not
    public boolean blockedApp(PackageInfo packageInfo) {
        if (mBlockedApps.contains(packageInfo)) {
            return true;
        } else {
            return false;
        }
    }

    public void load(Set<String> blockedAppPackageNames) {

        List<PackageInfo> blocked = new ArrayList<>();
        for (PackageInfo packageInfo : mAllAppsOnPhone) {
            if (blockedAppPackageNames.contains(packageInfo.packageName)) {
                blocked.add(packageInfo);
            }
        }

        mTemp.clear();
        mBlockedApps.clear();
        mBlockedApps.addAll(blocked);
        updateBlockedApps();
    }

    public Set<String> getBlockedAppNames() {

        Set<String> blockedAppPackageNames = new HashSet<>();
        for (PackageInfo mBlockedApp : mBlockedApps) {
            blockedAppPackageNames.add(mBlockedApp.packageName);
        }

        return blockedAppPackageNames;
    }

    private void updateObservers() {
        sortDB(mBlockedApps);
//        this.setChanged();
  //      notifyObservers();
    //    clearChanged();
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }



}
