package com.mikkelthygesen.android.tid_bma_java;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartSession extends Fragment {

    private Spinner spinner;

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
        final List<String> providerAppNames = Database.getinstance().getProviderAppNames();
        int selectedItemPosition = providerAppNames.indexOf(Database.getinstance().getSelectedExerciseProviderName());

        ArrayAdapter<String> arrayAdapter;
        if(this.isAdded())
            arrayAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,providerAppNames);
        else arrayAdapter = null;
        if(arrayAdapter != null) {
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(selectedItemPosition);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = providerAppNames.get(position);
                Database.getinstance().setExerciseProviderBundleId(selected);
                String provider = Database.getinstance().getExerciseProviderBundleId();
                Log.d("Start Session", "onItemSelected: "+selected);
                saveProvderAppsToSharedPrefs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void saveProvderAppsToSharedPrefs(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        if(sharedPref == null) return;
        Log.d("Start Session", "saveProvderAppsToSharedPrefs: ");
        sharedPref.edit().putString(Database.SharePrefs.EXERCISE_PROVIDER, Database.getinstance().getExerciseProviderBundleId()).apply();
    }


}
