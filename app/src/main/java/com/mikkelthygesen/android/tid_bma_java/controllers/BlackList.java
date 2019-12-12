package com.mikkelthygesen.android.tid_bma_java.controllers;


import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikkelthygesen.android.tid_bma_java.BlackListAdapter;
import com.mikkelthygesen.android.tid_bma_java.data.BlockedAppDB;
import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;
import com.mikkelthygesen.android.tid_bma_java.R;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlackList extends Fragment{
    private String blockedapps = "blockedapps";
    public static final String BLOCKEDAPPS = "blockedapps";

    //Button to add apps to the Blacklist
    private Button mBlockAppsButton;
    private Button mUnblockAppsButton;
    private RecyclerView mBlockedAppsView;
    private BlackListAdapter appsAdapter;

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
        View v = inflater.inflate(R.layout.fragment_blacklist, container, false);
        mBlockedAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        mBlockedAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateUI();
        mBlockAppsButton = v.findViewById(R.id.blacklistButtonBlock);
        mBlockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfAppsOnDevice listOfAppsOnDevice = new ListOfAppsOnDevice();
                openFragment(listOfAppsOnDevice);
            }
        });
        mUnblockAppsButton = v.findViewById(R.id.blacklistButtonUnblock);
        mUnblockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> packageNames = appsAdapter.getCheckPackageNames();
                BlockedAppDB.saveBlockedApps(getActivity(), packageNames);
            }
        });
        return v;
    }


    private void updateUI() {
        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> blockedItems = BlockedAppDB.collectAllApplicationsOnPhone(packageManager, getActivity());
        ListIterator<BlockedItem> iter = blockedItems.listIterator();
        while (iter.hasNext()) {
            BlockedItem next = iter.next();
            if (!next.isChecked()){
                iter.remove();
            }
        }

        this.appsAdapter = new BlackListAdapter(packageManager, blockedItems);
        this.mBlockedAppsView.setAdapter(appsAdapter);
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
