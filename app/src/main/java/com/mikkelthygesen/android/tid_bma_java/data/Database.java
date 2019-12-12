package com.mikkelthygesen.android.tid_bma_java.data;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Central database singleton for global state management
 */
public class Database {
    private boolean isBlocking = true;
    private String exerciseProviderBundleId = "com.duolingo";
    private int exerciseTime = 1;
    private int browseTime = 1;
    private List<Pair<String,String>> lsPairs = new ArrayList<>();

    private static Database instance;

    /**
     * Constructor
     */
    private Database() {
        lsPairs.add(new Pair<>("com.duolingo","Duolingo"));
        lsPairs.add(new Pair<>("com.memrise.android.memrisecompanion","Memrise"));
        lsPairs.add(new Pair<>("com.sololearn","SoloLearn"));
    }

    /**
     * Singleton getter
     * @return
     */
    public static Database getinstance() {
        if (instance == null)
            instance = new Database();

        return instance;
    }

    /**
     * get whether the block is currently active
     * @return
     */
    public boolean getIsBlocking() {
        return isBlocking;
    }

    /**
     * Activates the blocking
     */
    public void activateBlocking() {
        isBlocking = true;
    }

    /**
     * Deactivates the blocking
     */
    public void deactivateBlocking() {
        isBlocking = false;
    }

    /**
     * get the bundle Id for the current exercise provider
     * @return
     */
    public String getExerciseProviderBundleId() {
        return exerciseProviderBundleId;
    }

    /**
     * sets a new exercise provider bundle Id
     * @param exerciseProviderBundleId
     */
    public void setExerciseProviderBundleId(String exerciseProviderBundleId) {
        if(exerciseProviderBundleId == null) return;
        Log.d(TAG, "setExerciseProviderBundleId: "+exerciseProviderBundleId);

        if (exerciseProviderBundleId.startsWith("com."))
            for (Pair<String,String> pair:lsPairs) {
                if (pair.first.equals(exerciseProviderBundleId))
                    this.exerciseProviderBundleId = pair.first;
            }
        else{
            for (Pair<String,String> pair:lsPairs){
                if(pair.second.equals(exerciseProviderBundleId))
                    this.exerciseProviderBundleId = pair.first;
            }
        }
        Log.d(TAG, "setExerciseProviderBundleId: "+this.exerciseProviderBundleId);
    }

    /**
     * get all exercise providers' app names
     * @return list of app names
     */
    public List<String> getProviderAppNames(){
        List ls = new ArrayList<String>();
        for (Pair<String,String> pair:lsPairs){
            ls.add(pair.second);
        }
        return ls;
    }

    /**
     * get the app name of the current exercise provider
     * @return
     */
    public String getSelectedExerciseProviderName(){
        String name = "";
        for (Pair<String,String> pair:lsPairs){
            if(pair.first.equals(exerciseProviderBundleId))
                name = pair.second;
        }
        return name;
    }

    /**
     * set new fun time in minutes
     * @param browseTime
     */
    public void setFunTime(int browseTime){
        if (browseTime > 0 && browseTime <= 5)
            this.browseTime = browseTime;
    }

    /**
     * get the set time to browse blocked apps in minutes
     * @return how many minutes you can use your blocked apps
     */
    public int getFunTime(){
        return browseTime;
    }

    /**
     * Set how many minutes the block should work
     * @param exerciseTime
     */
    public void setBlockTime(int exerciseTime){
        if(exerciseTime > 0 && exerciseTime <= 5){
            this.exerciseTime = exerciseTime;
        }
    }

    /**
     * get how many minutes the block should work
     * @return
     */
    public int getBlockTime(){
        return exerciseTime;    }
}

