package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import static com.mikkelthygesen.android.tid_bma_java.BlackList.BLOCKEDAPPS;

public class PageOne extends Fragment {

    private BlackListAdapter appsAdapter;
    private Button mAllAppsOnDevice;
    private Button mUnblockAppsButton;
    private RecyclerView mBlockedAppsView;
    private AppsDB mAppsDB;
    Fragment listOfAppsOnDevice = ListOfAppsOnDevice.newInstance();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blacklist, container, false);

        mBlockedAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        mBlockedAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAppsDB = AppsDB.get(getActivity());
        mAppsDB.updateBlockedApps();
        mUnblockAppsButton.setVisibility(View.INVISIBLE);

        updateUI();
        mAllAppsOnDevice = v.findViewById(R.id.blacklistButtonBlock);
        mAllAppsOnDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listOfAppsOnDevice.isResumed()) {
                    openFragment(listOfAppsOnDevice);
                }
                else {
                    return;
                }
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
        loadBlockedApps();
        return v;
    }


    private void updateUI() {
        AppsDB appsDB = AppsDB.get(getActivity());
        appsAdapter = new BlackListAdapter(appsDB, true);
        mBlockedAppsView.setAdapter(appsAdapter);
    }

    @Override
    public void onDestroyView() {

        saveBlockedApps();
        super.onDestroyView();
    }


    private void loadBlockedApps() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(BLOCKEDAPPS, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Set<String> stringSet = sharedPreferences.getStringSet(BLOCKEDAPPS, new HashSet<String>());
            Set<String> tempSet = new HashSet<>(stringSet);
            mAppsDB.load(tempSet);
        }
    }

    private void saveBlockedApps() {
        Set<String> blockedAppPackageName = mAppsDB.getBlockedAppNames();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(BLOCKEDAPPS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(BLOCKEDAPPS, blockedAppPackageName);
        edit.apply();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
