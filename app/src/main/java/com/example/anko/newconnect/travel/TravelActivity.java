package com.example.anko.newconnect.travel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.anko.newconnect.NewProfileFragment;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.RegisterActivity;

import com.example.anko.newconnect.NotificationsFragment;
import com.example.anko.newconnect.ProfileFragment;
import com.example.anko.newconnect.SearchFragment;
import com.example.anko.newconnect.travel.TravelFragment.TravelHomeFragment;
import com.example.anko.newconnect.travel.TravelPostActivity;
import com.google.firebase.auth.FirebaseAuth;

public class TravelActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    //test out launch reg activity if user is unregistered with mAuth listener
    // private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //mAuthlistener test//doesnt work here, try in Travel Main Activity
       /* mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(TravelActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };*/
        //test end

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TravelHomeFragment()).commit();

    }

     /*protected void onStart() {
        //set up mAuthListerner below
        mAuth.addAuthStateListener(mAuthListener);
        //run to see if issue is fixed
        super.onStart();
    }*/ //doesnt work here, try in Travel activity

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.b_nav_home:
                            selectedFragment = new TravelHomeFragment();
                            break;

                        case R.id.b_nav_search:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.b_nav_add:
                            Intent intent = new Intent(TravelActivity.this,TravelPostActivity.class);
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
