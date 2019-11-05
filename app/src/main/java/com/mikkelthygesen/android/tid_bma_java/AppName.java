package com.mikkelthygesen.android.tid_bma_java;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Observable;
import java.util.UUID;

public class AppName extends Observable {
    private PackageInfo packageInfo;

    public AppName(PackageInfo packageInfo) {
      this.packageInfo = packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
      this.packageInfo = packageInfo;
      updateObservers();
    }

    private void updateObservers(){
        this.setChanged();
        notifyObservers();
        clearChanged();
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }
}
