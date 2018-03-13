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
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bassam.sporstincmanger.Activities.PaymentDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.PaymentCoursesAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomExpandableListViewListener;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
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

public class PaymentReport_Fragment extends Fragment {

    PaymentCoursesAdapter adapter;
    List<CourseEntity> courseList;
    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listener;
    int limitValue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_payment,container,false);
        setHasOptionsMenu(false);
        limitValue = getResources().getInteger(R.integer.selectLimit);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                courseList.clear();
                initilizeData();
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_Report);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                courseList.clear();
                initilizeData();
            }
        });
        listView = customListView.getListView();
        listener = new myCustomListViewListener(listView,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (courseList.size() >= limitValue)
                    listLoadMore();
            }
        };
        listView.setOnScrollListener(listener);

        courseList = new ArrayList<>();
        adapter = new PaymentCoursesAdapter(getContext(),R.layout.report_list_items,courseList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=courseList.size())
                    return;
                Intent intent = new Intent(getContext(), PaymentDetailsActivity.class);
                intent.putExtra("MyCourse",courseList.get(i));
                startActivity(intent);
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


    private void listLoadMore() {
        customListView.loadMore();
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
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.paidReport);

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", courseList.size());
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            Log.d("LimitReport","Limit: "+limit_info.toString());
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
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    CourseEntity courseEntity = new CourseEntity();
                    courseEntity.initData(jsonObject);
                    courseList.add(courseEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customListView.notifyChange(courseList.size());
        adapter.notifyDataSetChanged();
        listener.setLoading(false);
    }

}
