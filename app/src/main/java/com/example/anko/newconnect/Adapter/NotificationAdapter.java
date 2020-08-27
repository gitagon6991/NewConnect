package com.example.anko.newconnect.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.Model.Notification;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.Model.User;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.dinner.DinnerActivity;
import com.example.anko.newconnect.dinner.Fragment.PostDetailFragment;
import com.example.anko.newconnect.ProfileFragment;
import com.example.anko.newconnect.events.EventsActivity;
import com.example.anko.newconnect.events.EventsFragment.EventsDetailsFragment;
import com.example.anko.newconnect.lunch.LunchActivity;
import com.example.anko.newconnect.lunch.LunchFragment.LunchDetailsFragment;
import com.example.anko.newconnect.travel.TravelActivity;
import com.example.anko.newconnect.travel.TravelFragment.TravelDetailsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by ANKO on 02/08/2020.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Notification notification = mNotification.get(i);

        viewHolder.text.setText(notification.getText());

        getUserInfo(viewHolder.image_profile, viewHolder.username, notification.getUserid());

        if (notification.isIspost()) {
            viewHolder.post_image.setVisibility(View.VISIBLE);
            getPostImage(viewHolder.post_image, notification.getPostid());
//TODO insert other db methods here e.g get getTravelImage, getLunchImage
            getTravelImage(viewHolder.post_image, notification.getPostid());
            getEventsImage(viewHolder.post_image, notification.getPostid());
            getLunchImage(viewHolder.post_image, notification.getPostid());
        } else {
            viewHolder.post_image.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.isIspost()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", notification.getPostid());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();

                } else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", notification.getUserid());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_profile, post_image;
        public TextView username, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Glide.with(mContext).load(user.getImageurl()).into(imageView);

                    username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getPostImage(final ImageView imageView, final String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DinnerPosts").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    Glide.with(mContext).load(post.getPostimage()).into(imageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //TODO check if this works in notifications
        //lunch and travel database references

    }

    private void getTravelImage(final ImageView imageView, final String postid) {

        DatabaseReference referenceTravel = FirebaseDatabase.getInstance().getReference("TravelPosts").child(postid);

        referenceTravel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    Glide.with(mContext).load(post.getPostimage()).into(imageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getLunchImage(final ImageView imageView, final String postid) {

        DatabaseReference referenceLunch = FirebaseDatabase.getInstance().getReference("LunchPosts").child(postid);

        referenceLunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    Glide.with(mContext).load(post.getPostimage()).into(imageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getEventsImage(final ImageView imageView, final String postid) {

        DatabaseReference referenceEvents = FirebaseDatabase.getInstance().getReference("EventsPosts").child(postid);

        referenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    Glide.with(mContext).load(post.getPostimage()).into(imageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}