package com.example.toshi.seniorproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfileActivity extends AppCompatActivity {

    private CircleImageView displayImage;
    private TextView textDisplayName;
    private TextView textStatus;
    private Button friendRequest;
    private Button friendDecline;
    private Button buttonUserLinks;
    private String state;
    private DatabaseReference databaseReference;
    private DatabaseReference userRequestDatabase;
    private DatabaseReference userFriendDatabase;
    private DatabaseReference myRequestDatabase;
    private FirebaseUser currentUser;
    public static final String otherUserId = "id";
    String other_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        other_user_id = getIntent().getStringExtra("other_user_id");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(other_user_id);
        userRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");
        userFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        myRequestDatabase = FirebaseDatabase.getInstance().getReference().child("My Requests");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        displayImage = findViewById(R.id.userProPic);
        textDisplayName = findViewById(R.id.textDisplayName);
        textStatus = findViewById(R.id.textDescription);
        friendRequest = findViewById(R.id.buttonFriendRequest);
        friendDecline = findViewById(R.id.buttonDeclineRequest);
        buttonUserLinks = findViewById(R.id.buttonUserLinks);
        state = "not_friends";
        buttonUserLinks.setVisibility(View.INVISIBLE);
        buttonUserLinks.setEnabled(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String display_image = dataSnapshot.child("image").getValue().toString();
                String display_name = dataSnapshot.child("name").getValue().toString();
                String display_status = dataSnapshot.child("status").getValue().toString();
                textDisplayName.setText(display_name);
                textStatus.setText(display_status);
                if(!display_image.equals("default")){
                    Picasso.get().load(display_image).into(displayImage);
                }

                userRequestDatabase.child(currentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(other_user_id)) {
                                    String type = dataSnapshot.child(other_user_id).child("rType").getValue().toString();
                                    if(type.equals("rReceived")) {
                                        state = "req_received";
                                        friendRequest.setText("Accept Request");
                                        friendDecline.setVisibility(View.VISIBLE);
                                        friendDecline.setEnabled(true);
                                    } else if(type.equals("rSent")) {
                                        state = "req_sent";
                                        friendRequest.setText("Cancel Request");
                                        friendDecline.setVisibility(View.INVISIBLE);
                                        friendDecline.setEnabled(false);
                                    }
                                } else {
                                    userFriendDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(other_user_id)) {
                                                state = "friends";
                                                buttonUserLinks.setVisibility(View.VISIBLE);
                                                buttonUserLinks.setEnabled(true);
                                                friendRequest.setVisibility(View.INVISIBLE);
                                                friendRequest.setEnabled(false);
                                                friendDecline.setVisibility(View.VISIBLE);
                                                friendDecline.setEnabled(true);
                                                friendDecline.setText("Remove Friend");
                                                myRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue();

                                            } else {
                                                state = "not_friends";
                                                friendRequest.setVisibility(View.VISIBLE);
                                                friendRequest.setEnabled(true);
                                                friendDecline.setVisibility(View.INVISIBLE);
                                                friendDecline.setEnabled(false);
                                                myRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        friendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();

            }
        });

        friendDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeUser();
                }
            });

        buttonUserLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessLinks();
            }
        });
    }

    public void accessLinks() {
        Intent intent = new Intent(this, OtherUserLinks.class);
        intent.putExtra(otherUserId, other_user_id);
        startActivity(intent);
    }

    public void addUser() {
        friendRequest.setEnabled(false);
        if(state.equals("not_friends")) {
            userRequestDatabase.child(currentUser.getUid()).child(other_user_id).child("rType").setValue("rSent")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                userRequestDatabase.child(other_user_id).child(currentUser.getUid()).child("rType").setValue("rReceived").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                state = "req_sent";
                                                friendRequest.setText("Cancel Request");
                                                Toast.makeText(OtherUserProfileActivity.this, "Follow Request Sent", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                myRequestDatabase.child(other_user_id).child(currentUser.getUid()).setValue(currentUser);
                            }
                            friendRequest.setEnabled(true);
                        }
                    });
        } else if(state.equals("req_sent")) {
            userRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    userRequestDatabase.child(other_user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendRequest.setEnabled(true);
                            state = "not_friends";
                            friendRequest.setText("Follow User");
                            myRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue();
                            Toast.makeText(OtherUserProfileActivity.this, "Request Removed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else if(state.equals("req_received")) {

            userFriendDatabase.child(currentUser.getUid()).child(other_user_id).setValue(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    userFriendDatabase.child(other_user_id).child(currentUser.getUid()).setValue(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    userRequestDatabase.child(currentUser.getUid()).
                                            child(other_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            userRequestDatabase.child(other_user_id).
                                                    child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    friendRequest.setEnabled(true);
                                                    state = "friends";
                                                    myRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                }
            });
        }
    }

    public void removeUser() {
        if (state.equals("friends")) {
            userFriendDatabase.child(currentUser.getUid()).child(other_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    userRequestDatabase.child(other_user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendRequest.setEnabled(true);
                            state = "not_friends";
                            friendDecline.setEnabled(false);
                            Toast.makeText(OtherUserProfileActivity.this, "Friend Removed", Toast.LENGTH_LONG).show();
                            myRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue();
                        }
                    });
                }
            });
        } else if(state.equals("req_received")) {
            userRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    userRequestDatabase.child(other_user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendRequest.setVisibility(View.INVISIBLE);
                            friendRequest.setEnabled(false);
                            state = "not_friends";
                            friendDecline.setVisibility(View.INVISIBLE);
                            friendDecline.setEnabled(false);
                            myRequestDatabase.child(currentUser.getUid()).child(other_user_id).removeValue();
                            Toast.makeText(OtherUserProfileActivity.this, "Declined", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
