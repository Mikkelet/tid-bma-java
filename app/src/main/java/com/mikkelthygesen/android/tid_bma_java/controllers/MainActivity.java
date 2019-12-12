package com.mikkelthygesen.android.tid_bma_java.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.mikkelthygesen.android.tid_bma_java.data.Database;
import com.mikkelthygesen.android.tid_bma_java.R;
import com.mikkelthygesen.android.tid_bma_java.data.SharedPrefs;


public class MainActivity extends AppCompatActivity {

    private StartSession mStartSessionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mStartSessionFragment == null){
            mStartSessionFragment = StartSession.newInstance();
            openFragment(mStartSessionFragment);
        }
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        sharedPrefs.getProviderBundleId();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.commit();
    }
}
