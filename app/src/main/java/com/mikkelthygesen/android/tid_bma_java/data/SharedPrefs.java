package com.mikkelthygesen.android.tid_bma_java.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.mikkelthygesen.android.tid_bma_java.data.Database;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefs {
    Context mContext;
    private static final int MAX_VALUE = 5;
    private static final String SHARED_PREFS = "sp";
    private static final String BLOCK_TIME = "block_time";
    private static final String FUN_TIME = "fun_time";

    public SharedPrefs(Context context) {
        mContext = context;
    }

    /**
     * Set a new block time.
     * @param value the new block time in minutes that the block should be in effect
     */
    public void setBlockTime(int value){
        // get the shared preferences file "sp"
        SharedPreferences sharedPref = mContext.getSharedPreferences("sp", Context.MODE_PRIVATE);
        // if it is null, just return
        if (sharedPref == null) return;
        // save the progress under the key "blocked Apps progress"
        sharedPref.edit().putInt("blockedAppsProgress", value).apply();
    }
    public void getBlockTime(){
        SharedPreferences sharedPref = mContext.getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if (sharedPref == null) {
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        int blockedAppsProgress = sharedPref.getInt(BLOCK_TIME, 1);
        if (blockedAppsProgress > MAX_VALUE) blockedAppsProgress = MAX_VALUE;
        Database.getinstance().setBrowseTime(blockedAppsProgress);
    }
    public void setFunTime(int value){
        // get the shared preferences file "sp"
        SharedPreferences sharedPref = mContext.getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if it is null, just return
        if (sharedPref == null) return;

        // save the progress under the key "progress"
        sharedPref.edit().putInt(FUN_TIME, value).apply();
    }
    public void getFunTime(){

        SharedPreferences sharedPref = mContext.getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if (sharedPref == null) {
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        int exerciseProgress = sharedPref.getInt(FUN_TIME, 1);
        if (exerciseProgress > MAX_VALUE) exerciseProgress = MAX_VALUE;
        Database.getinstance().setExerciseTime(exerciseProgress);
    }
    public void setBlockedApps(){

    }
    public List getBlockedApps(){
        return new ArrayList();
    }
    public void setProviderApp(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        if(sharedPref == null) return;
        sharedPref.edit().putString(Database.SharePrefs.EXERCISE_PROVIDER, Database.getinstance().getSelectedExerciseProviderName()).apply();
    }
}
