package com.example.anko.newconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.Adapter.MyPhotoAdapter;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class NewProfileFragment extends Fragment {

    ImageView image_profile, options;
    TextView posts, followers, following, username, bio, age, country, gender;
    Button edit_profile;

    LinearLayout chat, invite, show_my_details, my_posts, my_saves, show_posts, followersLayout, followingLayout;

    private List<String> mySaves;

    MyPhotoAdapter myPhotoAdapter_saves, myPhotoAdapter_saves2, myPhotoAdapter_saves3, myPhotoAdapter_saves4;
    List<Post> postList_saves, postList_saves2, postList_saves3, postList_saves4;


    RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4;
    RecyclerView recyclerView_save, recyclerView_save2, recyclerView_save3, recyclerView_save4;

    MyPhotoAdapter myPhotoAdapter, myPhotoAdapter2, myPhotoAdapter3, myPhotoAdapter4;
    List<Post> postList, postList2, postList3, postList4;

    FirebaseUser firebaseUser;
    String profileid;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    ImageButton my_details, my_photos, saved_photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_profile, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

       SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
       profileid = prefs.getString("profileid", "none");



        image_profile = view.findViewById(R.id.image_profile);
        // options = view.findViewById(R.id.imagee);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        username = view.findViewById(R.id.username);
        bio = view.findViewById(R.id.bio);
        age = view.findViewById(R.id.age);
        country = view.findViewById(R.id.country);
        gender = view.findViewById(R.id.gender);
        edit_profile = view.findViewById(R.id.edit_profile);
        my_photos = view.findViewById(R.id.my_photos);//image button
        saved_photos = view.findViewById(R.id.saved_photos);//image button
        my_details = view.findViewById(R.id.my_details); //image button
        show_my_details = view.findViewById(R.id.show_my_details); //linear layout to be replaced by recycler views

        my_posts = view.findViewById(R.id.my_posts);

        my_saves = view.findViewById(R.id.my_saves);

        show_posts = view.findViewById(R.id.show_posts);

        followersLayout = view.findViewById(R.id.followersLayout);

        followingLayout = view.findViewById(R.id.followingLayout);



        //recycler vies 8 of them
//Start with My posts recycler views
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        myPhotoAdapter = new MyPhotoAdapter(getContext(), postList);
        recyclerView.setAdapter(myPhotoAdapter);
//2
        recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new GridLayoutManager(getContext(), 3);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        postList2 = new ArrayList<>();
        myPhotoAdapter2 = new MyPhotoAdapter(getContext(), postList2);
        recyclerView2.setAdapter(myPhotoAdapter2);
//3
        recyclerView3 = view.findViewById(R.id.recycler_view3);
        recyclerView3.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager3 = new GridLayoutManager(getContext(), 3);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        postList3 = new ArrayList<>();
        myPhotoAdapter3 = new MyPhotoAdapter(getContext(), postList3);
        recyclerView3.setAdapter(myPhotoAdapter3);
//4
        recyclerView4 = view.findViewById(R.id.recycler_view4);
        recyclerView4.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager4 = new GridLayoutManager(getContext(), 3);
        recyclerView4.setLayoutManager(linearLayoutManager4);
        postList4 = new ArrayList<>();
        myPhotoAdapter4 = new MyPhotoAdapter(getContext(), postList4);
        recyclerView4.setAdapter(myPhotoAdapter4);

//My saves recycler views
        recyclerView_save = view.findViewById(R.id.recycler_view_save);
        recyclerView_save.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves = new GridLayoutManager(getContext(), 3);
        recyclerView_save.setLayoutManager(linearLayoutManager_saves);
        postList_saves = new ArrayList<>();
        myPhotoAdapter_saves = new MyPhotoAdapter(getContext(), postList_saves);
        recyclerView_save.setAdapter(myPhotoAdapter_saves);
//2
        recyclerView_save2 = view.findViewById(R.id.recycler_view_save2);
        recyclerView_save2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves2 = new GridLayoutManager(getContext(), 3);
        recyclerView_save2.setLayoutManager(linearLayoutManager_saves2);
        postList_saves2 = new ArrayList<>();
        myPhotoAdapter_saves2 = new MyPhotoAdapter(getContext(), postList_saves2);
        recyclerView_save2.setAdapter(myPhotoAdapter_saves2);
//3
        recyclerView_save3 = view.findViewById(R.id.recycler_view_save3);
        recyclerView_save3.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves3 = new GridLayoutManager(getContext(), 3);
        recyclerView_save3.setLayoutManager(linearLayoutManager_saves3);
        postList_saves3 = new ArrayList<>();
        myPhotoAdapter_saves3 = new MyPhotoAdapter(getContext(), postList_saves3);
        recyclerView_save3.setAdapter(myPhotoAdapter_saves3);
//4
        recyclerView_save4 = view.findViewById(R.id.recycler_view_save4);
        recyclerView_save4.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves4 = new GridLayoutManager(getContext(), 3);
        recyclerView_save4.setLayoutManager(linearLayoutManager_saves4);
        postList_saves4 = new ArrayList<>();
        myPhotoAdapter_saves4 = new MyPhotoAdapter(getContext(), postList_saves4);
        recyclerView_save4.setAdapter(myPhotoAdapter_saves4);

        //Toggling views

        show_my_details.setVisibility(View.VISIBLE);
        my_posts.setVisibility(View.GONE);
        my_saves.setVisibility(View.GONE);

        //recyclerView.setVisibility(View.GONE);
        //recyclerView_save.setVisibility(View.GONE);

        chat = view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getContext(), NotificationsActivity.class));
            }
        });

        invite = view.findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvite();
                Toast.makeText(getContext(),"Invitation Sent",Toast.LENGTH_SHORT).show();
            }
        });

        show_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_my_details.setVisibility(View.GONE);
                my_posts.setVisibility(View.VISIBLE);
                my_saves.setVisibility(View.GONE);
            }
        });

        userInfo();
        getFollowers();
        getNrPosts();

        //new post numbers check if they successfully increment
        getNrPosts2();
        getNrPosts3();
        getNrPosts4();

        myPhotos();

        //new my posts methods
        myPhotos2();
        myPhotos3();
        myPhotos4();

        //all read saves methods collected into mySaves method
        mysaves();

        totalPosts();
        //addNotifications();


        profileid.equals(firebaseUser.getUid());
        edit_profile.setText("Edit Profile");


     /*   if(profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Edit Profile");
        }else{
            checkFollow();
            saved_photos.setVisibility(View.GONE);
        }*/
//TODO fix notifications issue
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if(btn.equals("Edit Profile")){
                    //go to edit profile/set up activity
                    startActivity(new Intent(getContext(), SetupActivity.class));

                }else if(btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                    //add followers notification
                    addNotifications();
                } else if (btn.equals("following")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                    //add followers notification
                    // addNotifications(user.getId());
                }
            }
        });

        my_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_my_details.setVisibility(View.VISIBLE);
                my_posts.setVisibility(View.GONE);
                my_saves.setVisibility(View.GONE);

                //recyclerView.setVisibility(View.GONE);
                // recyclerView_save.setVisibility(View.GONE);
            }
        });

        my_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_my_details.setVisibility(View.GONE);
                my_posts.setVisibility(View.VISIBLE);
                my_saves.setVisibility(View.GONE);
            }
        });

        saved_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_my_details.setVisibility(View.GONE);
                my_posts.setVisibility(View.GONE);
                my_saves.setVisibility(View.VISIBLE);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

        //new followers click dependent on layout
        followersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

        //end

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                startActivity(intent);
            }
        });

        //following layout onClick. remove above if redundant

        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                startActivity(intent);
            }
        });

        //end





        return view;

    }


    private void sendInvite() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        //TODO test this later
        //DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Notifications").child(postid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "sent an invite");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }


    //add Follow notifications to profile



    private void addNotifications(){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);

    }
    //end of Followed Notifications

    //get user info
    private void userInfo(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

       // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (getContext() == null) {
                    return;
                }
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);

                username.setText(user.getUsername());
                bio.setText(user.getBio());

                age.setText(user.getAge());
                country.setText(user.getCountry());
                gender.setText(user.getGender());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //check follow

    private void checkFollow(){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(profileid).exists()) {
                    edit_profile.setText("following");
                    //set text size edit_profile.setTextSize();
                } else {
                    edit_profile.setText("follow");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //getfollowers
//below code only works when the databse is the same even if the children are different
    private void getFollowers(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("following");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void totalPosts(){
      //  getNrPosts();
        //getNrPosts2();
        //getNrPosts3();
        //getNrPosts4();
        //posts.setText("");
    }

    //get number of posts/my events
    public void getNrPosts() {



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DinnerPosts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        i++;
                    }
                }
                 posts.setText("" + i);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getNrPosts2() {


        DatabaseReference referenceLunch = FirebaseDatabase.getInstance().getReference("LunchPosts");

        referenceLunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO change here profile posts count
                //try this later
                // int i = 0;
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        i++;
                    }
                }
                // posts.setText(""+i+j); //TODO or try having all database references together: research on that
                 posts.setText("" + i);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getNrPosts3() {


        DatabaseReference referenceTravel = FirebaseDatabase.getInstance().getReference("TravelPosts");

        referenceTravel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO another change from i to k
                //int i, j = 0;
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        i++;
                    }
                }
                 posts.setText("" + i);


                int c = i;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getNrPosts4() {



        DatabaseReference referenceEvents = FirebaseDatabase.getInstance().getReference("EventsPosts");

        referenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //from i to l
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        i++;
                    }
                }
                  posts.setText("" + i);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //methods for photo adapter

    private void myPhotos() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DinnerPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileid)) {
                        postList.add(post);
                    }

                }
                //check notifications to fix issue
                Collections.reverse(postList);

                myPhotoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void myPhotos2() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LunchPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList2.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileid)) {
                        postList2.add(post);
                    }

                }

                Collections.reverse(postList2);
                myPhotoAdapter2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void myPhotos3() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TravelPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileid)) {
                        postList3.add(post);
                    }

                }

                Collections.reverse(postList3);
                myPhotoAdapter3.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void myPhotos4() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EventsPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList4.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileid)){
                        postList4.add(post);
                    }

                }

                Collections.reverse(postList4);
                myPhotoAdapter4.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //my saves code. fetch from all databases
    private void mysaves(){


        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mySaves.add(snapshot.getKey());
                }
                readsaves();
                readsaves2();
                readsaves3();
                readsaves4();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readsaves() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DinnerPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_saves.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : mySaves) {
                        if (post.getPostid().equals(id)) {
                            postList_saves.add(post);
                        }
                    }

                }

                myPhotoAdapter_saves.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readsaves2() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LunchPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_saves2.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : mySaves) {
                        if (post.getPostid().equals(id)) {
                            postList_saves2.add(post);
                        }
                    }

                }

                myPhotoAdapter_saves2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readsaves3(){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TravelPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_saves3.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    for(String id : mySaves){
                        if (post.getPostid().equals(id)){
                            postList_saves3.add(post);
                        }
                    }

                }

                myPhotoAdapter_saves3.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void readsaves4(){
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EventsPosts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_saves4.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    for(String id : mySaves){
                        if (post.getPostid().equals(id)){
                            postList_saves4.add(post);
                        }
                    }

                }

                myPhotoAdapter_saves4.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
