package com.mikkelthygesen.android.tid_bma_java;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.mikkelthygesen.android.tid_bma_java.BlockedAppDB.collectAllBlockedApplications;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartSession extends Fragment {

    private Spinner spinner;

    private ImageView arrowToTimer;

    private Button mBlacklist;
    private BlackListAdapter appsAdapter;
    private RecyclerView listOfAppsView;


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

        mBlacklist = view.findViewById(R.id.GoToActivationSite);
        mBlacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfAppsOnDevice lst = new ListOfAppsOnDevice();
                openFragment(lst);
            }
        });

        listOfAppsView = view.findViewById(R.id.listOfAppRecyclerView);
        listOfAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> items = collectAllBlockedApplications(packageManager, getActivity());
        if(!items.isEmpty()) {
            appsAdapter = new BlackListAdapter(packageManager, items, true);
            listOfAppsView.setAdapter(appsAdapter);
        }

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
    private void saveProvderAppsToSharedPrefs(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        if(sharedPref == null) return;
        Log.d("Start Session", "saveProvderAppsToSharedPrefs: ");
        sharedPref.edit().putString(Database.SharePrefs.EXERCISE_PROVIDER, Database.getinstance().getSelectedExerciseProviderName()).apply();
    }


}
