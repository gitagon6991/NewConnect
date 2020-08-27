package com.example.anko.newconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.anko.newconnect.CommentsActivity;
import com.example.anko.newconnect.FollowersActivity;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.Model.User;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.dinner.DinnerActivity;
import com.example.anko.newconnect.dinner.Fragment.DinnerHomeFragment;
import com.example.anko.newconnect.dinner.Fragment.PostDetailFragment;
import com.example.anko.newconnect.ProfileFragment;
import com.example.anko.newconnect.events.EventsActivity;
import com.example.anko.newconnect.events.EventsFragment.EventsDetailsFragment;
import com.example.anko.newconnect.lunch.LunchActivity;
import com.example.anko.newconnect.lunch.LunchFragment.LunchDetailsFragment;
import com.example.anko.newconnect.travel.TravelActivity;
import com.example.anko.newconnect.travel.TravelFragment.TravelDetailsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ANKO on 25/07/2020.
 */
//Adapters used to fetch posts from the database onto the app, not to crowd the respective sending posts activities
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    public Context mContext;
    public List<Post> mPost; //connect to Model: Post java class

    String postid;

    private FirebaseUser firebaseUser;

    public PostAdapter (Context mContext, List<Post> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }

    //generated methods

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup, false);
        return new ViewHolder(view);
        //viewGroup used in place of parent while int viewType & int position are both int i in tutorial. switch if error.

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       /*new final*/
       final Post post = mPost.get(i);

        Glide.with(mContext).load(post.getPostimage()).into(viewHolder.post_image);

        //new if code. fetch image from db onto the viewholder as long as a description is available
        if (post.getDescription().equals("")){
            viewHolder.description.setVisibility(View.GONE);
        } else {
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(post.getDescription());
        }
//publisher info used to connect users and their posts across database tables
        publisherInfo(viewHolder.image_profile, viewHolder.username, viewHolder.publisher, post.getPublisher());

        //add likes to viewholder
        isLiked(post.getPostid(), viewHolder.like);
        nrLikes(viewHolder.likes, post.getPostid()); //counts the number of likes and updates view holder


        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotifications(post.getPublisher(), post.getPostid());//added after notifications
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        //end of likes part 1. Rest of code below at the bottom.

        //save posts below to appear on profile

        isSaved(post.getPostid(), viewHolder.save);

        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostid()).setValue(true);
                    addNotifications(post.getPublisher(), post.getPostid());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostid()).removeValue();
                }
            }
        });

        //comment method initialization
        getComments(post.getPostid(), viewHolder.comments); //get unique id for posts. each comment is hence unique despite only one Comment activity

        //Start of Comment Activity code
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);
            }
        });
        //use unique post id to identify posts and unique publisher id to identify users
        //end of Comment Activity code

        //likes and followers: following and those who like your posts
        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id", post.getPostid());
                intent.putExtra("title", "following");
                mContext.startActivity(intent);
            }
        });
        //end off likes followers code
        //some error may show because we cant jump from an activity to a fragment so just change the code a little
        //first go to user adapter & add boolean isFragment field

        //TODO on click listener to open profile fragment and post detail fragement
        //post detail fragment code
        viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

        viewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

        viewHolder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

        //TODO: actual path to post deatil below

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();
                //TODO how to use one component for multiple activitues and refer back to them using (mContext instanceof)

                // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //new PostDetailFragment()).commit();

                if (mContext instanceof DinnerActivity){
                      ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                              new PostDetailFragment()).commit();
            } else if (mContext instanceof TravelActivity) {
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new TravelDetailsFragment()).commit();
                } else if (mContext instanceof EventsActivity){
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new EventsDetailsFragment()).commit();
            } else if (mContext instanceof LunchActivity){
                   ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           new LunchDetailsFragment()).commit();
                }

            }
        });

        //end of post detail code

    }

    @Override
    public int getItemCount() {

        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//recycler view components in R.layout.post_items
        public ImageView image_profile, post_image, like, comment, save;
        public TextView username, likes, publisher, description, comments;

        public ViewHolder(@NonNull View itemView){
            super (itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);


        }
    }

    //New commenting functionality
    private void getComments(String postid, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.setText("View All "+dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //end of commenting code

    //New Like functionality
    private void isLiked(String postid, final ImageView imageView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_favorited);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(R.drawable.ic_favorite);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Like Notifications
    private void addNotifications(String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);

    }
    //end of Like Notifications

//get number of likes
    public void nrLikes(final TextView likes, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" likes");
                //space before likes. necessarry between count and text.
               // likes.setText(dataSnapshot.getChildrenCount()+"likes");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //End of Like functionality



    //fetch image,username and profile picture via post publisher information
    private void publisherInfo( /*@NonNull*/ final ImageView image_profile, final TextView username, final TextView publisher, String userid){

       // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("userid");

        //This is the one used. No quotation marks in the last child as ID is not a field you set yourself but is autogenerated
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(/*@NonNull*/ DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                if (user != null) {
                    Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                publisher.setText(user.getUsername());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//save code

    private void isSaved(final String postid, final ImageView imageView){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("Saves")
            .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.child(postid).exists()){
                imageView.setImageResource(R.drawable.ic_save_black);
                imageView.setTag("saved");
            }else{
                imageView.setImageResource(R.drawable.ic_save);
                imageView.setTag("save");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
}
