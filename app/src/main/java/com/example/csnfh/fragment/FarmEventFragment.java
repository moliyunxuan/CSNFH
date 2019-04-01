package com.example.csnfh.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csnfh.R;
import com.example.csnfh.adapter.FarmEventPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmEventFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FarmEventPagerAdapter viewpageradapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_farm_event, container, false);
        initView(v);
        return v;
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        viewpageradapter = new FarmEventPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewpageradapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
    }


}
