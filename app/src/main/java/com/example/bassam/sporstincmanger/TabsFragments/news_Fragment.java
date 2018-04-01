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

import com.example.bassam.sporstincmanger.Activities.NewsDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.NewsAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Entities.NewsEntity;
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

public class news_Fragment extends Fragment {

    NewsAdapter adapter;
    List<NewsEntity> NewsList;

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    private boolean connectionStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news,container,false);
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
                initializeNews(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_news);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeNews(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);
        NewsList = new ArrayList<>();
        adapter = new NewsAdapter(getContext(),R.layout.list_item_news,NewsList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= NewsList.size())
                    return;
                Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
                intent.putExtra("MyNews",NewsList.get(i));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null)
            initializeNews(false);
        else
            fillBySavedState(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("NewsList", (Serializable) NewsList);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<NewsEntity> list1 = (ArrayList<NewsEntity>) savedInstanceState.getSerializable("NewsList");
        NewsList.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(NewsList.size());
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    private void listLoadMore() {
        customListView.loadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeNews(true);
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

    private void initializeNews(final boolean loadMore) {
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
            params.put("table", "news");
            params.put("limit",limit_info.toString());

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

    private void fillAdapter(JSONArray response,boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            NewsList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    NewsList.add(new NewsEntity( response.getJSONObject(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            customListView.timeOut();
            return;
        }
        customListView.notifyChange(NewsList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }
}
