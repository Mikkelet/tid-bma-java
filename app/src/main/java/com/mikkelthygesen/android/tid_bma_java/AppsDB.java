package com.mikkelthygesen.android.tid_bma_java;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mikkelthygesen.android.tid_bma_java.Storage.BlockMyAppSqlite;
import com.mikkelthygesen.android.tid_bma_java.Storage.SQLBaseHelper;
import com.mikkelthygesen.android.tid_bma_java.Storage.WrapperCursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;


public class AppsDB extends Observable {
    private static AppsDB sAppsDB;

    private static List<PackageInfo> mAllAppsOnPhone;
    private static List<PackageInfo> mBlockedApps;
    private static List<PackageInfo> mTemp;
    private static PackageManager packageManager;
    private static SQLiteDatabase mDatabase;


    public static AppsDB get(Context context) {
        if (mBlockedApps == null) {
            packageManager = context.getPackageManager();
            sAppsDB = new AppsDB();
        }

        mDatabase = new SQLBaseHelper(context)
                .getWritableDatabase();

        return sAppsDB;
    }

    public List<BlockedAppItem> getListOfItems() {
        List<BlockedAppItem> blockedAppItems = new ArrayList<>();
        WrapperCursor cursor = queryItems(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                blockedAppItems.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return blockedAppItems;
    }

    private WrapperCursor queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                BlockMyAppSqlite.ItemTable.HEADER,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new WrapperCursor(cursor);
    }


    private AppsDB() {
        mAllAppsOnPhone = new ArrayList<>();
        mBlockedApps = new ArrayList<>();
        mTemp = new ArrayList<>();
        createDB(packageManager);
    }

    private void createDB(PackageManager packageManager) {
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

    private static ContentValues itemValues(BlockedAppItem blockedAppItem) {
        ContentValues values = new ContentValues();

        values.put(BlockMyAppSqlite.ItemTable.Cols.NAME, blockedAppItem.getName());
        values.put(BlockMyAppSqlite.ItemTable.Cols.ISITBLOCKED, blockedAppItem.getIsitblocked());
        values.put(BlockMyAppSqlite.ItemTable.Cols.UUID, blockedAppItem.getId());

        return values;
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
        for (PackageInfo packageInfo : mTemp) {
            BlockedAppItem blockedAppItem = new BlockedAppItem("", "b");
            blockedAppItem.setName(packageInfo.packageName);
            if (blockedAppItem.getIsitblocked() == "b") {
                mBlockedApps.remove(blockedAppItem);
            } else {
                blockedAppItem.setIsitblocked("u");
            }
            addAppToSqliteDB(blockedAppItem);

        }
        mTemp.clear();
        updateObservers();
    }

    private void addAppToSqliteDB(BlockedAppItem blockedAppItem) {
        ContentValues values = itemValues(new BlockedAppItem(blockedAppItem.getName(), blockedAppItem.getIsitblocked(), blockedAppItem.getId()));
        mDatabase.insert(BlockMyAppSqlite.ItemTable.HEADER, null, values);
        setChanged();
        notifyObservers();
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
