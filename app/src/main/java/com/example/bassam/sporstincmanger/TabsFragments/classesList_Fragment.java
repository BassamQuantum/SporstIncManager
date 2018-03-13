package com.example.bassam.sporstincmanger.TabsFragments;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Activities.ClassesDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.classesListAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListViewListener;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Entities.NewsEntity;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassam on 12/21/2017.
 */

public class classesList_Fragment extends Fragment {

    static  private String TAG = classesList_Fragment.class.getSimpleName();
    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    private classesListAdapter adapter;
    private List<classesEntity> classesList = new ArrayList<>();
    private List<classesEntity> AllClasses = new ArrayList<>();

    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    private int selectedPostion = -1;
    private int REQUEST_CODE = 1;
    private int SelectedClass = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_classes_list,container,false);
        setHasOptionsMenu(true);

        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0 ;
                initilizeClassesList(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_classes);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0 ;
                initilizeClassesList(false);
            }
        });
        listView = customListView.getListView();

        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (AllClasses.size() >= limitValue)
                    ListLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);

        adapter = new classesListAdapter(getContext(),R.layout.classes_list_items,classesList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= classesList.size())
                    return;
                SelectedClass = i;
                Intent intent = new Intent(getContext(), ClassesDetailsActivity.class);
                intent.putExtra(getString(R.string.Key_ClassEntity),classesList.get(i));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        if (savedInstanceState == null)
            initilizeClassesList(false);
        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("ClassesList", (Serializable) AllClasses);
        outState.putInt("Filter",selectedPostion);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<classesEntity> list1 = (ArrayList<classesEntity>) savedInstanceState.getSerializable("ClassesList");
        AllClasses.addAll(list1);
        selectedPostion = savedInstanceState.getInt("Filter");
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        filterData(selectedPostion);

        listView.onRestoreInstanceState(mListInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(selectedPostion+1).setChecked(true);
    }

    private void ListLoadMore() {
        customListView.loadMore();
        currentStart = AllClasses.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeClassesList(true);
            }
        },1500);
    }

    private void initilizeClassesList(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String Today = df.format(c.getTime());

            JSONObject where_info = new JSONObject();
            where_info.put("class_date",Today);

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.classesData);

            HashMap<String, String> params = new HashMap<>();
            params.put("where", where_info.toString());
            params.put("limit", limit_info.toString());

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

    private void fillAdapter(JSONArray response , boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore) {
            classesList.clear();
            AllClasses.clear();
        }
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    classesEntity entity = new classesEntity( response.getJSONObject(i));
                    if(!classesList.contains(entity))
                        classesList.add(entity);
                }
                AllClasses.addAll(classesList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        filterData(selectedPostion);
        listViewListener.setLoading(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.classes_filter, menu);  // Use classes_filter.xmllter.xml from step 1
        menu.getItem(0).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter1) {
            item.setChecked(true);
            selectedPostion = -1;
            filterData(selectedPostion);
            return true;
        }
        else if (id == R.id.action_filter2) {
            item.setChecked(true);
            selectedPostion = 0;
            filterData(selectedPostion);
            return true;
        }
        else if (id == R.id.action_filter3) {
            item.setChecked(true);
            selectedPostion = 1;
            filterData(selectedPostion);
            return true;
        }
        else if (id == R.id.action_filter4) {
            item.setChecked(true);
            selectedPostion = 2;
            filterData(selectedPostion);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterData(int status) {
        if (status == 1)
            status = 2;
        else if (status == 2)
            status = 1;
        classesList.clear();
        for (classesEntity entity : AllClasses){
            if(entity.getState() == status || status == -1){
                classesList.add(entity);
            }
        }
        customListView.notifyChange(classesList.size());
        adapter.notifyDataSetChanged();
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            int new_status = data.getIntExtra(getString(R.string.SessionStatus),-1);
            if (new_status != -1) {
                classesList.get(SelectedClass).setState(new_status);
                adapter.notifyDataSetChanged();
            }

        }
    }

}
