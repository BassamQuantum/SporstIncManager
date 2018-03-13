package com.example.bassam.sporstincmanger.NavigationDrawer_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bassam.sporstincmanger.Activities.NewRequestActivity;
import com.example.bassam.sporstincmanger.Adapters.SectionsPagerAdapter;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.TabsFragments.NotificationsReceivedFragment;
import com.example.bassam.sporstincmanger.TabsFragments.NotificationsSent_Fragment;

/**
 * Created by Bassam on 12/21/2017.
 */

public class NotificationsFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests,container,false);
        setHasOptionsMenu(true);

        mViewPager = (ViewPager) root.findViewById(R.id.requestscontainer);

        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.requeststabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton create = root.findViewById(R.id.fab);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewRequestActivity.class);
                intent.putExtra("CurrentItem",mViewPager.getCurrentItem());
                startActivity(intent);
            }
        });

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new NotificationsReceivedFragment(),"Received");
        mSectionsPagerAdapter.addFragment(new NotificationsSent_Fragment(),"Sent");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.request);
    }
}
