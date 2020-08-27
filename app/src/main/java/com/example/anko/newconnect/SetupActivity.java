package com.example.anko.newconnect;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anko.newconnect.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {
    ImageView mImageView;
    Button mChooseBtn;

    ImageView close, image_profile;
    TextView save;
    Button tv_change;
    MaterialEditText username, bio, age, country, gender, phonenumber;

    FirebaseUser firebaseUser;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;


    /*private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        close =findViewById(R.id.close);
        image_profile =findViewById(R.id.image_profile);
        save =findViewById(R.id.save);
        tv_change =findViewById(R.id.tv_change);
        username =findViewById(R.id.username);
        bio =findViewById(R.id.bio);
        age =findViewById(R.id.age);
        country =findViewById(R.id.country);
        gender =findViewById(R.id.gender);

        phonenumber =findViewById(R.id.phone_number);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                username.setText(user.getUsername());
                bio.setText(user.getBio());
                age.setText(user.getAge());
                gender.setText(user.getGender());
                country.setText(user.getCountry());
                phonenumber.setText(user.getPhonenumber());

                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(SetupActivity.this);
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(SetupActivity.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(username.getText().toString(),
                        bio.getText().toString(),
                        age.getText().toString(),
                        gender.getText().toString(),
                        phonenumber.getText().toString(),
                        country.getText().toString());

                //test finish here later
                // finish();
            }
        });

    }

    private void updateProfile(String username, String bio, String age, String gender, String country, String phonenumber) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("bio", bio);
        hashMap.put("age", age);
        hashMap.put("country", country);
        hashMap.put("gender", gender);
        hashMap.put("phonenumber", phonenumber);

        reference.updateChildren(hashMap);

        //close intent can also be tested here
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private  void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(mImageUri != null){
            final StorageReference filereference = storageRef.child(System.currentTimeMillis()
                    +"."+ getFileExtension(mImageUri));
            uploadTask = filereference.putFile(mImageUri);

            uploadTask = filereference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        //avoid failure error by differentiating !task with task
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("imageurl", ""+myUrl);


                        reference.updateChildren(hashMap);

                        pd.dismiss();
                    } else {
                        Toast.makeText(SetupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    //cntrl + o

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            uploadImage();
        } else {
            Toast.makeText(this,"Something gone wrong!",Toast.LENGTH_SHORT).show();
        }
    }
}

//VIEWS
      /*  mImageView = (ImageView) findViewById(R.id.image_view);
        mChooseBtn = (Button) findViewById(R.id.choose_image_button);

        //handle button click
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check runtime permission
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission NOT granted. Request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission

                    } //inner if closing
                    else {
                        //permission already granted
                        pickImageFromGallery();

                    }//end of inner else function
                } //first if closing

                else {
                    //system os is less than marshmallow
                    pickImageFromGallery();
                } //end of outer else function

            }
        }); //end of button code

    }

    private void pickImageFromGallery() {
        //Intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    //handle result of runtime permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    //permission was denied
                    Toast.makeText(this,"Permission denied...!", Toast.LENGTH_SHORT).show();

                }//end of else clause
            }
        }
    }//end of onRequestPermissionResult

    //handle result of picked image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //finally set image to your image view
            mImageView.setImageURI(data.getData());
        }
    }

}*/
