package com.example.bassam.sporstincmanger.Activities;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Aaa_data.BottomNavigationAction;
import com.example.bassam.sporstincmanger.Aaa_data.Functions;
import com.example.bassam.sporstincmanger.Adapters.ListViewExpandable_Adapter_CoursesDetails;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.item1_courses_details;
import com.example.bassam.sporstincmanger.Entities.item2_courses_details;
import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CourseDetailsActivity extends AppCompatActivity {


    TextView SessionsNum ,description, durationOfSession, noClsses ;

    ImageView levelImage;

    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_CoursesDetails adapter_coursesDetails;

    ArrayList<item1_courses_details> header_list;
    HashMap < Integer, item2_courses_details> child_list;

    Functions functions;

    CustomLoadingView loadingView;
    LinearLayout navigationBlow;

    String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Level Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationBlow = findViewById(R.id.belowlayout_navigation);
        BottomNavigationAction bottomNavigationAction = new BottomNavigationAction(getApplicationContext() ,navigationBlow);
        bottomNavigationAction.createClickListener();

        //CourseName = findViewById(R.id.course_details_name);
        functions = new Functions(CourseDetailsActivity.this);

        loadingView = findViewById(R.id.LoadingView);
        levelImage = findViewById(R.id.Course_icon);
        SessionsNum = findViewById(R.id.course_details_no_classes);
        expandableListView = findViewById(R.id.course_details_expandableListView);
        description = findViewById(R.id.course_details_description);
        durationOfSession = findViewById(R.id.course_details_session_duration);
        noClsses = findViewById(R.id.noClassesTextView_coursedetails);

        header_list = new ArrayList<>();
        child_list = new HashMap<>();

        //expandableListView.setAdapter(adapter_coursesDetails);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        final CourseEntity myCourse = (CourseEntity) getIntent().getSerializableExtra("MyCourse");

        String ImageUrl = myCourse.getImageUrl();
        //fillImage(name,icon);
        if(!ImageUrl.equals("")) {
            Picasso.with(CourseDetailsActivity.this).load(Constants.others_host + ImageUrl).into(levelImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ","ERROR In Loading");
                }
            });
        }
        SessionsNum.setText(myCourse.getClasses_Num());
        durationOfSession.setText(myCourse.getSession_duration());
        description.setText(myCourse.getDescription());


        // progressDialog.show();
        adapter_coursesDetails = new ListViewExpandable_Adapter_CoursesDetails(CourseDetailsActivity.this, header_list, child_list, myCourse);
        fill_list_view(myCourse);

        LinearLayout ll = findViewById(R.id.ll_coursesdetails);
        adapter_coursesDetails.setLl(ll);

        /*if (savedInstanceState!=null)
            loadingTime = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myCourse != null){
                    //fillView(myCourse);
                    progressDialog.show();
                    fill_list_view(myCourse);
                }
                else
                    loadingView.fails(); }
        }, loadingTime);*/

    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ListViewExpandable_Adapter_CoursesDetails listAdapter = (ListViewExpandable_Adapter_CoursesDetails) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private void fillView(CourseEntity courseEntity) {
        // CourseName.setText(courseEntity.getCourseName());
        getSupportActionBar().setTitle(courseEntity.getCourseName());
        String ImageUrl = courseEntity.getImageUrl();
        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(Constants.others_host + ImageUrl).into(levelImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ", "ERROR In Loading");
                }
            });
        }
        SessionsNum.setText(courseEntity.getClasses_Num());
        //CoursePrice.setText(courseEntity.getPrice());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String date = df.format(courseEntity.getStartDate());
        //startDate.setText(date);
        date = df.format(courseEntity.getEndDate());
        //endDate.setText(date);
        //CourseLevel.setText(courseEntity.getLevel());
        description.setText(courseEntity.getDescription());
        loadingView.success();
    }

    public void fill_list_view(CourseEntity myCourse) {
        header_list.clear();
        child_list.clear();
        JSONObject where_info = new JSONObject();
        try {
            where_info.put("groups.course_id", myCourse.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String OnCondition = "groups.coach_id = users.id";
        String select = "groups.id, groups.name, users.name, groups.start_date," +
                "groups.days, groups.daystime";

        HttpCall httpCall = functions.joinDB("groups", "users", where_info, OnCondition, select);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){

                    try {
                        for (int i=0; i<response.length(); i++){

                            JSONObject result = response.getJSONObject(i);
                            int class_id = result.getInt("groups_id");
                            String class_name = result.getString("name");
                            String coach_name = result.getString("name");
                            String start_date = result.getString("group_sdate");
                            String[] days = get_days(result.getString("days"));
                            String[] daystime = result.getString("daystime").split("@");
                            header_list.add(new item1_courses_details(class_name, start_date,class_id));
                            child_list.put(class_id, new item2_courses_details(coach_name, days, daystime));
                        }
                        noClsses.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.VISIBLE);
                        adapter_coursesDetails.notifyDataSetChanged();
                        expandableListView.setAdapter(adapter_coursesDetails);
                        // progressDialog.dismiss();

                    } catch (JSONException e) {
                        // progressDialog.dismiss();
                        expandableListView.setVisibility(View.GONE);
                        e.printStackTrace();

                    }

                } else {
                    //progressDialog.dismiss();
                    noClsses.setVisibility(View.VISIBLE);
                    expandableListView.setVisibility(View.GONE);
                    //checkMail();
                    //verfication();
                }
                loadingView.success();

            }
        }.execute(httpCall);


    }

    private String[] get_days(String dayss) {
        String[] dayss_split;

        if(dayss != null) {
            dayss_split = dayss.split("@");
            for (int i=0; i<dayss_split.length; i++) {
                dayss_split[i] = weekDays[Integer.valueOf(dayss_split[i])];
            }
        } else  {
            dayss_split = new String[]{" "};
        }

        return dayss_split;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void checkBooking(){

    }
}
