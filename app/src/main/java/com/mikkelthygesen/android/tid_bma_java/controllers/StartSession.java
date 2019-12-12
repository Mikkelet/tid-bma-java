package com.mikkelthygesen.android.tid_bma_java.controllers;


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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikkelthygesen.android.tid_bma_java.BlackListAdapter;
import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;
import com.mikkelthygesen.android.tid_bma_java.data.Database;
import com.mikkelthygesen.android.tid_bma_java.R;
import com.mikkelthygesen.android.tid_bma_java.data.SharedPrefs;

import java.util.List;

import static com.mikkelthygesen.android.tid_bma_java.data.BlockedAppsManager.collectAllBlockedApplications;

public class StartSession extends Fragment {

    private Spinner spinner;
    private ImageView arrowToTimer;
    private Button mButtonToApps;
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
        setupSpinner(view);

        setupButtonToApps(view);

        setupRecyclerView(view);

        setupArrowToTimer(view);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Initialize the arrow so when clicked it will take the user to
     * the Timer class.
     * @param view the given view where the arrow is.
     */
    private void setupArrowToTimer(View view) {
        arrowToTimer = view.findViewById(R.id.ArrowToTimer);
        arrowToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = Timer.newInstance();
                openFragment(timer);
            }
        });
    }

    /**
     * Initialize RecyclerView so it contains all blocked apps,
     * if there are any.
     * @param view the given view where the RecyclerView is.
     */
    private void setupRecyclerView(View view) {
        listOfAppsView = view.findViewById(R.id.listOfAppRecyclerView);
        listOfAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> items = collectAllBlockedApplications(getActivity());

        if(!items.isEmpty()) {
            listOfAppsView.setVisibility(View.VISIBLE);
             appsAdapter = new BlackListAdapter(packageManager, items, true);
             listOfAppsView.setAdapter(appsAdapter);
         }
    }

    /**
     * Initialize the Button to all the apps on the device.
     * @param view the given view where the Button is.
     */
    private void setupButtonToApps(View view) {
        mButtonToApps = view.findViewById(R.id.GoToActivationSite);
        mButtonToApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfAppsOnDevice lst = new ListOfAppsOnDevice();
                openFragment(lst);
            }
        });
    }

    /**
     * Initialize the Spinner with all the available
     * exercise providers.
     * @param view the given view where the Spinner is.
     */
    private void setupSpinner(View view) {
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
                SharedPrefs sharedPrefs = new SharedPrefs(getContext());
                sharedPrefs.setProviderApp();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Replaces the main fragment layout on the activity with a new one.
     * @param fragment The one replacing the main fragment.
     */
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
