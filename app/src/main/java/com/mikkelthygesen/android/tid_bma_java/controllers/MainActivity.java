package com.mikkelthygesen.android.tid_bma_java.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.mikkelthygesen.android.tid_bma_java.data.Database;
import com.mikkelthygesen.android.tid_bma_java.R;


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
        getSelectedExerciseProvider();

    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.commit();
    }

    private void getSelectedExerciseProvider(){
        SharedPreferences sharedPref = getSharedPreferences("sp", Context.MODE_PRIVATE);
        if(sharedPref == null){
            return;
        }
        String epFromSp = sharedPref.getString(Database.SharePrefs.EXERCISE_PROVIDER,"");
        Database.getinstance().setExerciseProviderBundleId(epFromSp);


    }
}
