package com.example.toshi.seniorproject;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private RecyclerView linkList;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private View v;
    private String cur_id;
    private TextView getName;
    public static final String NICKNAME = "usernickname";
    public static final String LINKNAME = "name";
    public static final String LINK = "link";

    public ListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_list, container, false);
        linkList = v.findViewById(R.id.linksFragmentList);
        getName = v.findViewById(R.id.getNameListFragment);
        mAuth = FirebaseAuth.getInstance();
        cur_id = mAuth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(cur_id).child("Links");
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue().toString();
                getName.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        final String link = dataSnapshot.child("link").getValue().toString();
                        final String name = dataSnapshot.child("name").getValue().toString();
                        holder.setNameHelper(name);
                        holder.setStatusHelper(link);
                        holder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick (View v){
                                Intent intent = new Intent(getActivity(), MainMediaPlayer.class);
                                intent.putExtra(NICKNAME, getName.getText().toString());
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
