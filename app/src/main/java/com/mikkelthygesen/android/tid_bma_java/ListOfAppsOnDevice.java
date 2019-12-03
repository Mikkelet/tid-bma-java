package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.mikkelthygesen.android.tid_bma_java.BlackList.BLOCKEDAPPS;
import static com.mikkelthygesen.android.tid_bma_java.BlockedAppDB.collectAllApplicationsOnPhone;

public class ListOfAppsOnDevice extends Fragment implements Observer {

    private RecyclerView listOfAppsView;
    private BlackListAdapter appsAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blacklist_recyclerview,container,false);

        listOfAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        listOfAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> items = collectAllApplicationsOnPhone(packageManager, getActivity());
        appsAdapter = new BlackListAdapter(packageManager, items);
        listOfAppsView.setAdapter(appsAdapter);

        return v;
    }

    @Override
    public void onDestroyView() {

        saveBlockedApps();
        super.onDestroyView();
    }


    private void saveBlockedApps() {
        BlockedAppDB.saveBlockedApps(getActivity(), appsAdapter.getCheckPackageNames());
    }

    @Override
    public void update(Observable o, Object arg) {
        appsAdapter.notifyDataSetChanged();

        PackageManager packageManager = getActivity().getPackageManager();
        List<BlockedItem> items = BlockedAppDB.collectAllApplicationsOnPhone(packageManager, getActivity());
        appsAdapter = new BlackListAdapter(packageManager, items);
        listOfAppsView.setAdapter(appsAdapter);
    }
}
