package com.example.anko.newconnect.lunch.LunchFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.Adapter.PostAdapter;
import com.example.anko.newconnect.HomeActivity;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.Model.User;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LunchHomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;

    String profileid;
    FirebaseUser firebaseUser;
    private ImageView profile_pic, back_btn;

    private List<String> followingList;

    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lunch_home, container, false);

        progressBar = view.findViewById(R.id.progress_circular);

        //get current user, new code
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");
        //remove if there is an issue

        back_btn = view.findViewById(R.id.back_btn);

        //TODO profile pic and back button test
        profile_pic = view.findViewById(R.id.profile_pic);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  startActivity(new Intent(this.getContext(),HomeActivity.class));
                finish();  */
                startActivity(new Intent(getContext(), HomeActivity.class));

            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

        fetchProfilepic();
        //end of new code test

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recyclerView.setAdapter(postAdapter);

        checkFollowing();

        return view;
    }
    private void checkFollowing(){
        followingList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                readPosts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LunchPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postLists.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for (String id : followingList){
                        if (post.getPublisher().equals(id)){
                            postLists.add(post);
                        }
                    }
                }

                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //TODO load profile pic
    private void fetchProfilepic(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (getContext() == null) {
                    return;
                }
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageurl()).into(profile_pic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
