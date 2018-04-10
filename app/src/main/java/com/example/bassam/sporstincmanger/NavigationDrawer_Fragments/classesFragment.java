package com.example.bassam.sporstincmanger.NavigationDrawer_Fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bassam.sporstincmanger.Adapters.SectionsPagerAdapter;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.TabsFragments.calender_Fragment;
import com.example.bassam.sporstincmanger.TabsFragments.classesList_Fragment;

/**
 * Created by Bassam on 12/21/2017.
 */

public class classesFragment extends Fragment {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_classes,container,false);
        setHasOptionsMenu(false);

        mViewPager = (ViewPager) root.findViewById(R.id.classescontainer);

        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.classestabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new classesList_Fragment(),getString(R.string.courses));
        mSectionsPagerAdapter.addFragment(new calender_Fragment(),getString(R.string.calendarTab));

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}
