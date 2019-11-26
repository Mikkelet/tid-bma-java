package com.mikkelthygesen.android.tid_bma_java;

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

        AppsDB.get(getActivity());

        updateUI();

        return v;
    }

    private void updateUI(){
        AppsDB appsDB = AppsDB.get(getActivity());
        appsAdapter = new BlackListAdapter(appsDB, false);
        listOfAppsView.setAdapter(appsAdapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        appsAdapter.notifyDataSetChanged();
        updateUI();
    }
}
