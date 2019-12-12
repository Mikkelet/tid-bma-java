package com.mikkelthygesen.android.tid_bma_java.controllers;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikkelthygesen.android.tid_bma_java.BlackListAdapter;
import com.mikkelthygesen.android.tid_bma_java.data.BlockedAppsManager;
import com.mikkelthygesen.android.tid_bma_java.R;
import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;

import static com.mikkelthygesen.android.tid_bma_java.data.BlockedAppsManager.collectAllApplicationsOnPhone;

public class ListOfAppsOnDevice extends Fragment implements Observer {

    private RecyclerView listOfAppsView;
    private BlackListAdapter appsAdapter;
    private Button mBlockAppsButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blacklist_recyclerview, container, false);

        listOfAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        listOfAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> items = collectAllApplicationsOnPhone(getActivity());
        appsAdapter = new BlackListAdapter(packageManager, items);
        listOfAppsView.setAdapter(appsAdapter);

        mBlockAppsButton = v.findViewById(R.id.blacklistButtonBlock);
        mBlockAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBlockedApps();
                StartSession startSession = new StartSession();
                openFragment(startSession);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {

        saveBlockedApps();
        super.onDestroyView();
    }


    private void saveBlockedApps() {
        BlockedAppsManager.saveBlockedApps(getActivity(), appsAdapter.getCheckPackageNames());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void update(Observable o, Object arg) {
        appsAdapter.notifyDataSetChanged();

        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> items = BlockedAppsManager.collectAllApplicationsOnPhone(getActivity());
        appsAdapter = new BlackListAdapter(packageManager, items);
        listOfAppsView.setAdapter(appsAdapter);
    }
}
