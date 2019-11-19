package com.mikkelthygesen.android.tid_bma_java.Storage;

import android.content.Context;

import androidx.room.Room;

public class DatabaseSingleton {

    private Context context;
    private static DatabaseSingleton mInstance;

   private static AppDatabase db;


    private DatabaseSingleton(Context context) {
        this.context = context ;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        db = Room.databaseBuilder(context, AppDatabase.class, "BlockMyApp").build();
    }


    public static synchronized DatabaseSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseSingleton(context);
        }
        return mInstance;
    }


   public static AppDatabase getAppDatabase() {
       return db;
   }

}
