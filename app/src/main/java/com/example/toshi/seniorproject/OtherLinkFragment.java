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
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherLinkFragment extends Fragment {

    private RecyclerView linkList;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private View v;
    private String cur_id;
    private TextView getName;
    public static final String N = "usernickname";
    public static final String LINK = "link";
    String other_user_id2;

    public OtherLinkFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_other_link, container, false);
        linkList = v.findViewById(R.id.otherlinksFragmentList);
        getName = v.findViewById(R.id.textView);

        other_user_id2 = getActivity().getIntent().getStringExtra(OtherUserProfileActivity.otherUserId);

        mAuth = FirebaseAuth.getInstance();
        cur_id = mAuth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(other_user_id2).child("Links");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(cur_id);
        linkList.setHasFixedSize(true);
        linkList.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        startListening();
    }

    public void startListening(){

        FirebaseRecyclerOptions<MyFriendsHelper> options = new FirebaseRecyclerOptions.Builder<MyFriendsHelper>().
                setQuery(userReference, MyFriendsHelper.class).build();


        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<MyFriendsHelper, MyFriendsHolder>(options) {
            @Override
            public MyFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.links_layout, parent, false);

                return new MyFriendsHolder(view);
            }


            @Override
            protected void onBindViewHolder(final MyFriendsHolder holder, int position, MyFriendsHelper model) {
                final String other_user_id = getRef(position).getKey();
                userReference.child(other_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String link = dataSnapshot.child("link").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        holder.setNameHelper(name);
                        holder.setStatusHelper(link);
                        holder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick (View v){
                                Intent intent = new Intent(getActivity(), MainMediaPlayer.class);
                                intent.putExtra(N, other_user_id2);
                                intent.putExtra(LINK, link);
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
        linkList.setAdapter(adapter);
        adapter.startListening();
    }

}
