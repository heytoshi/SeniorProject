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
import android.widget.NumberPicker;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView friendList;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private DatabaseReference myRequestDatabase;
    private FirebaseAuth mAuth;
    private View v;
    private String cur_id;

    public RequestsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_requests, container, false);
        friendList = v.findViewById(R.id.requestsFragmentList);
        mAuth = FirebaseAuth.getInstance();
        cur_id = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        myRequestDatabase = FirebaseDatabase.getInstance().getReference().child("My Requests").child(cur_id);
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
                setQuery(myRequestDatabase, MyFriendsHelper.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<MyFriendsHelper, MyFriendsHolder>(options) {

            @Override
            public MyFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_layout, parent, false);

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
                        holder.setNameHelper(name);
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
