package com.mikkelthygesen.android.tid_bma_java.Storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BlockedApp.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BlockedAppDao blockedAppDao();
}