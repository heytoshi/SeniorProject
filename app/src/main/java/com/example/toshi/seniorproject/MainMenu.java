package com.example.toshi.seniorproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTablayout;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Senior Project");
        mViewPager = findViewById(R.id.tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTablayout = findViewById(R.id.main_tabs);
        mTablayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.mainmenuLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId() == R.id.mainmenuProfile) {
            Intent intent2 = new Intent(this, ProfileSettings.class);
            startActivity(intent2);
        }
        else if(item.getItemId() == R.id.toChat) {
            Intent intent3 = new Intent(this, ProfileActivity.class);
            startActivity(intent3);
        }
        else if(item.getItemId() == R.id.searchUsers) {
            Intent intent3 = new Intent(this, OtherUsers.class);
            startActivity(intent3);
        }
    return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
