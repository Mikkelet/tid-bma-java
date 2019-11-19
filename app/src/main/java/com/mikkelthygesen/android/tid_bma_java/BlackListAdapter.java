package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.BlackListHolder> {

    private PackageManager packageManager;
    private AppsDB appsDB;
    private boolean mBlocked;


    public BlackListAdapter(AppsDB appsDB, boolean blocked) {
        this.appsDB = appsDB;
        this.packageManager = appsDB.getPackageManager();
        this.mBlocked = blocked;
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
        holder.bind(appsDB.getOneApp(position,mBlocked));
    }

    @Override
    public int getItemCount() {
        return appsDB.getSize(mBlocked);
    }


    public class BlackListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mAppNameTextView;
        private ImageView mIconImage;
        private CheckBox mSelectionState;

        public BlackListHolder(ViewGroup parent) {
            super(parent);

            //Creating the views.
            mAppNameTextView = parent.findViewById(R.id.appName);
            mIconImage = parent.findViewById(R.id.app_icon);
            mSelectionState = parent.findViewById(R.id.checkApp);

            parent.setOnClickListener(this);

            mSelectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        appsDB.isItBlocked(getAdapterPosition(), mBlocked);
                    }
                }
            });
        }

        public void bind(PackageInfo packageInfo) {
            //Bind the icon to the imageView.
            mIconImage.setImageDrawable(packageManager
                    .getApplicationIcon(packageInfo.applicationInfo));
            //Bind the app's name to the textView.
            mAppNameTextView.setText(packageManager.getApplicationLabel(
                    packageInfo.applicationInfo).toString());
            if(!mBlocked) {
                mSelectionState.setChecked(appsDB.blockedApp(packageInfo));
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if(mSelectionState.isChecked()){
                mSelectionState.setChecked(false);
                appsDB.isItBlocked(adapterPosition, mBlocked);
            } else{
                mSelectionState.setChecked(true);
            }
        }
    }
}
