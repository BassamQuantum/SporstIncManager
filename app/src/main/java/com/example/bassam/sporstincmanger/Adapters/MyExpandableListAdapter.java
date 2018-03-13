package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.GroupEntity;
import com.example.bassam.sporstincmanger.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    List<CourseEntity> listDataHeader;
    HashMap<Integer,List<GroupEntity> > listHashMap;

    public MyExpandableListAdapter(Context mContext, List<CourseEntity> listDataHeader, HashMap<Integer, List<GroupEntity>> listHashMap) {
        this.mContext = mContext;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i).getId()).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i).getCourseName();
    }

    @Override
    public Object getChild(int i, int j) {
        return listHashMap.get(listDataHeader.get(i).getId()).get(j);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String header = (String) getGroup(i);

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.report_courses_group_list,null);
        }

        TextView ListHeader = view.findViewById(R.id.lbListHeader);
        ListHeader.setTypeface(null, Typeface.BOLD);
        ListHeader.setText(header);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final GroupEntity mygroup = (GroupEntity) getChild(i,i1);

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.report_course_item_list,null);
        }

        TextView GroupName = view.findViewById(R.id.reportItemGroupName);
        TextView Attendance = view.findViewById(R.id.reportItemAttendance);

        GroupName.setText(mygroup.getName());
        Attendance.setText(mygroup.getAttendacePrecentage());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
