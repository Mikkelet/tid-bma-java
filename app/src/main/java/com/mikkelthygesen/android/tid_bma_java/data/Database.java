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
    private Set<String> blockedAppsBundleIds = new HashSet<>();
    private int exerciseTime = 1;
    private int browseTime = 1;
    private List<Pair<String,String>> lsPairs = new ArrayList<>();

    private static Database instance;

    private Database() {
        blockedAppsBundleIds.add("com.facebook.katana");
        blockedAppsBundleIds.add("com.instagram.android");

        lsPairs.add(new Pair<>("com.duolingo","Duolingo"));
        lsPairs.add(new Pair<>("com.memrise.android.memrisecompanion","Memrise"));
        lsPairs.add(new Pair<>("com.sololearn","SoloLearn"));
    }

    public static Database getinstance() {
        if (instance == null)
            instance = new Database();

        return instance;
    }

    public boolean getIsBlocking() {
        return isBlocking;
    }

    public void activateBlocking() {
        isBlocking = true;
    }

    public void deactivateBlocking() {
        isBlocking = false;
    }

    public String getExerciseProviderBundleId() {
        return exerciseProviderBundleId;
    }

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

    public List<String> getProviderAppNames(){
        List ls = new ArrayList<String>();
        for (Pair<String,String> pair:lsPairs){
            ls.add(pair.second);
        }
        return ls;
    }
    public String getSelectedExerciseProviderName(){
        String name = "";
        for (Pair<String,String> pair:lsPairs){
            if(pair.first.equals(exerciseProviderBundleId))
                name = pair.second;
        }
        return name;
    }
    public void setBrowseTime(int browseTime){
        if (browseTime > 0 && browseTime <= 5)
            this.browseTime = browseTime;
    }
    public int getBrowseTime(){
        return browseTime * 1000;
    }
    public void setExerciseTime(int exerciseTime){
        if(exerciseTime > 0 && exerciseTime <= 5){
            this.exerciseTime = exerciseTime;
        }
    }
    public int getExerciseTime(){
        return exerciseTime * 1000;
    }
}

