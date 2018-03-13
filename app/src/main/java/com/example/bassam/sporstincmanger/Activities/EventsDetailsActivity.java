package com.example.bassam.sporstincmanger.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;

public class EventsDetailsActivity extends AppCompatActivity {

    TextView title ,date ,time ,description;

    CustomLoadingView loadingView;
    int loadingTime = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Event Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingView = findViewById(R.id.LoadingView);
        title = findViewById(R.id.eventDetailsTitle);
        time = findViewById(R.id.eventDetailsTime);
        date = findViewById(R.id.eventDetailsDate);
        description = findViewById(R.id.eventDetailsDescription);

        final EventEntity eventEntity = (EventEntity) getIntent().getSerializableExtra("MyEvent");

        if (savedInstanceState != null)
            loadingTime = 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (eventEntity!=null){
                    fillView(eventEntity);
                }
                else
                    loadingView.fails(); }
        }, loadingTime);
    }

    private void fillView(EventEntity eventEntity) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(eventEntity.getDate());
        title.setText(eventEntity.getTitle());
        time.setText(eventEntity.getTime());
        date.setText(formattedDate);
        description.setText(eventEntity.getDescription());
        loadingView.success();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
