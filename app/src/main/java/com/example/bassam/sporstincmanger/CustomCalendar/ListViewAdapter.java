package com.example.bassam.sporstincmanger.CustomCalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.MyItemClickListener;
import com.example.bassam.sporstincmanger.R;

import java.util.List;

/**
 * Created by Bassam on 12/31/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context ;
    List<classesEntity> myclasses;
    MyItemClickListener clickListener;

    public ListViewAdapter(@NonNull Context context, int resource, List<classesEntity> classeslist) {
        this.context = context;
        this.myclasses = classeslist;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.classes_list_items, parent,false);

        ViewHolder mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        classesEntity myclass = getItem(position);

        String classStatus = myclass.getStatus();
        if (classStatus.equals("Running"))
            viewHolder.status.setTextColor(Color.parseColor("#22a630"));
        else if (classStatus.equals("Canceled"))
            viewHolder.status.setTextColor(Color.parseColor("#df1b1c"));
        else if (classStatus.equals("Postponed"))
            viewHolder.status.setTextColor(Color.parseColor("#f98a03"));
        else if (classStatus.equals("Finished"))
            viewHolder.status.setTextColor(Color.parseColor("#ed4e4d4d"));
        else
            viewHolder.status.setTextColor(Color.parseColor("#2a388f"));

        viewHolder.status.setText(classStatus);
        viewHolder.Title.setText(myclass.getCourseName()+" "+myclass.getClassName());
        viewHolder.Time.setText(myclass.getStartTime()+" to "+myclass.getEndTime());
    }
    public classesEntity getItem(int position) {
        return myclasses.get(position);
    }

    @Override
    public int getItemCount() {
        return myclasses.size();
    }

    public void setClickListener(MyItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView Title ,Time ,status;

        public ViewHolder(View view) {
            super(view);
            Title = view.findViewById(R.id.class_name);
            Time = view.findViewById(R.id.class_start_end);
            status = view.findViewById(R.id.class_status);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}