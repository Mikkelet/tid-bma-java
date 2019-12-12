package com.mikkelthygesen.android.tid_bma_java.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.fragment.app.FragmentActivity;

import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mikkelthygesen.android.tid_bma_java.controllers.BlackList.BLOCKEDAPPS;

/**
 * Class that manages the blocked apps
 */
public class BlockedAppsManager {


    /**
     * Save the current list of blocked apps to Shared Preferences
     * @param activity required activity to make use of Shared Preferences
     * @param checkPackageNames new list of blocked apps that will overwrite previous
     */
    public static void saveBlockedApps(FragmentActivity activity, Set<String> checkPackageNames) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(BLOCKEDAPPS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(BLOCKEDAPPS, checkPackageNames);
        edit.apply();
    }

    /**
     *
     * @param packageManager
     * @param activity
     * @return
     */
    public static List<BlockedItem> collectAllApplicationsOnPhone(final PackageManager packageManager, FragmentActivity activity) {
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
        return sortBlocked(blockedItems,packageManager);
    }

    public static List<BlockedItem> collectAllBlockedApplications(final PackageManager packageManager, FragmentActivity activity) {
        ArrayList<BlockedItem> blockedItems = new ArrayList<>();

        List<PackageInfo> packageInfoList = packageManager.
                getInstalledPackages(PackageManager.GET_PERMISSIONS);

        Set<String> blockedPackageNames = loadBlockedApps(activity);
        if(blockedPackageNames.isEmpty()){
            return blockedItems;
        }
        for (PackageInfo pi : packageInfoList) {
            if (!isSystemPackage(pi)) {
                BlockedItem blockedItem = new BlockedItem(pi);
                blockedItem.setChecked(blockedPackageNames.contains(pi.packageName));
                if(blockedItem.isChecked())
                    blockedItems.add(blockedItem);
            }
        }
        return sortBlocked(blockedItems,packageManager);
    }

    private static List<BlockedItem> sortBlocked(List<BlockedItem> blockedItems, final PackageManager packageManager){
        Collections.sort(blockedItems, new Comparator<BlockedItem>() {
            public int compare(BlockedItem arg0, BlockedItem arg1) {
                return
                        packageManager.getApplicationLabel(
                                arg0.getPackageInfo().applicationInfo).toString().compareTo(
                                packageManager.getApplicationLabel(
                                        arg1.getPackageInfo().applicationInfo).toString());
            }
        });
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
