package com.mikkelthygesen.android.tid_bma_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private StartSession mStartSessionFragment = StartSession.newInstance();

    private BottomNavigationView mBottomNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFragment(mStartSessionFragment);
        getSelectedExerciseProvider();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
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
