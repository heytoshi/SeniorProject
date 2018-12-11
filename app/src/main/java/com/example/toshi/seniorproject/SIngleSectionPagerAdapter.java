package com.example.toshi.seniorproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SIngleSectionPagerAdapter extends FragmentPagerAdapter {

    public SIngleSectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OtherLinkFragment otherLinkFragment = new OtherLinkFragment();
                return otherLinkFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "USER LINKS";
            default:
                return null;
        }
    }
}
