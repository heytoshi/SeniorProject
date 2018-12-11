package com.example.toshi.seniorproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;


public class ProfileActivity extends AppCompatActivity {

    private Button btn;
    private Button save;
    private TextView nickname;
    public static final String NICKNAME = "usernickname";
    public static final String LINK = "link";

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private EditText yourLink;
    private DatabaseReference firebaseDatabase;
    private Button saveLink;
    private EditText nameForLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nickname = findViewById(R.id.nickname);
        btn = findViewById(R.id.enterchat);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Links");
        yourLink = findViewById(R.id.pasteYourLink);
        saveLink = findViewById(R.id.saveLink);
        nameForLink = findViewById(R.id.nameForLink);

        saveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yourLink.getText().toString().isEmpty() && !nameForLink.getText().toString().isEmpty()) {
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", nameForLink.getText().toString());
                    userMap.put("link", yourLink.getText().toString());
                    firebaseDatabase.push().setValue(userMap);
                }
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue().toString();
                nickname.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the link is not empty
                if(!yourLink.getText().toString().isEmpty()) {
                    finish();
                    Intent i = new Intent(ProfileActivity.this, MainMediaPlayer.class);
                    i.putExtra(NICKNAME, nickname.getText().toString());
                    i.putExtra(LINK, yourLink.getText().toString());
                    startActivity(i);
                }
            }
        });
    }

}

