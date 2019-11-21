package com.mikkelthygesen.android.tid_bma_java;


import java.util.UUID;

public class BlockedAppItem {
    private String name = null;
    private String isitblocked;
    private String mId;


    public BlockedAppItem(String name, String isitblocked) {
        this.name = name;
        this.isitblocked = isitblocked;
    }

    public BlockedAppItem(String name, String isitblocked, String mId) {
        this.name = name;
        this.isitblocked = isitblocked;
        this.mId = mId;
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

    public void toggleApp(String isitblocked) {
        if (isitblocked== "u"){
            isitblocked.replace("u","b");
        }else {
            isitblocked.replace("b","u");
        }
        this.isitblocked = isitblocked;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getId() {
        return mId;
    }

}