package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;


public class AppsDB extends Observable {
    private static AppsDB sAppsDB;

    private static List<PackageInfo> mAllAppsOnPhone;

    private static List<PackageInfo> mBlockedApps;
    private static List<PackageInfo> mTemp;
    private static PackageManager packageManager;
    private static SharedPreferences mDatabase;


    public static AppsDB get(Context context) {
        if (sAppsDB == null) {
            packageManager = context.getPackageManager();
            sAppsDB = new AppsDB();
        }

        mDatabase = new SharedPreferences() {
            @Override
            public Map<String, ?> getAll() {
                return null;
            }

            @Nullable
            @Override
            public String getString(String key, @Nullable String defValue) {
                return null;
            }

            @Nullable
            @Override
            public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
                return null;
            }

            @Override
            public int getInt(String key, int defValue) {
                return 0;
            }

            @Override
            public long getLong(String key, long defValue) {
                return 0;
            }

            @Override
            public float getFloat(String key, float defValue) {
                return 0;
            }

            @Override
            public boolean getBoolean(String key, boolean defValue) {
                return false;
            }

            @Override
            public boolean contains(String key) {
                return false;
            }

            @Override
            public Editor edit() {
                return null;
            }

            @Override
            public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

            }

            @Override
            public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

            }
        };

        return sAppsDB;
    }

    public static List<PackageInfo> getmBlockedApps() {
        return mBlockedApps;
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

    public static List<PackageInfo> getmAllAppsOnPhone() {
        return mAllAppsOnPhone;
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
//Save to shared preferences
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

    private void updateObservers() {
        sortDB(mBlockedApps);
        this.setChanged();
        notifyObservers();
        clearChanged();
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }


}
