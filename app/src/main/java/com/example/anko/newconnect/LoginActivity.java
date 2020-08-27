package com.example.anko.newconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anko.newconnect.dinner.DinnerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class   LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private Button btn_login;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPass = (EditText) findViewById(R.id.loginPass);
        btn_login = (Button) findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();

        textView3 = findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(registerIntent);
            }
        });

        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new code for auth
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait....");
                pd.show();
                //end of new code. check again online
                String txt_email = loginEmail.getText().toString();
                String txt_password = loginPass.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                } else{
                    mAuth.signInWithEmailAndPassword(txt_email,txt_password).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        //new auth code again. hope it works
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(mAuth.getCurrentUser().getUid());
                                        mDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                pd.dismiss();
                                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        //end of new auth code

                                       /*Return if new auth code fails: Intent intent = new Intent(LoginActivity.this,DinnerActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();*/
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }


    /*public void loginButtonClick(View view) {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkUserExists();
                    }
                }
            });
        }
    }

    private void checkUserExists() {
        final String userid = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(userid)) {
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

}
