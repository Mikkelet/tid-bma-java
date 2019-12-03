package com.mikkelthygesen.android.tid_bma_java;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartSession extends Fragment {

    private Spinner spinner;

    private ImageView arrowToTimer;

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
            arrayAdapter = new ArrayAdapter<String>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,myArraySpinner);
        else arrayAdapter = null;
        if(arrayAdapter != null)
            spinner.setAdapter(arrayAdapter);

        arrowToTimer = view.findViewById(R.id.ArrowToTimer);
        arrowToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = Timer.newInstance();
                openFragment(timer);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
