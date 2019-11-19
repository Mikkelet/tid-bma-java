package com.mikkelthygesen.android.tid_bma_java.Storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BlockedApp {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "package_name")
    public String packageName;
}