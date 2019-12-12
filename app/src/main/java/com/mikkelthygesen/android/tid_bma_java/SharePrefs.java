package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class SharePrefs {
    Context mContext;

    public SharePrefs(Context context) {
        mContext = context;
    }

    public void setBlockTime(int value){

    }
    public int getBlockTime(){
        return 0;
    }
    public void setFunTime(int value){

    }
    public int getFunTime(){
        return 0;
    }
    public void setBlockedApps(){

    }
    public List getBlockedApps(){
        return new ArrayList();
    }
}
