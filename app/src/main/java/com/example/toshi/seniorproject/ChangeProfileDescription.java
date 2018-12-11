package com.example.toshi.seniorproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeProfileDescription extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private FloatingActionButton changeStatusButton;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_description);

        textInputLayout = findViewById(R.id.textDescriptionLayout);
        changeStatusButton = findViewById(R.id.changeStatusBUtton);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = textInputLayout.getEditText().getText().toString();
                firebaseDatabase.child("status").setValue(status);
                Toast.makeText(getApplicationContext(), "Status Changed", Toast.LENGTH_SHORT).show();
                goBack();
            }
        });

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ProfileSettings.class);
        startActivity(intent);
        finish();
    }

    public void goBack() {
        Intent intent = new Intent(this, ProfileSettings.class);
        startActivity(intent);
        finish();
    }

}
