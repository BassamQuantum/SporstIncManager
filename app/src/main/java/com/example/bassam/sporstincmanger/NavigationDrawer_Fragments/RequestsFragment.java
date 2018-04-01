package com.example.bassam.sporstincmanger.NavigationDrawer_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Activities.RequestDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.RequestsReceivedAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.requestsEntity;
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
 * Created by Bassam on 2/7/2018.
 */

public class RequestsFragment extends Fragment {
    private static final String TAG = RequestsFragment.class.getSimpleName();

    GlobalVars globalVars;

    private myCustomListView customListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    private RequestsReceivedAdapter adapter;
    private List<requestsEntity> requestsList;

    private int REQUEST_UPDATE = 7;
    private int request_position = -1;

    private boolean connectionStatus;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests_received, container, false);
        setHasOptionsMenu(true);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializeRequests(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment, R.string.no_Requests);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeRequests(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if(requestsList.size() >= limitValue)
                    listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);

        requestsList = new ArrayList<>();
        adapter = new RequestsReceivedAdapter(getContext(), R.layout.list_item_request_received , requestsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= requestsList.size())
                    return;
                request_position = i ;
                Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
                intent.putExtra("MyRequest", requestsList.get(i));
                intent.putExtra("requestType",1);
                startActivityForResult(intent , REQUEST_UPDATE);
            }
        });

        if (savedInstanceState == null)
            initializeRequests(false);
        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("RequestsList", (Serializable) requestsList);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<requestsEntity> list1 = (ArrayList<requestsEntity>) savedInstanceState.getSerializable("RequestsList");
        requestsList.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(requestsList.size());
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            int new_status = data.getIntExtra("request_status", -1);
            if (new_status != -1) {
                requestsList.get(request_position).setState(new_status);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void listLoadMore() {
        customListView.loadMore();
        currentStart = requestsList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeRequests(true);
            }
        }, 1500);

    }

    private void initializeRequests(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()) {
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectMangerData);
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
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

    private void fillAdapter(JSONArray response , boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            requestsList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    requestsList.add(new requestsEntity(response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            customListView.timeOut();
            return;
        }
        customListView.notifyChange(requestsList.size());
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