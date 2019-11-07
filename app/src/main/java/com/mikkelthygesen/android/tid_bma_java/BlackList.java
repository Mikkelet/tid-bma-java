package com.mikkelthygesen.android.tid_bma_java;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlackList extends Fragment {



    public BlackList() {
        // Required empty public constructor
    }

    public static BlackList newInstance() {
        
        Bundle args = new Bundle();
        
        BlackList fragment = new BlackList();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_black_list, container, false);
    }

}
