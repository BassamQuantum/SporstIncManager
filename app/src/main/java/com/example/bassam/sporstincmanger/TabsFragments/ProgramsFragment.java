package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bassam.sporstincmanger.Activities.LevelsActivity;
import com.example.bassam.sporstincmanger.Adapters.ProgramsAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.ProgramEntity;
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
 * A placeholder fragment containing a simple view.
 */
public class ProgramsFragment extends Fragment {

    private ProgramsAdapter adapter;
    private List<ProgramEntity> programList;

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    private boolean connectionStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_programs,container,false);
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializePrograms(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_programs);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializePrograms(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (programList.size() >= limitValue)
                    ListLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);
        programList =new ArrayList<>();

        adapter = new ProgramsAdapter(getContext(),R.layout.list_item_programs, programList);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= programList.size())
                    return;
                Intent intent = new Intent(getContext(), LevelsActivity.class);
                intent.putExtra("program_id", programList.get(i).getId());
                startActivity(intent);
            }
        });

        if (savedInstanceState == null)
            initializePrograms(false);
        else
            fillBySavedState(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("ProgramList", (Serializable) programList);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<ProgramEntity> list1 = (ArrayList<ProgramEntity>) savedInstanceState.getSerializable("ProgramList");
        programList.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(programList.size());
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    private void ListLoadMore() {
        customListView.loadMore();
        currentStart = programList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializePrograms(true);
            }
        }, 1500);
    }

    private void initializePrograms(final boolean loadMore) {
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
            limit_info.put(getString(R.string.select_start), currentStart);
            limit_info.put(getString(R.string.select_limit), limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table), getString(R.string.table_program));
            params.put(getString(R.string.parameter_order),getString(R.string.value_true));
            params.put(getString(R.string.parameter_limit), limit_info.toString());

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
            programList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    programList.add(new ProgramEntity( response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            customListView.timeOut();
            return;
        }
        customListView.notifyChange(programList.size());
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