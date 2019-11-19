package com.mikkelthygesen.android.tid_bma_java.Storage;

import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.mikkelthygesen.android.tid_bma_java.BlockedAppItem;

import java.util.UUID;

public class WrapperCursor extends CursorWrapper {
    public WrapperCursor(Cursor cursor) {
        super(cursor);
    }

    public BlockedAppItem getItem() {

        String uuid = getString(getColumnIndex(BlockMyAppSqlite.ItemTable.Cols.UUID));
        String appName = String.valueOf((getColumnIndex(BlockMyAppSqlite.ItemTable.Cols.NAME)));
        String isItBlocked = getString(getColumnIndex(BlockMyAppSqlite.ItemTable.Cols.ISITBLOCKED));

        BlockedAppItem blockedAppItem = new BlockedAppItem(appName, isItBlocked);
        blockedAppItem.setName(appName);
        blockedAppItem.setIsitblocked("u");
        blockedAppItem.setmId(UUID.randomUUID());
        return blockedAppItem;
    }

}
