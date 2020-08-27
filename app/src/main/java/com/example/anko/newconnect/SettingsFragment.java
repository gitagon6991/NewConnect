package com.example.anko.newconnect;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

/**
 * Created by ANKO on 26/07/2020.
 */

public class SettingsFragment extends PreferenceFragment {
    /*SwitchPreference nightmode;
    private Context mContext;
    private Activity mActivity;*/



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        //all new code: TODO

     /*   mContext = this.getActivity();
        mActivity = this.getActivity();

        final SwitchPreference nightmode = (SwitchPreference)findPreference(this.getResources().getString(R.string.night_mode));
        nightmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if(nightmode.isChecked()){
                    Toast.makeText(mActivity,"Unchecked",Toast.LENGTH_SHORT).show();
                    nightmode.setChecked(false);
                } else {
                    Toast.makeText(mActivity,"Checked",Toast.LENGTH_SHORT).show();
                    nightmode.setChecked(true);
                }
                return false;
            }
        });

        //checkbox preference
        final CheckBoxPreference pref_sync = (CheckBoxPreference) findPreference(this.getResources().getString(R.string.sync_pref));
        pref_sync.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if(pref_sync.isChecked()){
                    Toast.makeText(mActivity,"Unchecked",Toast.LENGTH_SHORT).show();
                    pref_sync.setChecked(false);
                } else {
                    Toast.makeText(mActivity,"Checked",Toast.LENGTH_SHORT).show();
                    pref_sync.setChecked(true);
                }
                return false;
            }
        });*/

    }
}
