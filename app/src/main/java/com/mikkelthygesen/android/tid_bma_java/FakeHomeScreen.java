package com.mikkelthygesen.android.tid_bma_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class FakeHomeScreen extends AppCompatActivity {

    private ImageButton mImageButtonFacebook;
    private ImageButton mImageButtonInstagram;
    private ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_home_screen);

        mImageButtonFacebook = findViewById(R.id.imageButton_facebook);
        mImageButtonInstagram = findViewById(R.id.imageButton_instagram);
        mConstraintLayout = findViewById(R.id.fake_homescreen_constraint_layout);
        configureUI();
    }

    private void configureUI(){



        mImageButtonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchApp("com.instagram.android");
            }
        });

        mImageButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchApp("com.facebook.katana");
            }
        });
    }

    private void launchApp(String bundleId){
        Database db = Database.getinstance();

        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(bundleId);
        if(intent == null) {
            Toast.makeText(this, "could not find that app \n"+bundleId, Toast.LENGTH_SHORT).show();
        }else if(db.getIsBlocking()){
            intent = packageManager.getLaunchIntentForPackage(db.getExerciseProviderBundleId());
            //Toast.makeText(this, "App blocked!", Toast.LENGTH_SHORT).show();
            Intent service = new Intent(FakeHomeScreen.this, BlockService.class);
            startService(service);
        }
        db.deactivateBlocking();
        if(intent != null)
        startActivity(intent);
    }
}
