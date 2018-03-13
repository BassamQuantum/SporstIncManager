package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.AttendanceEntity;
import com.example.bassam.sporstincmanger.R;

import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class ReportClassesAdapter extends ArrayAdapter<AttendanceEntity> {
    Context context ;
    List<AttendanceEntity> myclasses;


    public ReportClassesAdapter(@NonNull Context context, int resource, List<AttendanceEntity> myclasses) {
        super(context, resource);
        this.context = context;
        this.myclasses = myclasses;
    }

    @Override
    public int getCount() {
        return myclasses.size();
    }

    @Nullable
    @Override
    public AttendanceEntity getItem(int position) {
        return myclasses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.report_class_list_items, null);
        }
        AttendanceEntity attendanceEntity = getItem(position);

        TextView ClassName = view.findViewById(R.id.reportClassName);
        TextView Date = view.findViewById(R.id.reportClassDate);
        TextView Attendance = view.findViewById(R.id.reportClassAttend);

        Attendance.setText(String.format("%.0f",attendanceEntity.getAtten_precent())+" %");
        ClassName.setText(attendanceEntity.getClassName());
        Date.setText(attendanceEntity.getDate());

        return  view;
    }
}
