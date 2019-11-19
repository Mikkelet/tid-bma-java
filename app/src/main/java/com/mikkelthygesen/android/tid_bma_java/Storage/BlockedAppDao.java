package com.mikkelthygesen.android.tid_bma_java.Storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BlockedAppDao {
    @Query("SELECT * FROM BlockedApp")
    List<BlockedApp> getAll();

    @Insert
    void insertAll(BlockedApp... blockedApps);

    @Delete
    void delete(BlockedApp blockedApp);

    @Query("SELECT * FROM BlockedApp WHERE first_name LIKE :search " +
            "OR last_name LIKE :search")
     List<BlockedApp> findUserWithName(String search);
}