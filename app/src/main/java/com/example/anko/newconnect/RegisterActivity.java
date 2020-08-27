package com.example.anko.newconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    private TextView signIn, terms_settings;
    private EditText username, email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button registerBtn;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signIn = (TextView) findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });

        terms_settings = findViewById(R.id.terms_settings);
        terms_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,SettingsActivity.class));
                finish();;
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                finish();;
            }
        });

        username = (EditText) findViewById(R.id.nameField);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passField);
        registerBtn = (Button) findViewById(R.id.buttonReg);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_username, txt_email, txt_password);
                }

            }
        });

        mAuth = FirebaseAuth.getInstance();

        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    private void startSignInActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    private void register(final String username, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){



                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/newconnect-ab229.appspot.com/o/placeholder.png?alt=media&token=28076eb3-c0a6-4658-a59a-47754fa48361");
                    hashMap.put("bio", "");
                    hashMap.put("age", "");
                    hashMap.put("country", "");
                    hashMap.put("gender", "");
                    hashMap.put("phonenumber", "");

                    mDatabase.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();;
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //return onClick method if old code is returned


    /*public void signUpButtonClicked(View view) {
        final String name_content, email_content, password_content;
        name_content = name.getText().toString().trim();
        email_content = email.getText().toString().trim();
        password_content = password.getText().toString().trim();
        if (!TextUtils.isEmpty(email_content) && !TextUtils.isEmpty(name_content) && !TextUtils.isEmpty(password_content)) {
            //create users
            mAuth.createUserWithEmailAndPassword(email_content, password_content).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("Name").setValue(name_content);

                        Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(registerIntent);
                    }
                }
            });

        }

    }*/
}
