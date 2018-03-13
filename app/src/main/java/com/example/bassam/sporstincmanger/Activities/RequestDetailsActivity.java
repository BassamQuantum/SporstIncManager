package com.example.bassam.sporstincmanger.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class RequestDetailsActivity extends AppCompatActivity {
    TextView subject ,content ,person ,date ,status,Course ,Group;
    LinearLayout mybuttons ,statusLayout;
    TextView accept , reject;
    boolean received = false;
    ProgressDialog progressDialog;

    CustomLoadingView loadingView;
    private int ID;
    private int requestID;
    private int loadingTime = 1200;
    private requestsEntity myRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Request Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                retrieveRequest(ID);
            }
        });
        progressDialog = new ProgressDialog(RequestDetailsActivity.this);
        subject = findViewById(R.id.requestReviewSubject);
        content = findViewById(R.id.requestReviewContent);
        person = findViewById(R.id.requestReviewPerson);
        status = findViewById(R.id.requestReviewStatus);
        date = findViewById(R.id.requestReviewDate);
        Course = findViewById(R.id.requestReviewCourse);
        Group = findViewById(R.id.requestReviewGroup);
        statusLayout = findViewById(R.id.request_statusLayout);
        mybuttons = findViewById(R.id.RequestReviewButtons);
        accept = findViewById(R.id.RequestReviewAccept);
        reject = findViewById(R.id.RequestReviewReject);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateRequest(1);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateRequest(0);
            }
        });

        if (savedInstanceState != null) {
            myRequest = (requestsEntity) savedInstanceState.getSerializable("Request");
            loadingTime = 0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Request",myRequest);
    }

    private void updateRequest(int value){
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id",requestID);

            JSONObject values = new JSONObject();
            values.put("status",value);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","requests");
            params.put("where",where_info.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    progressDialog.dismiss();
                    if (checkResponse(response))
                        mybuttons.setVisibility(View.GONE);
                    else
                        Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_LONG).show();

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myRequest = (requestsEntity) getIntent().getSerializableExtra("MyRequest");
        final int notify_id = getIntent().getIntExtra("notify_id",-1);
        int requestType = getIntent().getIntExtra("requestType",-1);

        if (requestType == 1)
            received = true;

        if (loadingTime == 0){
            fillView(myRequest);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myRequest != null){
                    fillView(myRequest);
                }
                else if (notify_id != -1){
                    retrieveRequest(notify_id);
                }
                else
                    loadingView.fails(); }
        }, loadingTime);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final int notify_id = intent.getIntExtra("notify_id",-1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notify_id != -1) {
                    loadingView.loading();
                    retrieveRequest(notify_id);
                }
                else {
                    loadingView.fails();
                }
            }
        },loadingTime);
    }

    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (!result.equals("ERROR"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    private void retrieveRequest(int notify_id) {
        if (!checkConnection()){
            ID = notify_id;
            loadingView.fails();
            loadingView.enableRetry();
            return;
        }
        try {
            received = true;
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);
            JSONObject where_info = new JSONObject();
            where_info.put("requests.id", notify_id);

            String OnCondition = "requests.to_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "requests");
            params.put("table2", "users");

            params.put("where", where_info.toString());
            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response != null) {
                            myRequest = new requestsEntity(response.getJSONObject(0));
                            fillView(myRequest);
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

    private void fillView(requestsEntity myRequest) {
       /* if(myRequest.getPersonType().equals("Trainee")) {
            LinearLayout groupLayout = findViewById(R.id.requestGroupLayout);
            groupLayout.setVisibility(View.VISIBLE);
            Group.setText(myRequest.getGroup());
        }*/
        requestID = myRequest.getRequest_ID();
        String requestStatus = myRequest.getStatus();

        if (requestStatus == null || requestStatus.equals(""))
            requestStatus = "Waiting";

        if (requestStatus.equals("Accepted"))
            status.setTextColor(Color.parseColor("#22a630"));
        else if (requestStatus.equals("Rejected"))
            status.setTextColor(Color.parseColor("#df1b1c"));
        else if (requestStatus.equals("Waiting"))
            status.setTextColor(Color.parseColor("#f98a03"));

        if(requestStatus.equals("Waiting")&&received) {
            mybuttons.setVisibility(View.VISIBLE);
            statusLayout.setVisibility(View.GONE);
        }else {
            mybuttons.setVisibility(View.GONE);
            statusLayout.setVisibility(View.VISIBLE);
        }

        status.setText(requestStatus);
        subject.setText(myRequest.getSubject());
        person.setText(myRequest.getPerson());
        content.setText(myRequest.getContent());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        String ReqDate = formatter.format(myRequest.getDate());
        date.setText(ReqDate);
        Course.setText(myRequest.getCourse());
        loadingView.success();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
