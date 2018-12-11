package com.example.toshi.seniorproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView friendList;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private View v;
    private String cur_id;


    public FriendsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_friends, container, false);
        friendList = v.findViewById(R.id.friendsFragmentList);
        mAuth = FirebaseAuth.getInstance();
        cur_id = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(cur_id);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        friendList.setHasFixedSize(true);
        friendList.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        startListening();

    }

    public void startListening(){
        FirebaseRecyclerOptions<MyFriendsHelper> options = new FirebaseRecyclerOptions.Builder<MyFriendsHelper>().
                setQuery(databaseReference, MyFriendsHelper.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<MyFriendsHelper, MyFriendsHolder>(options) {

            @Override
            public MyFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout, parent, false);
                return new MyFriendsHolder(view);
            }

            @Override
            protected void onBindViewHolder(final MyFriendsHolder holder, int position, MyFriendsHelper model) {
                final String other_user_id = getRef(position).getKey();
                userReference.child(other_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();

                        holder.setNameHelper(name);
                        holder.setStatusHelper(status);
                        holder.setImageHelper(image);

                        holder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick (View v){
                                Intent intent = new Intent(getActivity(), OtherUserProfileActivity.class);
                                intent.putExtra("other_user_id", other_user_id);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        friendList.setAdapter(adapter);
        adapter.startListening();
    }

}
