package com.example.bassam.sporstincmanger.NavigationDrawer_Fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bassam.sporstincmanger.Activities.ComplainDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.ComplainsAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Entities.complainEntity;
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
 * Created by Bassam on 12/21/2017.
 */

public class complainsFragment extends Fragment {
    private static final String TAG = complainsFragment.class.getSimpleName();

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;

    private ComplainsAdapter adapter;
    private List<complainEntity> ReviewedcomplainList;
    private List<complainEntity> AllComplains;

    myCustomListViewListener listViewListener;
    int limitValue,currentStart;

    int selectedPosition = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_complains,container,false);
        setHasOptionsMenu(true);

        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        ReviewedcomplainList = new ArrayList<>();
        AllComplains = new ArrayList<>();
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializeComplains(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_complains);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeComplains(false);
            }
        });
        listView = customListView.getListView();

        listViewListener = new myCustomListViewListener(listView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if(AllComplains.size() >= limitValue)
                    ListLoadMore();
            }
        };

        listView.setOnScrollListener(listViewListener);
        adapter = new ComplainsAdapter(getContext(),R.layout.list_item_complains, ReviewedcomplainList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= ReviewedcomplainList.size())
                return;
                Intent intent = new Intent(getContext(), ComplainDetailsActivity.class);
                intent.putExtra("MyComplain", ReviewedcomplainList.get(i));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null)
            initializeComplains(false);
        else
            fillBySavedState(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(null);
        /*outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("ComplainsList", (Serializable) AllComplains);
        outState.putInt("Filter",selectedPosition);*/
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.complain);

        ArrayList<complainEntity> list1 = (ArrayList<complainEntity>) savedInstanceState.getSerializable("ComplainsList");
        AllComplains.addAll(list1);
        selectedPosition = savedInstanceState.getInt("Filter");
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        filterData(selectedPosition);

        listView.onRestoreInstanceState(mListInstanceState);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(selectedPosition+1).setChecked(true);
    }

    private void ListLoadMore() {
        customListView.loadMore();
        currentStart = AllComplains.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeComplains(true);
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

    private void initializeComplains(final boolean loadMore) {
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
            httpCall.setUrl(Constants.join);

            JSONObject limit_info = new JSONObject();
            String where_info = "{\"complains.to_id IS NULL\":null}";
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            String OnCondition = "complains.user_id = users.id";
            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "complains");
            params.put("table2", "users");

            params.put("on", OnCondition);
            params.put("limit",limit_info.toString());
            params.put("where",where_info);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG,String.valueOf(response));
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
            ReviewedcomplainList.clear();

        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    ReviewedcomplainList.add(new complainEntity(response.getJSONObject(i)));
                }
                AllComplains = new ArrayList<>(ReviewedcomplainList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        filterData(selectedPosition);
        listViewListener.setLoading(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.complain_filter, menu);  // Use classes_filter.xmllter.xml from step 1
        menu.getItem(0).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.complain_filter1) {
            item.setChecked(true);
            selectedPosition = -1;
            filterData(selectedPosition);
            return true;
        }
        else if (id == R.id.complain_filter2) {
            item.setChecked(true);
            selectedPosition = 0;
            filterData(selectedPosition);
            return true;
        }
        else if (id == R.id.complain_filter3) {
            item.setChecked(true);
            selectedPosition = 1;
            filterData(selectedPosition);
            return true;
        }
        else if (id == R.id.complain_filter4) {
            item.setChecked(true);
            selectedPosition = 2;
            filterData(selectedPosition);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterData(int personType) {
        ReviewedcomplainList.clear();
        for (int i=0 ;i<AllComplains.size();i++){
            if (AllComplains.get(i).getPersonType() == personType || personType == -1) {
                ReviewedcomplainList.add(AllComplains.get(i));
            }
        }
        customListView.notifyChange(ReviewedcomplainList.size());
        adapter.notifyDataSetChanged();
    }
}
