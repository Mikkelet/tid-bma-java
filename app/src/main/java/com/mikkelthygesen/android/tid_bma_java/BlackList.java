package com.mikkelthygesen.android.tid_bma_java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlackList extends Fragment implements Observer {

    //Button to add apps to the Blacklist
    private Button mBlockAppsButton;
    private Button mUnblockAppsButton;

    private RecyclerView mBlockedAppsView;
    private BlackListAdapter appsAdapter;

    private AppsDB mAppsDB;

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

        mBlockedAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        mBlockedAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAppsDB = AppsDB.get(getActivity());


        mAppsDB.updateBlockedApps();

        updateUI();

        mBlockAppsButton = v.findViewById(R.id.blacklistButtonBlock);
        mBlockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment listOfAppsOnDevice = ListOfAppsOnDevice.newInstance();
                openFragment(listOfAppsOnDevice);
            }
        });

        mUnblockAppsButton = v.findViewById(R.id.blacklistButtonUnblock);
        mUnblockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppsDB.removeBlockedApps();
            }
        });

        return v;
    }

    private void updateUI(){
        AppsDB appsDB = AppsDB.get(getActivity());
        appsDB.addObserver(this);

        appsAdapter = new BlackListAdapter(appsDB, true);
        mBlockedAppsView.setAdapter(appsAdapter);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void update(Observable o, Object arg) {
        appsAdapter.notifyDataSetChanged();
        updateUI();
    }
}
