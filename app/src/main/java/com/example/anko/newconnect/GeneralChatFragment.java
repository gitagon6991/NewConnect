package com.example.anko.newconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anko.newconnect.Model.Message;
import com.example.anko.newconnect.sports.SportsActivity;
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

import java.util.List;


public class GeneralChatFragment extends Fragment {
    private EditText editMessage;
    private Button button_send;
    private DatabaseReference mDatabase;
    private RecyclerView mMessageList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private LinearLayout back;
    private List<Message> message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_general_chat, container, false);


        editMessage = view.findViewById(R.id.editMessage);
        button_send = view.findViewById(R.id.button_send);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("General_Messages");
        mMessageList = view.findViewById(R.id.messageRec);
        mMessageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mMessageList.setLayoutManager(linearLayoutManager);
        //authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(getContext(),RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButtonClicked();
            }
        });

        return view;

    }

    public void sendButtonClicked(){
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
    public void onStart() {
        //set up mAuthListerner below
        mAuth.addAuthStateListener(mAuthListener);
        //run to see if issue is fixed
        super.onStart();
        FirebaseRecyclerAdapter<Message,MessageViewHolder> FBRA = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
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
                //  viewHolder.setImage(getApplicationContext(),model.getImageurl());
                viewHolder.setImage(getContext(),model.getImageurl());

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


