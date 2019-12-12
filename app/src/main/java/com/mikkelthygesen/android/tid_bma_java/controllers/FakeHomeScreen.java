package com.mikkelthygesen.android.tid_bma_java.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mikkelthygesen.android.tid_bma_java.data.Database;
import com.mikkelthygesen.android.tid_bma_java.R;
import com.mikkelthygesen.android.tid_bma_java.services.BlockService;
import com.mikkelthygesen.android.tid_bma_java.services.FunService;

public class FakeHomeScreen extends AppCompatActivity {

    private static final String TAG = "FakeHomeScreen";
    private ImageButton mImageButtonFacebook;
    private ImageButton mImageButtonInstagram;
    private String channelId = "channelId";
    private Integer notificationId = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_home_screen);

        configureUI();
    }

    /**
     * Initialize the UI for the fake home screen.
     */
    private void configureUI() {
        mImageButtonFacebook = findViewById(R.id.imageButton_facebook);
        mImageButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchApp("com.facebook.katana");
            }
        });

        mImageButtonInstagram = findViewById(R.id.imageButton_instagram);
        mImageButtonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });
    }

    /**
     * Shows a notification on the users device.
     */
    private void showNotification(){

        createNotificationChannel();
        createNotification();
    }

    /**
     * Creates the notification for the users device.
     */
    private void createNotification(){

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("https://scholar.google.com/scholar?hl=en&as_sdt=0,5&q=applied+information+security"));
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.eggplant)
                .setAutoCancel(true)
                .setContentTitle("Redirection")
                .setContentText("Dont waste your time, go to Google Scholar instead")
                .setContentIntent(pi)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Instead of doing silly things like checking what your friends are up to, you should go to Google Scholar:"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    /**
     * Creates the notification channel for the notification.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "testname";
            String description = "testdescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Checks if the app is blocked or not.
     * @param bundleId of the given app.
     */
    private void launchApp(String bundleId) {
        Database db = Database.getinstance();

        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(bundleId);
        if (intent == null) {
            Toast.makeText(this, "could not find that app \n" + bundleId, Toast.LENGTH_SHORT).show();
        } else if (db.getIsBlocking()) {
            intent = packageManager.getLaunchIntentForPackage(db.getExerciseProviderBundleId());
            //Toast.makeText(this, "App blocked!", Toast.LENGTH_SHORT).show();
            Intent blockService = new Intent(FakeHomeScreen.this, BlockService.class);

            if(intent == null) {
                Log.d(TAG, "launchApp: please install..."+db.getSelectedExerciseProviderName());
                Toast.makeText(getBaseContext(), "Please install "+db.getSelectedExerciseProviderName(), Toast.LENGTH_SHORT).show();
                return;
            }
            startService(blockService);
        } else {
            Log.d("Fakehomescreen", "launchApp: blocked!");
            Intent exerciseService = new Intent(FakeHomeScreen.this, FunService.class);
            startService(exerciseService);
        }
        if (intent != null) {
            startActivity(intent);
        }
        else if(db.getIsBlocking()){
            Log.d(TAG, "please install "+db.getSelectedExerciseProviderName());
        }
    }
}
