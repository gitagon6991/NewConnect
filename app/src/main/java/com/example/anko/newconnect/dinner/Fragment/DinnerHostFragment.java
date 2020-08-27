package com.example.anko.newconnect.dinner.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anko.newconnect.Adapter.PostAdapter;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class DinnerHostFragment extends Fragment {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    /**
     * The {@link ViewPager} that will host the section contents.
     */


    private PostAdapter postAdapter;
    private List<Post> postLists;
    String profileid;
    FirebaseUser firebaseUser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private List<String> followingList;

    ViewPager viewPager;
    ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dinner_host, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");
        //remove if there is an issue


      // mSectionsPagerAdapter = new NotificationsActivity.SectionsPagerAdapter(getFragmentManager());
       // mSectionsPagerAdapter = new NotificationsActivity.SectionsPagerAdapter(getFragmentManager());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));




       // TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        //ViewPager viewPager = view.findViewById(R.id.view_pager);
        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
       // ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
       // viewPagerAdapter.addFragment(new DinnerHomeFragment(), "My Feed");
        //viewPagerAdapter.addFragment(new GeneralFeedFragment(), "General");

       // viewPagerAdapter.addFragment(new PlaceholderFragment(), "Placeholder");

       // viewPager.setAdapter(viewPagerAdapter);
        //tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent;
                    loginIntent = new Intent(getContext(),RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };

        firebaseUser = mAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onStart() {
        //set up mAuthListerner below
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //  return PlaceholderFragment.newInstance(position + 1);
            Fragment fragment = null;
            switch(position){
                case 0:

                    fragment = new DinnerHomeFragment();

                    break;
                case  1:
                    fragment = new GeneralFeedFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }








    //new viewpager code
    /*class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super (fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        //cntrl + o to generate methods

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    */

}