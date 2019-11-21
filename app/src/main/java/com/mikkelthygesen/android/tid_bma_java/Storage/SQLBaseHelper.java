package com.mikkelthygesen.android.tid_bma_java.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "BlockMyApp";


    public SQLBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tableName = BlockMyAppSqlite.ItemTable.HEADER;
        String uuid = BlockMyAppSqlite.ItemTable.Cols.UUID;
        String appName = BlockMyAppSqlite.ItemTable.Cols.NAME;
        String IsItBlocked = BlockMyAppSqlite.ItemTable.Cols.ISITBLOCKED;

        db.execSQL("CREATE TABLE " + tableName + "(" + " _id integer primary key autoincrement, " +
                uuid + " UUID, " +
                appName + " NAME, " +
                IsItBlocked + " ISITBLOCKED);"
        );


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
