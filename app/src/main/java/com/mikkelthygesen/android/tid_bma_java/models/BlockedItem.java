package com.mikkelthygesen.android.tid_bma_java.models;

import android.content.pm.PackageInfo;

/**
 * Model class for a blocked app
 */
public class BlockedItem {

    private final PackageInfo packageInfo;
    private boolean checked;

    /**
     * initiates a new blocked item from a packageinfo class
     * @param packageInfo
     */
    public BlockedItem(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    /**
     * returns if the app is checked off for blocking
     * @return
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Set chcked off
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Get the associated pacakageinfo
     * @return
     */
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }
}
