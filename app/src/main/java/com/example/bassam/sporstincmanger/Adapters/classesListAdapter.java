package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.R;

import java.util.List;

/**
 * Created by Bassam on 12/24/2017.
 */

public class classesListAdapter extends ArrayAdapter<classesEntity> {
    Context context ;
    List<classesEntity> myclasses;


    public classesListAdapter(@NonNull Context context, int resource,List<classesEntity> classeslist) {
        super(context, resource);
        this.context = context;
        this.myclasses = classeslist;
    }

    @Override
    public int getCount() {
        return myclasses.size();
    }

    @Nullable
    @Override
    public classesEntity getItem(int position) {
        return myclasses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.classes_list_items, null);
        }
        classesEntity myclass = getItem(position);

        TextView Title = view.findViewById(R.id.class_name);
        TextView Time = view.findViewById(R.id.class_start_end);
        TextView StartTime = view.findViewById(R.id.class_start_time);
        TextView status = view.findViewById(R.id.class_status);

        String classStatus = myclass.getStatus();
        if (classStatus.equals("Running"))
            status.setTextColor(Color.parseColor("#22a630"));
        else if (classStatus.equals("Canceled"))
            status.setTextColor(Color.parseColor("#df1b1c"));
        else if (classStatus.equals("Postponed"))
            status.setTextColor(Color.parseColor("#f98a03"));
        else if (classStatus.equals("Finished"))
            status.setTextColor(Color.parseColor("#ed4e4d4d"));
        else
            status.setTextColor(Color.parseColor("#2a388f"));

        Title.setText(myclass.getCourseName()+" "+myclass.getClassName());
        StartTime.setText(myclass.getStartTime());
        Time.setText(myclass.getStartTime()+" to "+myclass.getEndTime());
        status.setText(classStatus);

        return  view;
    }
}
