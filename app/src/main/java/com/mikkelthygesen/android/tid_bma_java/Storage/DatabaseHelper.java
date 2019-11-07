package com.mikkelthygesen.android.tid_bma_java.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "User_specified_settings";
    public static final String TABLE_NAME = "settings_database";
    public static final String COLUMN1 = "ID";
    public static final String COLUMN2 = "list_of_applications_blocked";
    public static final String COLUMN3 = "timer_settings";
    public static final String COLUMN4 = "chosen_redirection_micro_learnings";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, list_of_applications_blocked TEXT, timer_settings TEXT, chosen_redirection_micro_learnings TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        this.onCreate(db);
    }

    public boolean insertData(String list_apps, String timer_settings, String chosen_learnings) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN2, list_apps);
        contentValues.put(COLUMN3, timer_settings);
        contentValues.put(COLUMN4, chosen_learnings);
        long result = database.insert(TABLE_NAME, null, contentValues);

        //check if value is -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}