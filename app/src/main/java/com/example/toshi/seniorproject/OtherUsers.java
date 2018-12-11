package com.example.toshi.seniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OtherUsers extends AppCompatActivity {

    private Toolbar doolbar;
    private RecyclerView usersList;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser currentUser;
    private EditText searchField;
    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = findViewById(R.id.users_list);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));

        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchField.getText().toString();
                startListening(search);
            }
        });

    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void startListening(String search){

        Query searchQuery = firebaseDatabase.orderByChild("name").startAt(search).endAt(search + "\uf8ff");

        if(search.equals(null)) {
            searchQuery = firebaseDatabase;
        }

        FirebaseRecyclerOptions<OtherUsersHelper> options = new FirebaseRecyclerOptions.Builder<OtherUsersHelper>().setQuery(searchQuery, OtherUsersHelper.class).build();
        final FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<OtherUsersHelper, UserHolder>(options) {

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout, parent, false);
                return new UserHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserHolder holder, int position, OtherUsersHelper model) {
                holder.setNameHelper(model.getName());
                holder.setStatusHelper(model.getStatus());
                holder.setImageHelper(model.getImage());
                final String other_user_id = getRef(position).getKey();
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v){
                        Intent intent = new Intent(OtherUsers.this, OtherUserProfileActivity.class);
                        intent.putExtra("other_user_id", other_user_id);
                        startActivity(intent);
                    }
                });
            }
        };
        usersList.setAdapter(adapter);
        adapter.startListening();
    }
}
