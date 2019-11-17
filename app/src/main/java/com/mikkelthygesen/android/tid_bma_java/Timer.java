package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xw.repo.BubbleSeekBar;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timer extends Fragment {


    private BubbleSeekBar bubbleSeekBar;
    private int progress;

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

        bubbleSeekBar = v.findViewById(R.id.bubbleSeekBarExercise);

        // get progress value from Share Preferences
        getProgressFromSharedPreferences();

        // set the progress
        bubbleSeekBar.setProgress(progress);
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                // Save the progress whenever it is changed
                saveProgressToSharedPreferences(progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
            }
        });
        
        Database.getinstance().bubbleSeekBarExercise = bubbleSeekBar.getProgress();
        return v;
    }

    // Here we can save any progress to the Shared Preferences
    private void saveProgressToSharedPreferences(int progress){
        // get the shared preferenes file "sp"
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if it is null, just return
        if(sharedPref == null) return;

        // save the progress under the key "progress"
        sharedPref.edit().putInt("progress",progress).apply();
    }

    // Here we get the progress from shared preferences
    private void getProgressFromSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if(sharedPref == null){
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        progress = sharedPref.getInt("progress",0);
    }

    private void print(String mesg){
        Log.d("Timer", mesg);
    }

}
