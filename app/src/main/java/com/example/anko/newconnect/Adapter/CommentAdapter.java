package com.example.anko.newconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.HomeActivity;
import com.example.anko.newconnect.Model.Comment;
import com.example.anko.newconnect.Model.User;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.dinner.DinnerActivity;
import com.example.anko.newconnect.lunch.LunchActivity;
import com.example.anko.newconnect.travel.TravelActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by ANKO on 31/07/2020.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mComment; //connect adapter to package Model: Comment java class that contains getters and setters

    private FirebaseUser firebaseUser;


    //click Alt Insert to generate methods


    public CommentAdapter(Context mContext, List<Comment> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup /*parent*/, int i/*viewType*/) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,viewGroup, false);
        return new ViewHolder(view); //translate layout onto view
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i/*position*/) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final  Comment comment = mComment.get(i);

        viewHolder.comment.setText(comment.getComment());

       // getUserInfo(viewHolder.image_profile, viewHolder.comment, comment.getPublisher());
        getUserInfo(viewHolder.image_profile, viewHolder.username, comment.getPublisher());

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO still works even without other activities mentioned


                Intent intent = new Intent(mContext, DinnerActivity.class);
                //Intent travelIntent = new Intent(mContext, TravelActivity.class);
                //Intent lunchIntent = new Intent(mContext, LunchActivity.class);//find a way to alter this. maybe use finish(); or close
                    //leave these 2. Maybe just define intent then add finish();
                    //mContext is very important so maybe use if - else function for the 3 different activities
                intent.putExtra("publisherid", comment.getPublisher());
                    mContext.startActivity(intent);

              //  travelIntent.putExtra("publisherid", comment.getPublisher());
                //mContext.startActivity(travelIntent);

                //lunchIntent.putExtra("publisherid", comment.getPublisher());
                //mContext.startActivity(lunchIntent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image_profile;
        public TextView username, comment;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(imageView);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
