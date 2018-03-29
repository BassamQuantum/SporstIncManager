package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Activities.NotificationDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.NotificationAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.NotificationEntity;
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

public class NotificationsReceivedFragment extends Fragment {

    private static String TAG = NotificationsReceivedFragment.class.getSimpleName();

    GlobalVars globalVars;

    private NotificationAdapter adapter ;
    private List<NotificationEntity> notificationList;
    myCustomListView customListView;
    myCustomListViewListener listViewListener;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;

    int limitValue,currentStart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications_received,container,false);
        setHasOptionsMenu(true);
        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        notificationList = new ArrayList<>();
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializeNotifications(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_notifications);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeNotifications(false);
            }
        });
        listView = customListView.getListView();

        listViewListener = new myCustomListViewListener(listView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if(notificationList.size()>=limitValue)
                    notificationLoadMore();
            }
        };

        listView.setOnScrollListener(listViewListener);
        adapter = new NotificationAdapter(getContext(),R.layout.list_item_notification,notificationList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= notificationList.size())
                    return;
                Intent intent = new Intent(getContext(), NotificationDetailsActivity.class);
                intent.putExtra("MyNotification",notificationList.get(i));
                startActivity(intent);
                notificationList.get(i).setRead(1);
                adapter.notifyDataSetChanged();
            }
        });
        if (savedInstanceState == null)
            initializeNotifications(true);
        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("NotificationsList", (Serializable) notificationList);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.notifi);

        ArrayList<NotificationEntity> list1 = (ArrayList<NotificationEntity>) savedInstanceState.getSerializable("NotificationsList");
        notificationList.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(notificationList.size());
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }
    private void notificationLoadMore() {
        customListView.loadMore();
        currentStart = notificationList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeNotifications(true);
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

    private void initializeNotifications(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("notification.to_id",globalVars.getId());

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.notification);
            HashMap<String,String> params = new HashMap<>();
            params.put("where",where_info.toString());
            params.put("limit",limit_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response,loadMore);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response ,boolean loadMore) {
        Log.d(TAG,String.valueOf(response));
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            notificationList.clear();

        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    NotificationEntity entity = new NotificationEntity(response.getJSONObject(i));
                    notificationList.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customListView.notifyChange(notificationList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }
}
