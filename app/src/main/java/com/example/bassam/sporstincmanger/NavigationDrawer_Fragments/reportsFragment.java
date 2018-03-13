package com.example.bassam.sporstincmanger.NavigationDrawer_Fragments;

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
import com.example.bassam.sporstincmanger.TabsFragments.CoursesReport_Fragment;
import com.example.bassam.sporstincmanger.TabsFragments.PaymentReport_Fragment;

/**
 * Created by Bassam on 12/21/2017.
 */

public class reportsFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports,container,false);
        setHasOptionsMenu(true);

        mViewPager = (ViewPager) root.findViewById(R.id.reportscontainer);

        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.reportstabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new CoursesReport_Fragment(),"Attendance");
        mSectionsPagerAdapter.addFragment(new PaymentReport_Fragment(),"Payment");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.report);
    }
}
