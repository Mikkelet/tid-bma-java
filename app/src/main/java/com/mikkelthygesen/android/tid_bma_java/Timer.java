package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xw.repo.BubbleSeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timer extends Fragment {


    private BubbleSeekBar bubbleSeekBarExercise;
    private BubbleSeekBar bubbleSeekBarBlockedApps;
    private int exerciseProgress;
    private int blockedAppsProgress;

    public Timer() {
        // Required empty public constructor
    }

    public static Timer newInstance() {
        
        Bundle args = new Bundle();
        
        Timer fragment = new Timer();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timer, container, false);


        bubbleSeekBarExercise = v.findViewById(R.id.bubbleSeekBarExercise);

        // get progress value from Share Preferences
        getExerciseProgress();

        // set the progress
        bubbleSeekBarExercise.setProgress(exerciseProgress);
        bubbleSeekBarExercise.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                // Save the progress whenever it is changed
                progressExercisesSP(progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
            }
        });

        bubbleSeekBarBlockedApps = v.findViewById(R.id.bubbleSeekBarBlockedApps);


        // get progress value from Share Preferences
        getBlockedAppsProgress();

        // set the progress
        bubbleSeekBarBlockedApps.setProgress(blockedAppsProgress);
        bubbleSeekBarBlockedApps.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                progressBlockedAppsSP(progress);

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        return v;
    }

    // Here we can save any progress to the Shared Preferences
    private void progressExercisesSP(int progress){
        // get the shared preferences file "sp"
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if it is null, just return
        if(sharedPref == null) return;

        // save the progress under the key "progress"
        sharedPref.edit().putInt("progress", progress).apply();

    }

    // Here we get the progress from shared preferences
    private void getExerciseProgress(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if(sharedPref == null){
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        exerciseProgress = sharedPref.getInt("progress",1);
    }

    private void progressBlockedAppsSP(int progress){
        // get the shared preferences file "sp"
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if it is null, just return
        if(sharedPref == null) return;

        // save the progress under the key "progress"
        sharedPref.edit().putInt("blockedAppsProgress", progress).apply();

    }


    // Here we get the progress from shared preferences
    private void getBlockedAppsProgress(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if(sharedPref == null){
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        blockedAppsProgress = sharedPref.getInt("blockedAppsProgress",1);
    }

    private void print(String mesg){
        Log.d("Timer", mesg);
    }

}
