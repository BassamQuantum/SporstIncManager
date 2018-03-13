package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Activities.EventsActivity;
import com.example.bassam.sporstincmanger.Activities.EventsDetailsActivity;
import com.example.bassam.sporstincmanger.Activities.NewsActivity;
import com.example.bassam.sporstincmanger.Adapters.EventsRecyclerAdapter;
import com.example.bassam.sporstincmanger.Adapters.NewsRecyclerAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Entities.NewsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;
import com.squareup.picasso.Picasso;

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

public class brief_Fragment extends Fragment {
    private static final String TAG = brief_Fragment.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;

    TextView AboutAcademy;
    ImageView Logo;
    RecyclerView newsRecyclerView , eventsRecyclerView;
    NewsRecyclerAdapter newsAdapter ;
    EventsRecyclerAdapter eventsAdapter;
    LinearLayout newsLayout , eventsLayout;
    TextView newsMore , eventMore;
    ArrayList<NewsEntity> NewsList = new ArrayList<>();
    ArrayList<EventEntity> eventsList = new ArrayList<>();
    String logo ,brief;

    ProgressBar progressBar;
    LinearLayout retry;
    RelativeLayout loading;
    NestedScrollView scrollView;

    int limitValue , Counter = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(false);

        limitValue = getResources().getInteger(R.integer.sliderLimit);

        scrollView = root.findViewById(R.id.layoutScrollView);
        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 Counter = 0;
                 getContent();
             }
        });

        NewsList = new ArrayList<>();
        eventsList = new ArrayList<>();
        newsLayout = root.findViewById(R.id.homeNewsSection);
        eventsLayout = root.findViewById(R.id.homeNewsSection);
        newsRecyclerView = root.findViewById(R.id.NewsList);
        eventsRecyclerView = root.findViewById(R.id.EventsList);
        progressBar = root.findViewById(R.id.progress_bar);
        loading = root.findViewById(R.id.LoadingData);

        retry = root.findViewById(R.id.layout_retry);
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL , false);
        newsAdapter = new NewsRecyclerAdapter(getContext(),NewsList);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        newsRecyclerView.setAdapter(newsAdapter);

        LinearLayoutManager eventsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL , false);
        eventsAdapter = new EventsRecyclerAdapter(getContext(),eventsList);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        eventsRecyclerView.setAdapter(eventsAdapter);

        newsMore = root.findViewById(R.id.newsMore);
        eventMore = root.findViewById(R.id.eventsMore);

        newsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewsActivity.class));
            }
        });

        eventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EventsActivity.class));
            }
        });
        AboutAcademy = root.findViewById(R.id.AboutAcademy);
        Logo = root.findViewById(R.id.AcademyLogo);

        if (savedInstanceState!=null)
            fillBySavedState(savedInstanceState);

        else
            getContent();

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ScrollPosition", new int[]{scrollView.getScrollX() , scrollView.getScrollY()});
        outState.putSerializable("NewsList", NewsList);
        outState.putSerializable("EventsList", eventsList);
        outState.putString("About",brief);
        outState.putString("logo",logo);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        loading.setVisibility(View.GONE);
        ArrayList<NewsEntity> list1 = (ArrayList<NewsEntity>) savedInstanceState.getSerializable("NewsList");
        NewsList.addAll(list1);
        ArrayList<EventEntity> list2 = (ArrayList<EventEntity>) savedInstanceState.getSerializable("EventsList");
        eventsList.addAll(list2);
        brief = savedInstanceState.getString("About");
        logo = savedInstanceState.getString("logo");

        newsAdapter.notifyDataSetChanged();
        eventsAdapter.notifyDataSetChanged();
        AboutAcademy.setText(brief);
        if (!logo.equals("")) {
            Picasso.with(getContext()).load(logo).into(Logo);
        }

        int[] positions = savedInstanceState.getIntArray("ScrollPosition");
        if (positions != null)
            scrollView.scrollTo(positions[0],positions[1]);
    }

    private void getContent(){
        if (checkConnection())
            refreshContent();

        else {
            progressBar.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    private synchronized void countFinished(){
        Counter ++;
        Log.d(TAG," Count "+Counter);
        if (Counter >= 3){
            viewData();
        }
    }

    private void refreshContent(){
        initializeAbout();
        initializeNews();
        initilizeEvents();
    }

    private void initializeAbout() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","info_academy");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillView(response);
            }
        }.execute(httpCall);
    }

    private void fillView(JSONArray response) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            try {
                JSONObject object = response.getJSONObject(0);

                logo ="";// object.getString("logo");
                brief = object.getString("about");

                if (!logo.equals("")) {
                    Picasso.with(getContext()).load(logo).into(Logo);
                }

                AboutAcademy.setText(brief);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        countFinished();
    }

    private void initializeNews() {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            JSONObject limit = new JSONObject();
            limit.put("start", 0);
            limit.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("table", "news");
            params.put("limit",limit.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillNewsAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillNewsAdapter(JSONArray response) {
        NewsList.clear();
        newsAdapter.notifyDataSetChanged();
        if (response != null) {
            try {
                for (int i = 0; i < response.length()&& i<4; i++) {
                    NewsList.add(new NewsEntity( response.getJSONObject(i)));
                }
                newsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        countFinished();
    }

    private void initilizeEvents() {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            JSONObject limit = new JSONObject();
            limit.put("start", 0);
            limit.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("table", "events");
            params.put("limit", limit.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillEventAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillEventAdapter(JSONArray response) {
        eventsList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length() && i<4; i++) {
                    eventsList.add(new EventEntity(response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        eventsAdapter.notifyDataSetChanged();

        countFinished();
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }
    private  void viewData(){
        loading.setVisibility(View.GONE);
    }
}
