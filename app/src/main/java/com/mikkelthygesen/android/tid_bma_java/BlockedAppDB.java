package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mikkelthygesen.android.tid_bma_java.BlackList.BLOCKEDAPPS;

public class BlockedAppDB {


    public static void saveBlockedApps(FragmentActivity activity, Set<String> checkPackageNames) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(BLOCKEDAPPS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(BLOCKEDAPPS, checkPackageNames);
        edit.apply();
    }
    public static List<BlockedItem> collectAllApplicationsOnPhone(PackageManager packageManager, FragmentActivity activity) {
        ArrayList<BlockedItem> blockedItems = new ArrayList<>();

        List<PackageInfo> packageInfoList = packageManager.
                getInstalledPackages(PackageManager.GET_PERMISSIONS);

        Set<String> blockedPackageNames = loadBlockedApps(activity);
        for (PackageInfo pi : packageInfoList) {
            if (!isSystemPackage(pi)) {
                BlockedItem blockedItem = new BlockedItem(pi);
                blockedItem.setChecked(blockedPackageNames.contains(pi.packageName));
                blockedItems.add(blockedItem);
            }
        }
        return blockedItems;
    }

    private static Set<String> loadBlockedApps(FragmentActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(BLOCKEDAPPS, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Set<String> stringSet = sharedPreferences.getStringSet(BLOCKEDAPPS, new HashSet<String>());
            return stringSet;
        }
        return new HashSet<>();
    }

    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }



}