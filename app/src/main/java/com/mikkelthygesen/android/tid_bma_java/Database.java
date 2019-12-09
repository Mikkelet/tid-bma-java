package com.mikkelthygesen.android.tid_bma_java;

import android.os.AsyncTask;
import android.util.Log;

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
    private Map<String,String> mapProviderApps = new HashMap<>();



    private static Database instance;

    private Database() {
        blockedAppsBundleIds.add("com.facebook.katana");
        blockedAppsBundleIds.add("com.instagram.android");

        mapProviderApps.put("com.duolingo","Duolingo");
        mapProviderApps.put("com.memrise.android.memrisecompanion","Memrise");
        mapProviderApps.put("com.sololearn","SoloLearn");
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
        if (exerciseProviderBundleId.startsWith("com."))
            this.exerciseProviderBundleId = exerciseProviderBundleId;
    }
    public void setBlockedAppsBundleIds(Set<String> blockedAppsBundleIds) {
        this.blockedAppsBundleIds = blockedAppsBundleIds;
    }
    public boolean isAppBlocked(String bundleId) {
        return blockedAppsBundleIds.contains(bundleId);
    }

    public List<String> getProviderAppNames(){
        return new ArrayList<>(mapProviderApps.values());
    }
    public String getSelectedExerciseProviderName(){
        return mapProviderApps.get(exerciseProviderBundleId);
    }
    public int getTimeLeftMilliseconds(){
        return timeLeft *1000;//* 60000;
    }

    static class SharePrefs{
        public static final String EXERCISE_PROVIDER = "exercise_provider";
    }
}

