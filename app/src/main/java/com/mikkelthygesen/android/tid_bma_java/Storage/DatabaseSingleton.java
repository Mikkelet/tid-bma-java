package com.mikkelthygesen.android.tid_bma_java.Storage;

import android.content.Context;

import androidx.room.Room;

public class DatabaseSingleton {

   private static AppDatabase db;

   public static void initialize(Context context) {

       db = Room.databaseBuilder(context,
               AppDatabase.class, "database-name").build();
   }

   public static AppDatabase getInstance() {
       return db;
   }

}
