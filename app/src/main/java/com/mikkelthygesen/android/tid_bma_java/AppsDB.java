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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class AppsDB extends Observable {
    private static AppsDB sAppsDB;

    private static List<PackageInfo> mAllAppsOnPhone;
    private static List<PackageInfo> mBlockedApps;
    private static List<PackageInfo> mTemp;
    private static PackageManager packageManager;
    private static SQLiteDatabase mDatabase;
    private static Context context;


    public static AppsDB get(Context context) {
        if (mAllAppsOnPhone == null) {
            packageManager = context.getPackageManager();
            sAppsDB = new AppsDB();
        }

        mDatabase = new SQLBaseHelper(context)
                .getWritableDatabase();

        return sAppsDB;
    }

    public BlockedAppItem getItem(UUID id) {
        WrapperCursor cursor = queryItems(
                BlockMyAppSqlite.ItemTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();

        } finally {
            cursor.close();
        }
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

    public static void setmAllAppsOnPhone(List<PackageInfo> mAllAppsOnPhone) {
        AppsDB.mAllAppsOnPhone = mAllAppsOnPhone;
    }

    public static List<PackageInfo> getmBlockedApps() {
        return mBlockedApps;
    }

    public static void setmBlockedApps(List<PackageInfo> mBlockedApps) {
        AppsDB.mBlockedApps = mBlockedApps;
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
        values.put(BlockMyAppSqlite.ItemTable.Cols.UUID, blockedAppItem.getId().toString());

        return values;
    }


    //Add or remove depending on state
    public void updateBlockedApps() {
        mBlockedApps.clear();
        mBlockedApps.addAll(mTemp);
        for (PackageInfo packageInfo : mTemp) {
            BlockedAppItem blockedAppItem = new BlockedAppItem("", "u");
            blockedAppItem.setName(packageInfo.toString());
            blockedAppItem.toggleApp("b");
            ContentValues values = itemValues(new BlockedAppItem(blockedAppItem.getName(), blockedAppItem.getIsitblocked(), UUID.randomUUID()));
            mDatabase.insert(BlockMyAppSqlite.ItemTable.HEADER, null, values);
            System.out.println(getListOfItems() +"lol");
            setChanged();
            notifyObservers();

            System.out.println();
        }
       // mTemp.clear();
        updateObservers();
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
