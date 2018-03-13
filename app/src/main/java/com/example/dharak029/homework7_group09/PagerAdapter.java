package com.example.dharak029.homework7_group09;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by dharak029 on 11/16/2017.
 */


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    FragmentManager fm;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FriendsFragment tab1 = new FriendsFragment();
                return tab1;
            case 1:
                AddNewFriendFragment tab2 = new AddNewFriendFragment();
                return tab2;
            case 2:
                RequestPendingFragment tab3 = new RequestPendingFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
