package com.mikkelthygesen.android.tid_bma_java;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlackList extends Fragment {

    //Button to add apps to the Blacklist
    private Button addAppsButton;



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
        View v = inflater.inflate(R.layout.fragment_blacklist, container, false);

        addAppsButton = v.findViewById(R.id.blacklistButtonAdd);
        addAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment listOfAppsOnDevice = ListOfAppsOnDevice.newInstance();
                openFragment(listOfAppsOnDevice);
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
