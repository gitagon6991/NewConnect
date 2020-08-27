package com.example.anko.newconnect.lunch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.anko.newconnect.NewProfileFragment;
import com.example.anko.newconnect.R;

import com.example.anko.newconnect.SearchFragment;
import com.example.anko.newconnect.NotificationsFragment;
import com.example.anko.newconnect.ProfileFragment;
import com.example.anko.newconnect.lunch.LunchFragment.LunchHomeFragment;
import com.example.anko.newconnect.lunch.LunchPostActivity;


public class LunchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new LunchHomeFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.b_nav_home:
                            selectedFragment = new LunchHomeFragment();
                            break;

                        case R.id.b_nav_search:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.b_nav_add:
                            Intent intent = new Intent(LunchActivity.this,PostActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.b_nav_notifications:
                            selectedFragment = new NotificationsFragment();
                            break;
                        case R.id.b_nav_profile:
                            selectedFragment = new NewProfileFragment();
                            break;


                    }
                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }

                    return true;
                }
            };
}
