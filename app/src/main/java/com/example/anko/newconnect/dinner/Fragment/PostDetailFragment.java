package com.example.anko.newconnect.dinner.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anko.newconnect.Adapter.PostAdapter;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailFragment extends Fragment {
    String postid;

    //test this one for delete
    private String post_key;


    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdapter postAdapter;

    ImageView share, delete, invite;

    TextView inviteText;

    FirebaseUser firebaseUser;
    String profileid;

    DatabaseReference mDatabase;


    public Context mContext;
    public List<Post> mPost; //connect to Model: Post java class

    //for delete
    private FirebaseAuth mAuth;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid = preferences.getString("postid", "none");

        mDatabase = FirebaseDatabase.getInstance().getReference("DinnerPosts");
        // post_key = getIntent().getExtras.getString("postid");//seems to only work on activities not fragments


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        invite = view.findViewById(R.id.invite);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotifications();
                // addNotifications(post.getPublisher(), post.getPostid());
                Toast.makeText(getContext(),"Dinner Request Sent",Toast.LENGTH_SHORT).show();

            }
        });

        inviteText = view.findViewById(R.id.inviteText);
        inviteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotifications();
                // addNotifications(post.getPublisher(), post.getPostid());
                Toast.makeText(getContext(),"Dinner Request Sent",Toast.LENGTH_SHORT).show();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody="https://youtube.com/offcentermedia";
                String shareSubject="Your Subject here";

                sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

                startActivity(Intent.createChooser(sharingIntent, "Share Using"));

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });

        readPost();

        return view;
    }

    private void deletePost() {
        //same as saying DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child();
        mDatabase.child(postid).removeValue();
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DinnerHomeFragment()).commit();
    }

    private void readPost() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DinnerPosts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();

                Post post = dataSnapshot.getValue(Post.class);

                postList.add(post);

                postAdapter.notifyDataSetChanged();

                if (mAuth.getCurrentUser().getUid().equals(postid)){
                    delete.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addNotifications(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        //TODO test this later
        //DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Notifications").child(postid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "invited you to a dinner date");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);

    }

}
