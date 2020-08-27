package com.example.anko.newconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    private Switch myswitch;
    private Context mContext;
    private Activity mActivity;
    private CardView logout, gen_settings;

    LinearLayout disappear;

    private ImageView profile_pic, back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO Nightmode implementation
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        } else setTheme(R.style.AppTheme);
        //night mode end

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myswitch = (Switch)findViewById(R.id.mySwitch);
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });

        gen_settings = findViewById(R.id.gen_settings);
        disappear = findViewById(R.id.disappear_settings);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        back_btn = (ImageView) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  startActivity(new Intent(this.getContext(),HomeActivity.class));
                finish();  */
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

            }
        });

        gen_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disappear.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().add(R.id.fragment_container,new SettingsFragment()).commit();

               /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       new SettingsFragment()).commit();*/
            }
        });


    }

    public void restartApp(){
        Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
        finish();
    }
}
