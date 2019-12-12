package com.mikkelthygesen.android.tid_bma_java.controllers;

import android.annotation.SuppressLint;
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

import com.mikkelthygesen.android.tid_bma_java.data.BlockedAppDB;
import com.mikkelthygesen.android.tid_bma_java.data.SharedPrefs;
import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;
import com.mikkelthygesen.android.tid_bma_java.data.Database;
import com.mikkelthygesen.android.tid_bma_java.R;
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
    private int MAX_VALUE = 5;
    private SharedPrefs mSharedPrefs;


    public Timer() {
        // Required empty public constructor
        mSharedPrefs = new SharedPrefs(getContext());
    }

    public static Timer newInstance() {

        Bundle args = new Bundle();
        Timer fragment = new Timer();
        fragment.setArguments(args);

        return fragment;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                if (intent == null) {
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
        mSharedPrefs.getBlockTime();
        mSharedPrefs.getFunTime();
        exerciseProgress = Database.getinstance().getExerciseTime();
        blockedAppsProgress = Database.getinstance().getBrowseTime();

        // set the progress
        bubbleSeekBarExercise.setProgress(exerciseProgress);
        bubbleSeekBarExercise.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                // Save the progress whenever it is changed
                mSharedPrefs.setBlockTime(progress);
                Database.getinstance().setExerciseTime(progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
            }
        });

        bubbleSeekBarBlockedApps = v.findViewById(R.id.bubbleSeekBarBlockedApps);

        // set the progress
        bubbleSeekBarBlockedApps.setProgress(blockedAppsProgress);
        bubbleSeekBarBlockedApps.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                mSharedPrefs.setFunTime(progress);
                Database.getinstance().setBrowseTime(progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        return v;
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
