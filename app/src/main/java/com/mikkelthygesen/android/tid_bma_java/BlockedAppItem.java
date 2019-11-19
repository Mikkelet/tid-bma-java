package com.mikkelthygesen.android.tid_bma_java;


import android.content.pm.PackageInfo;

import java.util.UUID;

public class BlockedAppItem {
    private String name = null;
    private String isitblocked = "U";
    private UUID mId;


    public BlockedAppItem(String name, String isitblocked) {
        this.name = name;
        this.isitblocked = isitblocked;
        mId = UUID.randomUUID();
    }

    public BlockedAppItem(String name, String isitblocked, UUID mId) {
        this.name = name;
        this.isitblocked = isitblocked;
        this.mId = mId;
    }

    public BlockedAppItem(UUID mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return oneLine("", "is here: ");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsitblocked() {
        return isitblocked;
    }

    public void setIsitblocked(String isitblocked) {
        this.isitblocked = isitblocked;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String oneLine(String pre, String post) {
        return pre + name + " " + post + isitblocked;
    }

    public UUID getId() {
        return mId;
    }

}