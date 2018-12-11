package com.example.toshi.seniorproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettings extends AppCompatActivity {
    //access database
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;

    private static final int PICKPICK = 123;
    private CircleImageView image2;
    private TextView nameUser;
    private TextView status;
    private ImageView changePic;
    private ImageView changeStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        image2 = findViewById(R.id.userProPic);
        nameUser = findViewById(R.id.textDisplayName);
        status = findViewById(R.id.textDescription);
        changePic = findViewById(R.id.changePicture);
        changeStatus = findViewById(R.id.imageButton2);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();
                String userName = dataSnapshot.child("name").getValue().toString();
                String desc = dataSnapshot.child("status").getValue().toString();

                nameUser.setText(userName);
                status.setText(desc);

                if(!image.equals("default")){
                    Picasso.get().load(image).into(image2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusFunc();
            }
        });

        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent2, "SELECT IMAGE"), PICKPICK);
            }
        });
    }
    @Override
    protected void onActivityResult(int code, int result, Intent data) {
        super.onActivityResult(code, result, data);
        if (code == PICKPICK && result == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
        } else if (code == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult i = CropImage.getActivityResult(data);
            if(result == RESULT_OK) {
                Uri uriResult = i.getUri();
                String current_id = firebaseUser.getUid();
                final StorageReference path = storageReference.child("propics").child(current_id + ".jpg");
                path.putFile(uriResult).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uriD = uri.toString();
                                    databaseReference.child("image").setValue(uriD).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProfileSettings.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(ProfileSettings.this, "Failed to Upload", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            else if (result == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = i.getError();
            }
        }
    }

    private void changeStatusFunc() {
        Intent intent = new Intent(this, ChangeProfileDescription.class);
        startActivity(intent);
        finish();
    }
}
