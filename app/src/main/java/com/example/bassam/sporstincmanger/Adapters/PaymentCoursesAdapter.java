package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class PaymentCoursesAdapter extends ArrayAdapter<CourseEntity> {
    Context context ;
    List<CourseEntity> mycourses;


    public PaymentCoursesAdapter(@NonNull Context context, int resource, List<CourseEntity> mycourses) {
        super(context, resource);
        this.context = context;
        this.mycourses = mycourses;
    }

    @Override
    public int getCount() {
        return mycourses.size();
    }

    @Nullable
    @Override
    public CourseEntity getItem(int position) {
        return mycourses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_items_report_level, null);
        }
        CourseEntity course = getItem(position);

        ImageView icon = view.findViewById(R.id.LevelIcon);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView GroupsNum = view.findViewById(R.id.classesNum);
        TextView ClassesNum = view.findViewById(R.id.SessionNum);
        TextView TraineeNum = view.findViewById(R.id.PaidNum);
        TextView PaidNum = view.findViewById(R.id.TraineeNum);

        String ImageUrl = course.getImageUrl();
        if(!ImageUrl.equals("")) {
            Picasso.with(context).load(Constants.others_host + ImageUrl).into(icon, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    Log.d("Image Loading ","ERROR In Loading");
                }
            });
        }

        GroupsNum.setText(course.getGroupsNum());
        ClassesNum.setText(course.getClasses_Num());
        TraineeNum.setText(course.getTrainees_count()+" Persons");
        PaidNum.setText(course.getPaid_count()+" Persons");

        return  view;
    }
}
