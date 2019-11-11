package com.mikkelthygesen.android.tid_bma_java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xw.repo.BubbleSeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timer extends Fragment {


    private BubbleSeekBar bubbleSeekBar;

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
        
        Database.getinstance().bubbleSeekBarExercise = bubbleSeekBar.getProgress();
        return v;
    }

}
