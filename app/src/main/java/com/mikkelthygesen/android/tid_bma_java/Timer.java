package com.mikkelthygesen.android.tid_bma_java;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timer extends Fragment {


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
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

}
