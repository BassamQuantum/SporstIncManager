package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.R;

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.report_list_items, null);
        }
        CourseEntity course = getItem(position);

        TextView CourseName = view.findViewById(R.id.reportName);

        TextView PaidPercentage = view.findViewById(R.id.reportPaied);


        CourseName.setText(course.getCourseName());
        PaidPercentage.setText(course.getPaidPrecentage());

        return  view;
    }
}
