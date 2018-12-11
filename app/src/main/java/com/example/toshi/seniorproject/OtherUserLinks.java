package com.example.toshi.seniorproject;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class OtherUserLinks extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private SIngleSectionPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTablayout;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_links);
        mAuth = FirebaseAuth.getInstance();

        mViewPager = findViewById(R.id.tabPager);
        mSectionsPagerAdapter = new SIngleSectionPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTablayout = findViewById(R.id.main_tabs);
        mTablayout.setupWithViewPager(mViewPager);
    }
}
