package com.example.csnfh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.csnfh.fragment.RecentEventFragment;
import com.example.csnfh.fragment.SquareFragment;

public class FarmEventPagerAdapter extends FragmentPagerAdapter {

    String[] tab_title;

    public FarmEventPagerAdapter(FragmentManager manager) {
        super(manager);
        tab_title = new String[]{"全部农场","近期活动"};
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SquareFragment();
                break;
            case 1:
                fragment = new RecentEventFragment();
                break;
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return tab_title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }

}
