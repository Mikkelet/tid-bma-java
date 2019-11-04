package com.mikkelthygesen.android.tid_bma_java;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlackList extends Fragment {

    private Spinner dropdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_black_list, container, false);

        dropdown = rootView.findViewById(R.id.spinnerItemTextView);
        fillTheSpinner();

        return rootView;
    }

    private void fillTheSpinner() {
        List<String> list = new ArrayList<>();
        PackageManager pm = ((this.getActivity())).getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            list.add(packageInfo.packageName);

        }
        System.out.println("test123" + list);



        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, list);
       /* dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        dropdown.setAdapter(dataAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }   }
