package com.example.anko.newconnect.movie;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.HomeActivity;
import com.example.anko.newconnect.R;
import com.example.anko.newconnect.RegisterActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.example.anko.newconnect.Model.Message;
import com.example.anko.newconnect.R;

import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private EditText editMessage;
    private DatabaseReference mDatabase;
    private RecyclerView mMessageList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private LinearLayout back;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


         /*  Toolbar toolbar = (Toolbar) findViewById(R.id.sports_toolbar);
        setSupportActionBar(toolbar);*/

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieActivity.this,HomeActivity.class));
                finish();
            }
        });

        editMessage = (EditText) findViewById(R.id.movie_editMessage);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Movie_Messages");
        mMessageList = (RecyclerView) findViewById(R.id.movie_messageRec);
        mMessageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMessageList.setLayoutManager(linearLayoutManager);
        //authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MovieActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };



    }

    public void movie_sendButtonClicked(View view){
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        //auth above

        final String messageValue = editMessage.getText().toString().trim();
        if(!TextUtils.isEmpty(messageValue)){
            final DatabaseReference newPost = mDatabase.push();
            //new auth code
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("content").setValue(messageValue);
                    //get username reference too
                    newPost.child("username").setValue(dataSnapshot.child("username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    newPost.child("imageurl").setValue(dataSnapshot.child("imageurl").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mMessageList.scrollToPosition(mMessageList.getAdapter().getItemCount());
            //end of auth code
            // cut this line and paste it in the above ValueEventListener: newPost.child("content").setValue(messageValue);
        }
    }

    @Override
    protected void onStart() {
        //set up mAuthListerner below
        mAuth.addAuthStateListener(mAuthListener);
        //run to see if issue is fixed
        super.onStart();
        FirebaseRecyclerAdapter <Message,MessageViewHolder> FBRA = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.singlemessagefile,
                MessageViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());

                //set the viewholder for the username after passing the username in the message class
                viewHolder.setUsername(model.getUsername());

                //try this
                // viewHolder.setImage(model.getImage());
                viewHolder.setImage(getApplicationContext(),model.getImageurl());

            }
        };
        mMessageList.setAdapter(FBRA);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MessageViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setContent(String content){
            TextView message_content = (TextView) mView.findViewById(R.id.messageText);
            //reference to singlemessagefile
            message_content.setText(content);

        }

        public void setUsername(String username){
            TextView username_content = (TextView) mView.findViewById(R.id.usernameText);
            username_content.setText(username);
        }

        public void setImage (Context ctx, String imageurl){
            //get reference to imageview first
            ImageView image_profile = (ImageView) itemView.findViewById(R.id.image_profile);
            //use Picasso
            Picasso.with(ctx).load(imageurl).into(image_profile);
        }
    }


}

