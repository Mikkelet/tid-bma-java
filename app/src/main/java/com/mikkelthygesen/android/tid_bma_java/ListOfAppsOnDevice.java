package com.mikkelthygesen.android.tid_bma_java;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfAppsOnDevice extends Fragment implements Observer {

    private RecyclerView listOfAppsView;

    private BlackListAdapter appsAdapter;

    private PackageManager packageManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ListOfAppsOnDevice newInstance() {
        
        Bundle args = new Bundle();
        
        ListOfAppsOnDevice fragment = new ListOfAppsOnDevice();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blacklist_recyclerview,container,false);

        listOfAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        listOfAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        packageManager = getActivity().getPackageManager();

        AppsDB.get(getActivity());

        updateUI();

        return v;
    }

    private void updateUI(){
        AppsDB appsDB = AppsDB.get(getActivity());
        appsDB.addObserver(this);

        appsAdapter = new BlackListAdapter(appsDB.getAppsDB(), packageManager);
        listOfAppsView.setAdapter(appsAdapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        appsAdapter.notifyDataSetChanged();
        updateUI();
    }
}
