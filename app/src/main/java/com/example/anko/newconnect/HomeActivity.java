package com.example.anko.newconnect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.Model.User;
import com.example.anko.newconnect.dinner.DinnerActivity;
import com.example.anko.newconnect.ProfileFragment;
import com.example.anko.newconnect.events.EventsActivity;
import com.example.anko.newconnect.lunch.LunchActivity;
import com.example.anko.newconnect.movie.MovieActivity;
import com.example.anko.newconnect.sports.SportsActivity;
import com.example.anko.newconnect.travel.TravelActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CardView travel;
    private CardView lunch;
    private CardView dinner;
    private CardView events;
    private CardView sports;
    private CardView movie;



    ImageView home_profileImage;
    TextView home_userName, home_email;


    Fragment selectedFragment = null;

    Dialog myDialog;
    ImageButton nightmode;

    String profileid;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //@SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Nightmode
      /*  if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }*/ //TODO: remove else brackets if it doesn't work


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //mAuth test 3
       /* mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(HomeActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };*/
        //mAuth

        home_profileImage = (ImageView) findViewById(R.id.home_profileImage);
        home_email = (TextView) findViewById(R.id.home_email);
        home_userName = (TextView) findViewById(R.id.home_userName);

      //  fetchProfilepic();



      //  home_userName.setText(R.string.test);
     //   fetchProfilepic();



        //Theme change
      /*  nightmode = (ImageButton)findViewById(R.id.nightmode);
        nightmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        //Handle Home Categories Here
        lunch = (CardView) findViewById(R.id.lunchClick);

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLunchActivity();
            }
        });

        dinner = (CardView) findViewById(R.id.dinnerClick);

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDinnerActivity();
            }
        });

        travel = (CardView) findViewById(R.id.travelClick);

        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTravelFeedActivity();
            }
        });

        events = (CardView) findViewById(R.id.eventsClick);

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventsActivity();
            }
        });

        sports = (CardView) findViewById(R.id.sportsClick);

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSportsroomActivity();
            }
        });

        movie = (CardView) findViewById(R.id.moviesClick);

        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMoviesActivity();
            }
        });

        //End of Home Dashboard Items


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //open dialog
        myDialog = new Dialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Upgrade to Premium to create your own Unique Events", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ShowPopup();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


   /* private void fetchProfilepic(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                home_userName.setText(user.getUsername());
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(home_profileImage);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/



    public void ShowPopup() {
        TextView close_popup;
        Button premium;
        LinearLayout register;
        LinearLayout login;
        myDialog.setContentView(R.layout.custom_popup);

        close_popup = (TextView) myDialog.findViewById(R.id.close_popup);
        premium = (Button) myDialog.findViewById(R.id.to_announcements);
        register = (LinearLayout) myDialog.findViewById(R.id.to_Register_page);
        login = (LinearLayout) myDialog.findViewById(R.id.to_login_page);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AnnouncementsActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openMoviesActivity() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    //Handle Home Dashboard Activity Listeners
    public void openTravelFeedActivity() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(HomeActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, TravelActivity.class);
                    startActivity(intent);
                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void openLunchActivity() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(HomeActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, LunchActivity.class);
                    startActivity(intent);
                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void openDinnerActivity() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(HomeActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, DinnerActivity.class);
                    startActivity(intent);
                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void openEventsActivity() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(HomeActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, EventsActivity.class);
                    startActivity(intent);
                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void openSportsroomActivity() {
        Intent intent = new Intent(this, SportsActivity.class);
        startActivity(intent);

    }

    //End of Home Items

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            //mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        } else if (id == R.id.action_about){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Home
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
            //TODO test with finish to avoid heavy app. Also try close app functionality
            //finish();
        } else if (id == R.id.nav_loginReg) {
            Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_account) {
            /*fragment = new Fragment();
            loadProfileFragment(fragment);*/
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser()==null){
                        Intent loginIntent = new Intent(HomeActivity.this,RegisterActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                    } else {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                                new NewProfileFragment()).commit();
                    }

                }
            };
            mAuth.addAuthStateListener(mAuthListener);

            //TODO replace and return code below if this doesnt work
           /* getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new ProfileFragment()).commit();*/

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(HomeActivity.this, ScheduleActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody="https://youtube.com/offcentermedia";
            String shareSubject="Your Subject here";

            sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

            startActivity(Intent.createChooser(sharingIntent, "Share Using"));

        }

        //place nightmode & Day Mode code here

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


   /* private void loadProfileFragment(Fragment fragment){
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }*/

    //TODO load profile pic


}
