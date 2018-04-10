package com.example.bassam.sporstincmanger.NavigationDrawer_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bassam.sporstincmanger.Adapters.SectionsPagerAdapter;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.TabsFragments.brief_Fragment;
import com.example.bassam.sporstincmanger.TabsFragments.courses_Fragment;

/**
 * Created by Bassam on 12/10/2017.
 */

public class homeFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main,container,false);
        setHasOptionsMenu(true);

        mViewPager = (ViewPager) root.findViewById(R.id.homecontainer);

        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.hometabs);
        tabLayout.setupWithViewPager(mViewPager);
        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new brief_Fragment(),getString(R.string.homeLabel));
        mSectionsPagerAdapter.addFragment(new courses_Fragment(),getString(R.string.coursesLabel));

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

}
