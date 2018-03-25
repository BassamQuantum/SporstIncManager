package com.example.bassam.sporstincmanger.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Aaa_data.Functions;
import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.item1_courses_details;
import com.example.bassam.sporstincmanger.Entities.item2_courses_details;
import com.example.bassam.sporstincmanger.Entities.item_name_id;
import com.example.bassam.sporstincmanger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Bassam on 25/3/2018.
 */

public class ListViewExpandable_Adapter_CoursesDetails extends BaseExpandableListAdapter {

    private GlobalVars globalVars;
    private Functions functions;
    public Context context;
    public List<item1_courses_details> header_list;
    public HashMap<Integer, item2_courses_details> child_hashmap;

    private CourseEntity courseEntity;
    private ArrayList<item_name_id> trainee_names;

    PopupWindow popup_window;
    private LinearLayout ll;

    public ListViewExpandable_Adapter_CoursesDetails(Context context, List<item1_courses_details> listDataHeader,
                                                     HashMap<Integer, item2_courses_details> listChildData, CourseEntity courseEntity) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
        this.courseEntity = courseEntity;
        this.globalVars = (GlobalVars) context.getApplicationContext();
        this.functions = new Functions(context);
        trainee_names = new ArrayList<>();
        checkParent();
    }

    public void setLl(LinearLayout ll) {
        this.ll = ll;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return 1;}

    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    @Override
    public Object getGroup(int groupPosition) {
        return this.header_list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this.child_hashmap.get(this.header_list.get(groupPosition).getClass_id());
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        item1_courses_details  header = (item1_courses_details) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_courses_details, null);
        }

        TextView class_name_textview =  convertView.findViewById(R.id.classNameTextView_item1coursesdetails);
        TextView start_date_textview =  convertView.findViewById(R.id.dateTextView_item1coursesdetails);

        class_name_textview.setText(header.getClass_name());
        start_date_textview.setText(header.getStart_date());


        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        item2_courses_details child = (item2_courses_details) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_courses_details, null);
        }

        TextView coach_name_textview = convertView.findViewById(R.id.coachNameTextView_item2coursedetails);
        TextView days_textview = convertView.findViewById(R.id.daysTextView_item2coursedetails);
        TextView times_textview = convertView.findViewById(R.id.timesTextView_item2coursedetails);

        coach_name_textview.setText(child.getCoach_name());
        StringBuilder days = new StringBuilder();
        StringBuilder times = new StringBuilder();

        for (int i=0; i<child.getDay().length; i++) {
            days.append(child.getDay()[i]).append("\n");
            times.append(child.getTime()[i]).append("\n");
        }

        days_textview.setText(days.toString());
        times_textview.setText(times.toString());
        return convertView;
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @SuppressLint("StaticFieldLeak")
    private void checkParent(){

        try {
            JSONObject where_info = new JSONObject();
            where_info.put("parent_id", globalVars.getId());
            HttpCall httpCall = functions.searchDB("users", where_info);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(response != null){
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject result = response.getJSONObject(i);
                                int id = result.getInt("id");
                                String name = result.getString("name");
                                trainee_names.add(new item_name_id(id, name));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } else {
                        trainee_names.clear();
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

