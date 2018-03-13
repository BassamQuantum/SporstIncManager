package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Activities.ClassesDetailsActivity;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomCalendar.CalendarCustomView;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.Interfaces.MyItemClickListener;
import com.example.bassam.sporstincmanger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassam on 12/21/2017.
 */

public class calender_Fragment extends Fragment {
    GlobalVars globalVars;

    CalendarCustomView calendarView;

    HashMap<String, List<classesEntity> > EventsMap;
    List<classesEntity> classesList ;
    private int REQUEST_CODE = 1 ,event_positions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calender,container,false);
        setHasOptionsMenu(false);
        globalVars = (GlobalVars) getActivity().getApplication();

        calendarView =  root.findViewById(R.id.custom_calendar);

        classesList = new ArrayList<>();
        EventsMap = new HashMap<>();

        initializeClassesList();

        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!autoRotation())
            return;

        if(isVisibleToUser) {
            if (getActivity() != null)
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        else {
            if (getActivity() != null)
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void fileEventsMap() {
        for (classesEntity entity : classesList){
            String ClassDate = entity.getClassdate();
            List<classesEntity> MapList = EventsMap.get(ClassDate);
            if (MapList == null)
                MapList = new ArrayList<>();
            MapList.add(entity);
            EventsMap.put(ClassDate,MapList);
        }
        calendarView.setEventsListener(new MyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                event_positions = position;
                Intent intent = new Intent(getContext(), ClassesDetailsActivity.class);
                intent.putExtra(getString(R.string.Key_ClassEntity),calendarView.getMyEvents().get(calendarView.SelectedDate).get(position));
                startActivityForResult(intent , REQUEST_CODE);
            }
        });
        calendarView.setMyEvents(EventsMap);
    }

    private boolean autoRotation(){
        if (getActivity() == null)
            return true;
        int rotate = android.provider.Settings.System.getInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        return (rotate == 1) ;
    }

    private void initializeClassesList() {
        try {
            JSONObject where_info = new JSONObject();

            HashMap<String, String> params = new HashMap<>();

            switch (globalVars.getType()){
                case 0:
                    where_info.put("group_trainee.trainee_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;
                case 1:
                    where_info.put("groups.coach_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;
                case 2:
                    where_info.put("groups.admin_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;
            }
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.classesData);

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
        classesList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    classesEntity entity = new classesEntity( response.getJSONObject(i));
                    if(!classesList.contains(entity))
                        classesList.add(entity);
                }
                fileEventsMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                calendarView.getMyEvents().get(calendarView.SelectedDate).get(event_positions).setState(new_status);
                calendarView.setUpEventsAapter(calendarView.SelectedDate);
            }

        }
    }
}
