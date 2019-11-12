package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.BlackListHolder> {

    private List<PackageInfo> appNames;
    private PackageManager packageManager;
    private AppsDB appsDB;


    public BlackListAdapter(List<PackageInfo> appNames, PackageManager packageManager) {
        this.appNames = appNames;
        this.packageManager = packageManager;
    }

    @NonNull
    @Override
    public BlackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int app = R.layout.fragment_blacklist_one_app;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(app, parent, false);
        return new BlackListHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlackListHolder holder, int position) {
        PackageInfo appName = appNames.get(position);
        holder.bind(appName, position);
    }

    @Override
    public int getItemCount() {
        return appNames.size();
    }


    public class BlackListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView appNameTextView;
        private ImageView iconImage;

        public BlackListHolder(ViewGroup parent) {
            super(parent);
            itemView.setOnClickListener(this);

            //Creating the views.
            appNameTextView = itemView.findViewById(R.id.appName);
            iconImage = itemView.findViewById(R.id.app_icon);
        }

        public void bind(PackageInfo packageInfo, int position) {
            //Bind the icon to the imageView.
            iconImage.setImageDrawable(packageManager
                    .getApplicationIcon(packageInfo.applicationInfo));
            //Bind the app's name to the textView.
            appNameTextView.setText(packageManager.getApplicationLabel(
                    packageInfo.applicationInfo).toString());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            AppsDB.
        }
    }
}
