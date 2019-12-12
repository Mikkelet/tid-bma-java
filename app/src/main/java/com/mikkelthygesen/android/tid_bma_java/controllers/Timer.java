package com.mikkelthygesen.android.tid_bma_java.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mikkelthygesen.android.tid_bma_java.R;
import com.mikkelthygesen.android.tid_bma_java.data.BlockedAppsManager;
import com.mikkelthygesen.android.tid_bma_java.data.Database;
import com.mikkelthygesen.android.tid_bma_java.data.SharedPrefs;
import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;
import com.xw.repo.BubbleSeekBar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timer extends Fragment {

    private BubbleSeekBar bubbleSeekBarBlockTime;
    private BubbleSeekBar bubbleSeekBarFunTime;

    private ImageView arrowToStartSession;

    private int blockTimeProgress;
    private int funTimeProgress;
    private Button mStartSessionButtonActivateBlock;
    private SharedPrefs mSharedPrefs;


    public Timer() {
        // Required empty public constructor
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

        activateButton(v);
        arrowBack(v);
        blockedTime(v);
        funTime(v);

        return v;
    }

    /**
     * Progress of time set in fun time and put into shared preferences
     * @param v
     */
    private void funTime(View v) {
        bubbleSeekBarFunTime = v.findViewById(R.id.bubbleSeekBarFunTime);
        mSharedPrefs.getFunTime();
        funTimeProgress = Database.getinstance().getFunTime();

        // set the progress fun time
        bubbleSeekBarFunTime.setProgress(funTimeProgress);
        bubbleSeekBarFunTime.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                // Save the progress of fun time whenever it is changed
                mSharedPrefs.setFunTime(progress);
                Database.getinstance().setFunTime(progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    /**
     * Progress of time set in blocked time and put into shared preferences
     * @param v
     */
    private void blockedTime(View v) {
        bubbleSeekBarBlockTime = v.findViewById(R.id.bubbleSeekBarBlockTime);
        mSharedPrefs.getBlockTime();
        blockTimeProgress = Database.getinstance().getBlockTime();

        // set the progress of blocked time
        bubbleSeekBarBlockTime.setProgress(blockTimeProgress);
        bubbleSeekBarBlockTime.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                // Save the progress of blocked time whenever it is changed
                mSharedPrefs.setBlockTime(progress);
                Database.getinstance().setBlockTime(progress);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
            }
        });
    }

    /**
     * An arrow button which brings the user back to the previous page
     * @param v
     */
    private void arrowBack(View v) {
        // arrow back to previous screen
        arrowToStartSession = v.findViewById(R.id.ArrowToStartSession);
        arrowToStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSession session = StartSession.newInstance();
                openFragment(session);
            }
        });
    }

    /**
     * The activation button turns green when an app are chosen to be blocked.
     * When the button clicked the user gets to the fake home screen.
     * @param v
     */
    private void activateButton(View v) {
        final List<BlockedItem> tempList = BlockedAppsManager.collectAllBlockedApplications(getActivity());
        mStartSessionButtonActivateBlock = v.findViewById(R.id.StartSessionButtonActivateBlock);

        // if shared preferences are set turn button green
        if (tempList.size() > 0){
            mStartSessionButtonActivateBlock.setBackgroundResource(R.drawable.activate_block_circle_pressed);
        }

        // get progress value from Shared Preferences
        mSharedPrefs = new SharedPrefs(getContext());

        // when 'activate and close' button pressed redirect to FakeHomeScreen.
        mStartSessionButtonActivateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getContext().getPackageManager();
                Intent intent = new Intent(getActivity(), FakeHomeScreen.class);
                if(tempList.size() > 0)
                startActivity(intent);
            }
        });
    }

    /**
    *  Replaces the main fragment layout on the activity with a new one.
    * @param fragment the one replacing the other.
     */
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
