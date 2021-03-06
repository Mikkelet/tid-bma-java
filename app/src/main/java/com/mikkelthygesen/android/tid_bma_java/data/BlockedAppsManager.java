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


/**
 * Class that manages the blocked apps
 */
public class BlockedAppsManager {

    public static final String BLOCKEDAPPS = "blockedapps";

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
     * Gets all the apps currently installed on the phone
     * @param activity
     * @return
     */
    public static List<BlockedItem> collectAllApplicationsOnPhone(FragmentActivity activity) {
        ArrayList<BlockedItem> blockedItems = new ArrayList<>();

        PackageManager packageManager = activity.getPackageManager();

        List<PackageInfo> packageInfoList =  packageManager.
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

    /**
     * Collect all blocked applications
     * @param activity activity needed to access package manager
     * @return a list of blocked items
     */
    public static List<BlockedItem> collectAllBlockedApplications(FragmentActivity activity) {

        ArrayList<BlockedItem> blockedItems = new ArrayList<>();
        PackageManager packageManager = activity.getPackageManager();

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

    /**
     * sort list of blocked items
     * @param blockedItems
     * @param packageManager
     * @return
     */
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

    /**
     * load blocked apps set from shared preferences
     * @param activity
     * @return
     */
    private static Set<String> loadBlockedApps(FragmentActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(BLOCKEDAPPS, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Set<String> stringSet = sharedPreferences.getStringSet(BLOCKEDAPPS, new HashSet<String>());
            return stringSet;
        }
        return new HashSet<>();
    }

    /**
     * check if package is a system package
     * @param pkgInfo
     * @return true if package is system package
     */
    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }



}
