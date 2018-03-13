package com.example.bassam.sporstincmanger.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bassam on 12/10/2017.
 */

 public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mfragments = new ArrayList<>();
    private final List<String> mFragemetsTitles = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mfragments.add(fragment);
        mFragemetsTitles.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragemetsTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mfragments.get(position);
    }

    @Override
    public int getCount() {
        return mfragments.size();
    }
}
