package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.xw.repo.BubbleSeekBar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timer extends Fragment {


    private BubbleSeekBar bubbleSeekBarExercise;
    private BubbleSeekBar bubbleSeekBarBlockedApps;

    private ImageView arrowToStartSession;

    private int exerciseProgress;
    private int blockedAppsProgress;
    private Button mStartSessionButtonActivateBlock;
    private Button activate;
    private boolean isDefault = true;
    private int MAX_VALUE = 5;


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
     mStartSessionButtonActivateBlock = v.findViewById(R.id.StartSessionButtonActivateBlock);

        List<BlockedItem> tempList = BlockedAppDB.collectAllBlockedApplications(getActivity().getPackageManager(), getActivity() );
        mStartSessionButtonActivateBlock = v.findViewById(R.id.StartSessionButtonActivateBlock);

        if (tempList.size() > 0){

            mStartSessionButtonActivateBlock.setBackgroundResource(R.drawable.activate_block_circle_pressed);
        }


        mStartSessionButtonActivateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getContext().getPackageManager();
                Intent intent = new Intent(getActivity(), FakeHomeScreen.class);
                startActivity(intent);

                if(true)
                    return;
                Intent intent2 = packageManager.getLaunchIntentForPackage("com.duolingo");
                if(intent == null) {
                    intent = packageManager.getLaunchIntentForPackage("com.android.chrome");
                    Toast.makeText(getContext(), "Please install Duolingo", Toast.LENGTH_SHORT).show();
                }

                intent.setAction("com.android.chrome");
                startActivity(intent);
            }
        });


        arrowToStartSession = v.findViewById(R.id.ArrowToStartSession);
        arrowToStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSession session = StartSession.newInstance();
                openFragment(session);
            }
        });

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
                progressExerciseSP(progress);
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

    // Here we save any progress for the exercise seekbar to the Shared Preferences
    private void progressExerciseSP(int progress){
        // get the shared preferences file "sp"
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if it is null, just return
        if(sharedPref == null) return;

        // save the progress under the key "progress"
        sharedPref.edit().putInt("progress", progress).apply();
    }

    // Here we get the exercise seekbar progress from shared preferences
    private void getExerciseProgress(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if(sharedPref == null){
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        exerciseProgress = sharedPref.getInt("progress",1);
        if(exerciseProgress > MAX_VALUE) exerciseProgress = MAX_VALUE;
    }

    // Here we save any progress for the blocked App seekbar to the Shared Preferences
    private void progressBlockedAppsSP(int progress){
        // get the shared preferences file "sp"
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if it is null, just return
        if(sharedPref == null) return;

        // save the progress under the key "blocked Apps progress"
        sharedPref.edit().putInt("blockedAppsProgress", progress).apply();
    }


    // Here we get the blockedApps progress from shared preferences
    private void getBlockedAppsProgress(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        // if null, return
        if(sharedPref == null){
            return;
        }

        // set our progress field to whatever is gotten from the shared preferences
        blockedAppsProgress = sharedPref.getInt("blockedAppsProgress",1);
        if(blockedAppsProgress > MAX_VALUE) blockedAppsProgress = MAX_VALUE;
    }

    private void print(String mesg){
        Log.d("Timer", mesg);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
