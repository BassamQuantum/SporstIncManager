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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Aaa_data.BottomNavigationAction;
import com.example.bassam.sporstincmanger.Adapters.PaymentGroupAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.GroupEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentDetailsActivity extends AppCompatActivity {

    TextView StartDate ,EndDate;
    LinearLayout navigationBlow;
    PaymentGroupAdapter adapter;
    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listener;
    List<GroupEntity> entityList;
    int limitValue , loadingTime = 1200;

    CourseEntity MyCourse;
    private boolean connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        MyCourse = (CourseEntity) getIntent().getSerializableExtra("MyCourse");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (MyCourse != null)
            toolbar.setTitle(MyCourse.getCourseName());
        else
            toolbar.setTitle("Payment Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*StartDate = findViewById(R.id.courseStartDate);
        EndDate = findViewById(R.id.courseEndDate);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String SDate = format.format(MyCourse.getStartDate());
        StartDate.setText(SDate);
        String EDate = format.format(MyCourse.getEndDate());
        EndDate.setText(EDate);*/

        limitValue = getResources().getInteger(R.integer.selectLimit);
        navigationBlow = findViewById(R.id.belowlayout_contactus);
        BottomNavigationAction bottomNavigationAction = new BottomNavigationAction(getApplicationContext() ,navigationBlow);
        bottomNavigationAction.createClickListener();
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                entityList.clear();
                initilizeData(MyCourse);
            }
        });
        customListView = findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_Report);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                entityList.clear();
                initilizeData(MyCourse);
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

        adapter = new PaymentGroupAdapter(getApplicationContext(),R.layout.list_items_report_class,entityList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PaymentDetailsActivity.this, PaymentTraineesActivity.class);
                intent.putExtra("LevelName",entityList.get(i).getCourseName());
                intent.putExtra("ClassName",entityList.get(i).getName());
                intent.putExtra("ClassID",entityList.get(i).getGroup_id());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String ClassDate = dateFormat.format(entityList.get(i).getGroup_sdate());
                intent.putExtra("ClassDate",ClassDate);
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);

        if (savedInstanceState == null)
            initilizeData(MyCourse);
        else {
            List<GroupEntity> list = (List<GroupEntity>) savedInstanceState.getSerializable("entityList");
            entityList.addAll(list);
            fillView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("LIST","Size on Save: "+entityList.size());
        outState.putSerializable("entityList", (Serializable) entityList);
    }

    private void listLoadMore() {
        customListView.loadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MyCourse != null)
                    initilizeData(MyCourse);
            }
        }, loadingTime);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getApplicationContext())) {
            return true;
        }
        return false;
    }

    private void initilizeData(CourseEntity MyCourse) {
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.paidReport);
            JSONObject where_info = new JSONObject();
            where_info.put("levels.course_id" ,MyCourse.getId() );

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", entityList.size());
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
           // params.put("limit",limit_info.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    connectionStatus = connectionTimeOut;
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
                    GroupEntity entity = new GroupEntity(2,jsonObject);
                    entityList.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            mSwipeRefreshLayout.setRefreshing(false);
            customListView.timeOut();
            return;
        }
        fillView();
    }

    private void fillView(){
        mSwipeRefreshLayout.setRefreshing(false);
        getSupportActionBar().setTitle(MyCourse.getCourseName());
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
