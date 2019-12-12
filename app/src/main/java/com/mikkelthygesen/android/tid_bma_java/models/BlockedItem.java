package com.mikkelthygesen.android.tid_bma_java.models;

import android.content.pm.PackageInfo;

public class BlockedItem {

    private final PackageInfo packageInfo;
    private boolean checked;

    public BlockedItem(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }
}
