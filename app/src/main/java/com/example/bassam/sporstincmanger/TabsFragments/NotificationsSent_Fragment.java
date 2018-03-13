package com.example.bassam.sporstincmanger.TabsFragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Activities.NewRequestActivity;
import com.example.bassam.sporstincmanger.Activities.RequestDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.RequestsSentAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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

public class NotificationsSent_Fragment extends Fragment {
    private static final String TAG = NotificationsSent_Fragment.class.getSimpleName();

    GlobalVars globalVars;

    private myCustomListView customListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;

    private RequestsSentAdapter adapter;
    private List<requestsEntity> requestsList;


    private FloatingActionMenu menuLabelsRight;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

    private Handler mUiHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests_sent, container, false);
        setHasOptionsMenu(false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
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
        listViewListener = new myCustomListViewListener(listView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (requestsList.size() >= limitValue)
                    listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);

        requestsList = new ArrayList<>();
        adapter = new RequestsSentAdapter(getContext(), R.layout.list_item_request_sent, requestsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= requestsList.size())
                    return;
                Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
                intent.putExtra("MyRequest", requestsList.get(i));
                startActivity(intent);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuLabelsRight = view.findViewById(R.id.menu_labels_right);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);

        menuLabelsRight.hideMenuButton(false);
        menuLabelsRight.setClosedOnTouchOutside(true);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        createCustomAnimation();
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuLabelsRight.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuLabelsRight.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuLabelsRight.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuLabelsRight.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuLabelsRight.getMenuIconView().setImageResource(menuLabelsRight.isOpened()
                        ? R.drawable.ic_create_white_24dp : R.drawable.ic_close);
                menuLabelsRight.setMenuButtonColorNormal(menuLabelsRight.isOpened()
                        ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.FloatingClick));
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuLabelsRight.setIconToggleAnimatorSet(set);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int delay = 400;
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuLabelsRight.showMenuButton(true);
                }
            }, delay);

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
            httpCall.setUrl(Constants.join);
            JSONObject where_info = new JSONObject();
            where_info.put("requests.from_id", globalVars.getId());
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);

            String OnCondition = "requests.to_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "requests");
            params.put("table2", "users");

            Log.d(TAG, "Where Condition:" + where_info.toString());
            params.put("where", where_info.toString());
            params.put("limit",limit_info.toString());
            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response ,loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response,boolean loadMore) {
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), NewRequestActivity.class);
            int Type = 0;
            switch (v.getId()) {
                case R.id.fab1:
                    Type = 1;
                    break;
                case R.id.fab2:
                    Type = 2;
                    break;
            }

            intent.putExtra("CurrentItem",Type);
            startActivity(intent);
            menuLabelsRight.close(true);
        }
    };
}