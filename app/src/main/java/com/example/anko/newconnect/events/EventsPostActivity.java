package com.example.anko.newconnect.events;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anko.newconnect.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class EventsPostActivity extends AppCompatActivity {
    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask; //change it works
    //StorageTask urlTask;
    StorageReference storageReference;

    ImageView close, image_added;
    TextView post;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_post);
        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        storageReference = FirebaseStorage.getInstance().getReference("eventsposts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsPostActivity.this,EventsActivity.class));
                finish();;
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        CropImage.activity().setAspectRatio(1,1).start(EventsPostActivity.this);


    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "."+ getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            // urlTask = fileReference.putFile(imageUri);

           /* Task<Uri> urlTask = uploadTask.continuewithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
                                                                @Override
                                                                public Task<Uri> then(@NonNull Task task) throws Exception {
                                                                    if (!task.isSuccessful()){
                                                                        throw task.getException();
                                                                    }

                                                                    return fileReference.getDownloadUrl();
                                                                }
                                                            })*/

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EventsPosts");

                        String postid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", myUrl);
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(EventsPostActivity.this,EventsActivity.class));
                        finish();

                    }else {
                        Toast.makeText(EventsPostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EventsPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    //ctrl + o
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image_added.setImageURI(imageUri);
        } else {
            Toast.makeText(this,"Something gone wrong!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EventsPostActivity.this,EventsActivity.class));
            finish();
        }

    }
}
