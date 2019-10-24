package com.mikkelthygesen.android.tid_bma_java;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartSession extends Fragment {

    private Spinner spinner;
    private Button startBlock;

    public StartSession() {
        // Required empty public constructor
    }

    public static StartSession newInstance() {

        Bundle args = new Bundle();

        StartSession fragment = new StartSession();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_session, container, false);
        spinner = view.findViewById(R.id.StartSessionSpinnerExerciseProviders);

        List<String> myArraySpinner = new ArrayList<String>();

        myArraySpinner.add("Duolingo");
        myArraySpinner.add("Memrise");
        myArraySpinner.add("SoloLearn");
        ArrayAdapter<String> arrayAdapter;
        if(this.isAdded())
            arrayAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,myArraySpinner);
        else arrayAdapter = null;
        if(arrayAdapter != null)
            spinner.setAdapter(arrayAdapter);

        // button
        initStartBlock(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initStartBlock(View view){
        startBlock = view.findViewById(R.id.StartSessionButtonActivateBlock);
        startBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  getActivity().getPackageManager().getLaunchIntentForPackage("com.duolingo");
                if (intent != null) {
                    startActivity(intent);//null pointer check in case package name was not found
                }
            }
        });

    }

}
