package com.example.bassam.sporstincmanger.Aaa_data;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Activities.MainActivity;
import com.example.bassam.sporstincmanger.Activities.ProfileActivity;
import com.example.bassam.sporstincmanger.R;

/**
 * Created by Bassam on 19/3/2018.
 */

public class BottomNavigationAction implements View.OnClickListener {
    Context context;
    View view;
    TextView home , courses , report, profile;

    public BottomNavigationAction(Context context, View view) {
        this.context = context;
        this.view = view;
        home = view.findViewById(R.id.homeNavigation);
        courses = view.findViewById(R.id.coursesNavigation);
        report = view.findViewById(R.id.emailTextView_contactus);
        profile = view.findViewById(R.id.direcctionTextView_contactus);
    }

    public void createClickListener(){
        home.setOnClickListener(this);
        courses.setOnClickListener(this);
        report.setOnClickListener(this);
        profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, MainActivity.class);
        int navigationPos = 0;
        switch (view.getId()){
            case R.id.homeNavigation:
                navigationPos = 0;
                break;
            case R.id.coursesNavigation:
                navigationPos = 1;
                break;
            case R.id.emailTextView_contactus:
                navigationPos = 4;
                break;
            case R.id.direcctionTextView_contactus:
                intent = new Intent(context, ProfileActivity.class);
                break;
        }
        intent.putExtra("HomePosition",navigationPos);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
