package com.example.bassam.sporstincmanger.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Aaa_data.BottomNavigationAction;
import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Adapters.PaymentTraineesAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.GroupEntity;
import com.example.bassam.sporstincmanger.Entities.TraineeEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PaymentTraineesActivity extends AppCompatActivity {

    private String Levelname , ClassName;
    private int ClassID;

    TextView className ;
    TextView ReminderBtn;
    private String ClassStartDate;
    private TextView dateLable;
    private List<TraineeEntity> traineeList;
    private PaymentTraineesAdapter adapter;
    private List<TraineeEntity> allTraineeList;

    GlobalVars globalVars;
    CustomLoadingView loadingView;
    LinearLayout navigationBlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_trainees);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars  = (GlobalVars) getApplication();

        Levelname = getIntent().getStringExtra("LevelName");
        ClassName = getIntent().getStringExtra("ClassName");
        ClassStartDate = getIntent().getStringExtra("ClassDate");
        ClassID = getIntent().getIntExtra("ClassID",0);

        navigationBlow = findViewById(R.id.belowlayout_navigation);
        BottomNavigationAction bottomNavigationAction = new BottomNavigationAction(getApplicationContext() ,navigationBlow);
        bottomNavigationAction.createClickListener();
        ReminderBtn = findViewById(R.id.reminderBtn);
        className = findViewById(R.id.ClassName);
        dateLable = findViewById(R.id.classStartDate);
        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                initializeTrainees();
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.paymentStatusFilter);
        String[] stringArray = getResources().getStringArray(R.array.payment_status_array);
        List<String> plantsList = new ArrayList<>(Arrays.asList(stringArray));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_status_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_status_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    filterTrainees(position-2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final CheckBox checkBox = findViewById(R.id.checkBox_item);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notifyTraineesList(checkBox.isChecked());
            }
        });

        ReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemindTraineesSelected();
            }
        });
        traineeList = new ArrayList<>();
        allTraineeList = new ArrayList<>();
        adapter = new PaymentTraineesAdapter(getApplicationContext(),R.layout.list_items_payment_trainee,traineeList);
        ListView listView = findViewById(R.id.trainees_listView);
        listView.setAdapter(adapter);

        initializeTrainees();
    }

    private void RemindTraineesSelected() {
        ArrayList<Integer> TraineesIDs = new ArrayList<>();
        for (TraineeEntity item : traineeList){
            if (item.isChecked()){
                TraineesIDs.add(item.getTraineeID());
            }
        }
        if (TraineesIDs.size() > 0)
            SendNotification(TraineesIDs);
        else
            show_toast("No trainee was selected...");
    }

    private void SendNotification(ArrayList<Integer> traineesIDs) {
        JSONArray array = new JSONArray(traineesIDs);
        JSONObject values = new JSONObject();
        try {
            show_toast("sending...");
            String title = "Payment Reminder";
            String Message = "this is a reminder for you for Not paid for "+Levelname + " , "+ ClassName+"\n";

            values.put("title",title);
            values.put("message",Message);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.sendReminder);
            HashMap<String,String> params = new HashMap<>();

            params.put("to_id",String.valueOf(array) );
            params.put("from_id",String.valueOf(globalVars.getId()));
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response!=null) {
                            String result = String.valueOf(response.get(0));
                            if (result.equals("ERROR"))
                                show_toast("Failed to send");
                            else
                                show_toast("Successfully sent");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void show_toast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void notifyTraineesList(boolean checked) {
        for (int i=0 ;i <traineeList.size(); i++){
            traineeList.get(i).setChecked(checked);
        }
        adapter.notifyDataSetChanged();
    }

    private void filterTrainees(int selectedStatus) {
        traineeList.clear();
        for (TraineeEntity item : allTraineeList){
            if (item.getPaidStatus() == selectedStatus || selectedStatus < 0){
                traineeList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getApplicationContext())) {
            return true;
        }
        return false;
    }

    private void initializeTrainees() {
        if (!checkConnection()){
            loadingView.fails();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.TraineePayment);
            JSONObject where_info = new JSONObject();

            where_info.put("group_id" ,ClassID );

            HashMap<String, String> params = new HashMap<>();
            params.put("where",where_info.toString());

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
        traineeList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    TraineeEntity entity = new TraineeEntity(jsonObject);
                    traineeList.add(entity);
                }
                allTraineeList.addAll(traineeList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fillView();
    }


    private void fillView() {
        getSupportActionBar().setTitle(Levelname);
        className.setText(ClassName);
        dateLable.setText(ClassStartDate);
        adapter.notifyDataSetChanged();
        loadingView.success();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
