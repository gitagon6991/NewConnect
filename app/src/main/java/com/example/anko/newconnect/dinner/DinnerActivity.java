package com.example.anko.newconnect.dinner;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.anko.newconnect.NewProfileFragment;
import com.example.anko.newconnect.NotificationsActivity;
//import com.example.anko.newconnect.dinner.Fragment.GeneralFragment;
import com.example.anko.newconnect.dinner.Fragment.DinnerHomeFragment;
import com.example.anko.newconnect.NotificationsFragment;
import com.example.anko.newconnect.ProfileFragment;
import com.example.anko.newconnect.SearchFragment;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.dinner.Fragment.DinnerHostFragment;

import java.util.ArrayList;

public class DinnerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras(); //use travel or lunch intent in respective activities
        if (intent != null){
            String publisher = intent.getString("publisherid");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NewProfileFragment()).commit();
        } else {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DinnerHostFragment()).commit();
        }
        //end of Bundle instance



    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.b_nav_home:
                            selectedFragment = new DinnerHostFragment();
                            break;

                        case R.id.b_nav_search:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.b_nav_add:
                            Intent intent = new Intent(DinnerActivity.this,DinnerPostActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.b_nav_notifications:
                            //Intent noteIntent = new Intent(DinnerActivity.this,NotificationsActivity.class);
                           // startActivity(noteIntent);
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
