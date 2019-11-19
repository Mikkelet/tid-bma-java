package com.mikkelthygesen.android.tid_bma_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.transition.Transition;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikkelthygesen.android.tid_bma_java.Storage.AppDatabase;
import com.mikkelthygesen.android.tid_bma_java.Storage.DatabaseSingleton;


public class MainActivity extends AppCompatActivity {

    private StartSession mStartSessionFragment = new StartSession();

    private BottomNavigationView mBottomNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseSingleton.getInstance(getApplicationContext());

        openFragment(mStartSessionFragment);
        mBottomNavigationMenu = findViewById(R.id.bottomNavigationView);
        mBottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_start_session:
                        openFragment(mStartSessionFragment);
                        return true;
                    case R.id.navigation_blacklist:
                        BlackList blackList = new BlackList();
                        openFragment(blackList);
                        return true;
                    case R.id.navigation_timer:
                        Timer timer = Timer.newInstance();
                        openFragment(timer);
                        return true;
                }
                return false;
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
