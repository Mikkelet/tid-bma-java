package com.mikkelthygesen.android.tid_bma_java;

import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Database {

    public int bubbleSeekBarExercise;
    private boolean isBlocking = true;
    private String exerciseProviderBundleId = "com.duolingo";
    private Set<String> blockedAppsBundleIds = new HashSet<>();
    private int timeLeft = 5;
    private int exerciseTime = 1;
    private int browseTime = 1;
    private List<Pair<String,String>> lsPairs = new ArrayList<>();

    private static Database instance;

    private Database() {
        blockedAppsBundleIds.add("com.facebook.katana");
        blockedAppsBundleIds.add("com.instagram.android");

        lsPairs.add(new Pair<String, String>("com.duolingo","Duolingo"));
        lsPairs.add(new Pair<String, String>("com.memrise.android.memrisecompanion","Memrise"));
        lsPairs.add(new Pair<String, String>("com.sololearn","SoloLearn"));
    }
    
    public synchronized void startTimer(){
        Log.d(TAG, "startTimer: Started");
        try {
             wait(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "startTimer: Stopped!");
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
    public void setBlockedAppsBundleIds(Set<String> blockedAppsBundleIds) {
        this.blockedAppsBundleIds = blockedAppsBundleIds;
    }
    public boolean isAppBlocked(String bundleId) {
        return blockedAppsBundleIds.contains(bundleId);
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

    static class SharePrefs{
        public static final String EXERCISE_PROVIDER = "exercise_provider";
    }
}

