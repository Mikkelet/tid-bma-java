package com.mikkelthygesen.android.tid_bma_java;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


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
        View v = inflater.inflate(R.layout.fragment_blacklist, container, false);
        mBlockedAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        mBlockedAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAppsDB = AppsDB.get(getActivity());
        filterDuplicateApplications();
        mAppsDB.updateBlockedApps();

        updateUI();
        mBlockAppsButton = v.findViewById(R.id.blacklistButtonBlock);
        mBlockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment listOfAppsOnDevice = ListOfAppsOnDevice.newInstance();
                openFragment(listOfAppsOnDevice);
                mAppsDB.updateBlockedApps();
            }
        });
        mUnblockAppsButton = v.findViewById(R.id.blacklistButtonUnblock);
        mUnblockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppsDB.removeBlockedApps();
                mAppsDB.updateBlockedApps();
            }
        });
        saveAppsToSharedPreferences();

        return v;
    }


    private void updateUI() {
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
    void checkIfSharedPreferencesIsEmpty() {
        List<BlockedAppItem> listOfItems = new ArrayList<>();
        if (listOfItems.size() <= 0)
        {
            return;
        }else {
            filterDuplicateApplications();
        }
    }

    private void filterDuplicateApplications() {
        List<BlockedAppItem> listOfItems;
        BlockedAppsSharedPreferences blockedAppsSharedPreferences = new BlockedAppsSharedPreferences();
        listOfItems = blockedAppsSharedPreferences.getBlockedAppItems();
        Set<BlockedAppItem> onCreateViewListOfBlockedApps = new HashSet<>(listOfItems);
        getSavedApps(onCreateViewListOfBlockedApps);
    }

    private void getSavedApps(Set<BlockedAppItem> onCreateViewListOfBlockedApps) {
        List<PackageInfo> tempId = new ArrayList<>(AppsDB.getmAllAppsOnPhone());
        List<PackageInfo> savedBlocksFromSharedPreferences = new ArrayList<>();
        for (BlockedAppItem blockedAppItem : onCreateViewListOfBlockedApps
        ) {
            if (AppsDB.getmAllAppsOnPhone().contains(blockedAppItem.getName())) {
                int i = tempId.indexOf(blockedAppItem.getName());
                PackageInfo packageInfo = tempId.get(i);
                savedBlocksFromSharedPreferences.add(packageInfo);

                mAppsDB.updateBlockedApps();
            }

        }

    }


    public void saveAppsToSharedPreferences() {
        BlockedAppsSharedPreferences blockedAppsSharedPreferences = new BlockedAppsSharedPreferences();
        blockedAppsSharedPreferences.saveToSharedPreferences();
        mAppsDB.updateBlockedApps();

    }

     private class BlockedAppsSharedPreferences {
        private String blockedapps = "blockedapps";
        public static final String BLOCKEDAPPS = "blockedapps";
        private List<String> blockedAppSP = new ArrayList<>();

        public List<String> getBlockedAppSP() {
            return blockedAppSP;
        }

        public void setBlockedAppSP(List<String> blockedAppSP) {
            blockedAppSP = blockedAppSP;
        }

        public List<BlockedAppItem> getBlockedAppItems() {
            getBlockedAppOnSharedPreferences();
            List<BlockedAppItem> tempList = new ArrayList<>();

            for (String string : getBlockedAppSP()) {
                BlockedAppItem blockedAppItem = new BlockedAppItem("", "", "");

                tempList.add(blockedAppItem);
            }

            return tempList;
        }

        public void addApp(String string) {
            getBlockedAppSP().add(string);

        }

        public void removeApp(String string) {
            getBlockedAppSP().remove(string);
        }

        public List saveToSharedPreferences() {
            List<PackageInfo> tempList = new ArrayList<>(AppsDB.getmBlockedApps());
            Set<String> tempSet = new HashSet<>();

            for (PackageInfo packageInfo: tempList) {
                String tempString = packageInfo.toString();
                tempSet.add(tempString);


            }
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(blockedapps, Context.MODE_PRIVATE);
            if (sharedPreferences == null) { return tempList;}
            else {

                sharedPreferences.edit().putStringSet(blockedapps, tempSet).commit();

            }

        return tempList;}

        public void getBlockedAppOnSharedPreferences() {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(blockedapps, Context.MODE_PRIVATE);
            if (sharedPreferences == null) {
                return;
            }

            Set<String> tempSet = new HashSet<>();
            tempSet.addAll(sharedPreferences.getStringSet(blockedapps, null));
            getBlockedAppSP().clear();
            getBlockedAppSP().addAll(tempSet);

        }

    }

}
