package com.example.bassam.sporstincmanger.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventsDetailsActivity extends AppCompatActivity {

    TextView title ,date ,time ,description;

    CustomLoadingView loadingView;
    private ImageView eventImage;
    private ProgressBar progressBar;
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
        time = findViewById(R.id.eventDetailTime);
        date = findViewById(R.id.event_date);
        description = findViewById(R.id.event_description);
        eventImage = findViewById(R.id.event_Image);
        progressBar = findViewById(R.id.progress_bar);

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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(eventEntity.getDate());
        getSupportActionBar().setTitle(eventEntity.getTitle());
        time.setText(eventEntity.getTime());
        date.setText(formattedDate);
        description.setText(eventEntity.getDescription());
        String ImageUrl = eventEntity.getImgUrl();

        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(Constants.others_host + ImageUrl).into(eventImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
        }
        loadingView.success();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
