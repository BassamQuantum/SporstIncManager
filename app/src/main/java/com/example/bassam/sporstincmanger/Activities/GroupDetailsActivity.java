package com.example.bassam.sporstincmanger.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Adapters.ReportClassesAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.AttendanceEntity;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.GroupEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupDetailsActivity extends AppCompatActivity {

    ReportClassesAdapter adapter;
    TextView GroupName ,Attendance ,CoachName ,AdminName ,PoolName , Coach_Attend;

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listener;
    int limitValue;
    List<AttendanceEntity> entityList;
    GroupEntity MyGroup;
    CustomLoadingView loadingView;
    int loadingTime = 1200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        loadingView = findViewById(R.id.LoadingView);

        MyGroup = (GroupEntity) getIntent().getSerializableExtra("myGroup");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GroupName = findViewById(R.id.GroupReviewName);
        Attendance = findViewById(R.id.GroupReviewAttendance);
        CoachName = findViewById(R.id.GroupReviewCoach);
        Coach_Attend = findViewById(R.id.GroupReviewCoachAttend);
        AdminName = findViewById(R.id.GroupReviewAdmin);
        PoolName = findViewById(R.id.GroupReviewPool);

        limitValue = getResources().getInteger(R.integer.selectLimit);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                entityList.clear();
                initilizeData();
            }
        });
        customListView = findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_sessions);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                entityList.clear();
                initilizeData();
            }
        });
        listView = customListView.getListView();
        listener = new myCustomListViewListener(listView,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                listLoadMore();
            }
        };
        listView.setOnScrollListener(listener);

        entityList = new ArrayList<>();

        adapter = new ReportClassesAdapter(getApplicationContext(),R.layout.report_class_list_items,entityList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupDetailsActivity.this, ActivitySessionAttendance.class);
                intent.putExtra(getString(R.string.Key_Course_name),MyGroup.getCourseName());
                intent.putExtra(getString(R.string.Key_Group_name),MyGroup.getName());
                intent.putExtra(getString(R.string.Key_CoachID),MyGroup.getCoach_id());
                intent.putExtra(getString(R.string.Key_Pool_name),MyGroup.getPoolName());
                intent.putExtra(getString(R.string.Key_FinishedClass),entityList.get(position));
                startActivity(intent);

            }
        });

        if (savedInstanceState!= null) {
            List<AttendanceEntity> list = (List<AttendanceEntity>) savedInstanceState.getSerializable("entityList");
            entityList.addAll(list);
            fillView();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (MyGroup != null) {
                        initilizeData();
                    } else
                        loadingView.fails();
                }
            }, loadingTime);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("entityList", (Serializable) entityList);
    }

    private void listLoadMore() {
        customListView.loadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeData();
            }
        }, loadingTime);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }


    private void initilizeData() {
        if (!checkConnection()){
            loadingView.enableRetry();
            loadingView.fails();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.attendanceReportDetails);
            JSONObject where_info = new JSONObject();
            where_info.put("classes.group_id ",MyGroup.getGroup_id());
            HashMap<String, String> params = new HashMap<>();
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response) {
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    AttendanceEntity attendanceEntity = new AttendanceEntity(jsonObject , MyGroup.getTraineeNum());
                    entityList.add(attendanceEntity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fillView();
    }

    private void fillView(){
        mSwipeRefreshLayout.setRefreshing(false);
        getSupportActionBar().setTitle(MyGroup.getName());
        GroupName.setText(MyGroup.getName());
        Attendance.setText(MyGroup.getAttendacePrecentage());
        CoachName.setText(MyGroup.getCoachName());
        Coach_Attend.setText(MyGroup.getCoachAttend());
        AdminName.setText(MyGroup.getAdminName());
        PoolName.setText(MyGroup.getPoolName());
        loadingView.success();
        customListView.notifyChange(entityList.size());
        adapter.notifyDataSetChanged();
        listener.setLoading(false);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
