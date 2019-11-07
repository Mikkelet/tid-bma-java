package com.mikkelthygesen.android.tid_bma_java.Storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.mikkelthygesen.android.tid_bma_java.R;

public class DataTransferableObject extends Fragment {

    DatabaseHelper myDB;
    EditText list_apps, timer_settings, chosen_learnings;
    Button buttonAdd;


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment3_layout, container, false);
        myDB = new DatabaseHelper(getActivity());

        list_apps = (EditText) rootView.findViewById(R.id.list_apps);
        timer_settings = (EditText) rootView.findViewById(R.id.timer_settings);
        chosen_learnings = (EditText) rootView.findViewById(R.id.chosen_learnings);
        buttonAdd = (Button) rootView.findViewById(R.id.button_add);


        addData();


        return rootView;
    }

    /**
     * if (container == null) {
     * return null;
     * }
     * return inflater.inflate(R.layout.fragment3_layout, container, false);
     * }
     **/



    public void addData() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.insertData(
                        list_apps.getText().toString(),
                        timer_settings.getText().toString(),
                        chosen_learnings.getText().toString()
                );
            }
        });
    }

}
