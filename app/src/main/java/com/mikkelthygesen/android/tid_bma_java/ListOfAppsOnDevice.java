package com.mikkelthygesen.android.tid_bma_java;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfAppsOnDevice extends Fragment implements Observer {

    private RecyclerView listOfAppsView;

    private AppsAdapter appsAdapter;

    private PackageManager packageManager;

    private AppsDB appsDB;

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

        //Skabelsen af det interactive recyclerview.
        listOfAppsView = v.findViewById(R.id.listOfAppRecyclerView);
        listOfAppsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        packageManager = getActivity().getPackageManager();

        List<PackageInfo> packageInfoList = packageManager.
                getInstalledPackages(PackageManager.GET_PERMISSIONS);

        appsDB = AppsDB.get(getActivity());
        for(PackageInfo pi : packageInfoList){
            boolean systemPackage = isSystemPackage(pi);
            if(!systemPackage){
                appsDB.addApp(pi);
            }
        }
        appsDB.sortDB();

        updateUI();

        return v;
    }

    private void updateUI(){
        AppsDB appsDB = AppsDB.get(getActivity());
        appsDB.addObserver(this);

        appsAdapter = new AppsAdapter(appsDB.getAppsDB());
        listOfAppsView.setAdapter(appsAdapter);
    }

    private class AppsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView appNameTextView;
        private ImageView iconImage;

        public AppsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_blacklist_one_app,parent,false));
            itemView.setOnClickListener(this);

            //Creating the views.
            appNameTextView = itemView.findViewById(R.id.appName);
            iconImage = itemView.findViewById(R.id.app_icon);
        }

        public void bind(PackageInfo packageInfo){
            //Bind the icon to the imageView.
            iconImage.setImageDrawable(packageManager
                    .getApplicationIcon(packageInfo.applicationInfo));
            //Bind the app's name to the textView.
            appNameTextView.setText(packageManager.getApplicationLabel(
                    packageInfo.applicationInfo).toString());
        }

        @Override
        public void onClick(View v) {
            Fragment blacklist = BlackList.newInstance();
            openFragment(blacklist);
        }

    }

    private class AppsAdapter extends RecyclerView.Adapter<AppsHolder>{

        private List<PackageInfo> appNames;

        public AppsAdapter(List<PackageInfo> appNames){
            this.appNames = appNames;
        }

        @NonNull
        @Override
        public AppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new AppsHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AppsHolder holder, int position) {
            PackageInfo appName = appNames.get(position);
            holder.bind(appName);
        }

        @Override
        public int getItemCount() {
            return appNames.size();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        appsAdapter.notifyDataSetChanged();
        updateUI();
    }
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
