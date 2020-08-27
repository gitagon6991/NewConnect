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

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.Model.Post;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.dinner.DinnerActivity;
import com.example.anko.newconnect.dinner.Fragment.PostDetailFragment;
import com.example.anko.newconnect.events.EventsActivity;
import com.example.anko.newconnect.events.EventsFragment.EventsDetailsFragment;
import com.example.anko.newconnect.lunch.LunchActivity;
import com.example.anko.newconnect.lunch.LunchFragment.LunchDetailsFragment;
import com.example.anko.newconnect.travel.TravelActivity;
import com.example.anko.newconnect.travel.TravelFragment.TravelDetailsFragment;

import java.util.List;

/**
 * Created by ANKO on 07/08/2020.
 */

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.ViewHolder> {

    public Context context;
    public List<Post> mPosts;

    //alt + insert


    public MyPhotoAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.photos_item, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Post post = mPosts.get(i);

        Glide.with(context).load(post.getPostimage()).into(viewHolder.post_image);

        /*viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        });*/

        //TODO: actual path to post deatil below

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();
                //TODO how to use one component for multiple activitues and refer back to them using (mContext instanceof)

                // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //new PostDetailFragment()).commit();

                if (context instanceof DinnerActivity){
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();
                } else if (context instanceof TravelActivity) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new TravelDetailsFragment()).commit();
                } else if (context instanceof EventsActivity){
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new EventsDetailsFragment()).commit();
                } else if (context instanceof LunchActivity){
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new LunchDetailsFragment()).commit();
                }

            }
        });

        //end of post detail code
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView post_image;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
        }

    }

}
