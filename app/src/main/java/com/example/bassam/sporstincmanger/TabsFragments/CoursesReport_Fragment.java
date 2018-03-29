package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.example.bassam.sporstincmanger.Activities.GroupDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.MyExpandableListAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomExpandableListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomExpandableListViewListener;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
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

/**
 * Created by Bassam on 12/10/2017.
 */

public class CoursesReport_Fragment extends Fragment {

    myCustomExpandableListView customExpandableListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ExpandableListView expandableListView;

    MyExpandableListAdapter adapter;
    List<CourseEntity> listDataHeader;
    HashMap<Integer,List<GroupEntity> > listHashMap;

    int limitValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_courses,container,false);
        setHasOptionsMenu(false);

        limitValue = getResources().getInteger(R.integer.selectLimit);

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        customExpandableListView = root.findViewById(R.id.customExpandableListView);
        customExpandableListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_Report);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listDataHeader.clear();
                listHashMap.clear();
                initilizeData();
            }
        });
        customExpandableListView.setOnRetryClick(new myCustomExpandableListView.OnRetryClick() {
            @Override
            public void onRetry() {
                listDataHeader.clear();
                listHashMap.clear();
                initilizeData();
            }
        });

        expandableListView = customExpandableListView.getExpandableListView();
        myCustomExpandableListViewListener listener = new myCustomExpandableListViewListener(expandableListView, mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (listDataHeader.size() >= limitValue)
                    listLoadMore();
            }
        };
        expandableListView.setOnScrollListener(listener);

        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        adapter = new MyExpandableListAdapter(getContext(),listDataHeader,listHashMap);

        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int j, long l) {
                GroupEntity myGroup = (GroupEntity) adapter.getChild(i,j);
                Intent intent = new Intent(getContext(), GroupDetailsActivity.class);
                intent.putExtra("myGroup",myGroup);
                startActivity(intent);
                return false;
            }
        });

        if (savedInstanceState == null)
            initilizeData();

        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", expandableListView.onSaveInstanceState());
        outState.putSerializable("CoursesList", (Serializable) listDataHeader);
        outState.putSerializable("HashMap",listHashMap);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<CourseEntity> list1 = (ArrayList<CourseEntity>) savedInstanceState.getSerializable("CoursesList");
        HashMap<Integer,List<GroupEntity> > myHashMap = (HashMap<Integer, List<GroupEntity>>) savedInstanceState.getSerializable("HashMap");
        listDataHeader.addAll(list1);
        listHashMap.putAll(myHashMap);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customExpandableListView.notifyChange(listDataHeader.size());
        adapter.notifyDataSetChanged();
        expandableListView.onRestoreInstanceState(mListInstanceState);
    }

    private void listLoadMore() {
        customExpandableListView.loadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeData();
            }
        }, 1500);
    }


    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }


    private void initilizeData() {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customExpandableListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.attendanceReport);
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", listDataHeader.size());
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("limit",limit_info.toString());

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
        mSwipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            List<CourseEntity> coursesList = fillReportData(response);
            for (CourseEntity entity : coursesList){
                listDataHeader.add(entity);
                listHashMap.put(entity.getId(),entity.getGroups());
            }

        }
        adapter.notifyDataSetChanged();
        customExpandableListView.notifyChange(listDataHeader.size());
    }

    private List<CourseEntity> fillReportData(JSONArray response){
        List<CourseEntity> courseEntityList = new ArrayList<>();
        List<GroupEntity> groupEntityList = new ArrayList<>();
        try {
            Log.d("ExpandableList ","Length"+response.length());
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                int Course_ID = 0;
                Course_ID = jsonObject.getInt("course_id");

                CourseEntity courseEntity = new CourseEntity();
                courseEntity.setCourseName(jsonObject.getString("course_name"));
                courseEntity.setId(jsonObject.getInt("course_id"));
                courseEntity.setGroups(new ArrayList<GroupEntity>());
                GroupEntity groupEntity = new GroupEntity(1,jsonObject);

                if(!courseEntityList.contains(courseEntity))
                    courseEntityList.add(courseEntity);

                if(!groupEntityList.contains(groupEntity)) {
                    groupEntityList.add(groupEntity);
                    int index = findCourse(courseEntityList , Course_ID);
                    if (index != -1)
                        courseEntityList.get(index).getGroups().add(groupEntity);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courseEntityList;
    }

    private int findCourse(List<CourseEntity> courseEntityList, int course_id) {
        for (int i=0;i<courseEntityList.size();i++){
            if (courseEntityList.get(i).getId() == course_id)
                return i;
        }
        return -1;
    }

}
