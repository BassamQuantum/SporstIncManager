package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Activities.CourseDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.CoursesAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
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

public class courses_Fragment extends Fragment {

    private CoursesAdapter adapter;
    private List<CourseEntity> courseList;

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    private boolean connectionStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_courses,container,false);
        setHasOptionsMenu(false);
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initilizeCourses(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_Courses);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initilizeCourses(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (courseList.size() >= limitValue)
                    ListLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);
        courseList=new ArrayList<>();

        adapter = new CoursesAdapter(getContext(),R.layout.course_list_item,courseList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= courseList.size())
                    return;
                Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                intent.putExtra("MyCourse",courseList.get(i));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null)
            initilizeCourses(false);
        else
            fillBySavedState(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("CoursesList", (Serializable) courseList);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<CourseEntity> list1 = (ArrayList<CourseEntity>) savedInstanceState.getSerializable("CoursesList");
        courseList.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(courseList.size());
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    private void ListLoadMore() {
        customListView.loadMore();
        currentStart = courseList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeCourses(true);
            }
        }, 1500);
    }

    private void initilizeCourses(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("ordered","true");
            params.put("table", "courses");
            params.put("limit", limit_info.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    connectionStatus = connectionTimeOut;
                    fillAdapter(response, loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response ,boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            courseList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    courseList.add(new CourseEntity( response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            customListView.timeOut();
            return;
        }
        customListView.notifyChange(courseList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }
    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }
}
