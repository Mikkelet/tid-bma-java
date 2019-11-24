package com.mikkelthygesen.android.tid_bma_java;

import java.util.HashSet;
import java.util.Set;

public class Database {

    public int bubbleSeekBarExercise;
    private boolean isBlocking = true;
    private String exerciseProviderBundleId = "com.duolingo";
    private Set<String> blockedAppsBundleIds = new HashSet<>();


    private static Database instance;

    private Database() {
        blockedAppsBundleIds.add("com.facebook.katana");
        blockedAppsBundleIds.add("com.instagram.android");
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
        if (exerciseProviderBundleId.startsWith("com."))
            this.exerciseProviderBundleId = exerciseProviderBundleId;
    }

    public void setBlockedAppsBundleIds(Set<String> blockedAppsBundleIds) {
        this.blockedAppsBundleIds = blockedAppsBundleIds;
    }

    public boolean isAppBlocked(String bundleId) {
        return blockedAppsBundleIds.contains(bundleId);
    }
}
