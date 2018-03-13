package com.example.bassam.sporstincmanger.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.CustomView.myCustomListView;
import com.example.bassam.sporstincmanger.Entities.complainEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ComplainDetailsActivity extends AppCompatActivity {

    TextView date ,person ,content,subject;
    CustomLoadingView loadingView;

    complainEntity complain;

    int ID ,loadingTime = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Complain Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                retrieveComplain(ID);
            }
        });
        date = findViewById(R.id.complainReviewDate);
        person = findViewById(R.id.complainReviewPerson);
        content = findViewById(R.id.complainReviewContent);
        subject = findViewById(R.id.complainReviewSubject);

        if (savedInstanceState != null) {
            complain = (complainEntity) savedInstanceState.getSerializable("Complain");
            loadingTime = 0;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        complain = (complainEntity) getIntent().getSerializableExtra("MyComplain");
        final int notify_id = getIntent().getIntExtra("notify_id",-1);
        if (loadingTime == 0){
            fillView(complain);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notify_id != -1)
                    retrieveComplain(notify_id);
                else if (complain != null)
                    fillView(complain);
                else {
                    loadingView.fails();
                }
            }
        },loadingTime);
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
                    retrieveComplain(notify_id);
                }
                else {
                    loadingView.fails();
                }
            }
        },1200);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Complain",complain);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    private void retrieveComplain(int notify_id) {
        if (!checkConnection()){
            ID = notify_id;
            loadingView.fails();
            loadingView.enableRetry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);
            JSONObject where_info = new JSONObject();
            where_info.put("complains.id",notify_id);
            String OnCondition = "complains.user_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "complains");
            params.put("table2", "users");

            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response != null) {
                            complain = new complainEntity(response.getJSONObject(0));
                            fillView(complain);
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

    private void fillView(complainEntity complain) {
        if (complain == null){
            loadingView.fails();
            return;
        }
        subject.setText(complain.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy hh:mm");
        String str = format.format(complain.getDate());
        date.setText(str);
        person.setText(complain.getPersonName());
        content.setText(complain.getContent());
        loadingView.success();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
