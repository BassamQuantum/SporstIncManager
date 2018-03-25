package com.example.bassam.sporstincmanger.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Aaa_data.BottomNavigationAction;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventsDetailsActivity extends AppCompatActivity {

    TextView  title ,date, time, description , event_link ,eventFile;

    CustomLoadingView loadingView;
    LinearLayout navigationBlow;
    private ImageView eventImage;
    private ProgressBar progressBar;
    int loadingTime = 1200;

    DownloadManager downloadManager;
    private EventEntity eventEntity;

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
        navigationBlow = findViewById(R.id.belowlayout_navigation);
        BottomNavigationAction bottomNavigationAction = new BottomNavigationAction(getApplicationContext() ,navigationBlow);
        bottomNavigationAction.createClickListener();
        title = findViewById(R.id.event_title);
        time = findViewById(R.id.eventDetailTime);
        date = findViewById(R.id.event_date);
        description = findViewById(R.id.event_description);
        event_link =findViewById(R.id.event_link);
        eventFile =findViewById(R.id.event_file);
        eventImage = findViewById(R.id.event_Image);
        progressBar = findViewById(R.id.progress_bar2);

        eventEntity = (EventEntity) getIntent().getSerializableExtra("MyEvent");

        event_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventEntity.getEventUrl()));
                startActivity(browserIntent);
            }
        });

        eventFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadEventFile();
            }
        });


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

    private void downloadEventFile() {
        Toast.makeText(getApplicationContext(),"Download...",Toast.LENGTH_SHORT).show();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Constants.upload_files_host+eventEntity.getEventFileUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }

    private void fillView(EventEntity eventEntity) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(eventEntity.getDate());
        title.setText(eventEntity.getTitle());
        time.setText(eventEntity.getTime());
        date.setText(formattedDate);
        if (eventEntity.getEventUrl() == null || eventEntity.getEventUrl().equals("") )
            event_link.setVisibility(View.GONE);
        else
            event_link.setText(eventEntity.getEventUrl());

        if (eventEntity.getEventFileUrl() == null || eventEntity.getEventFileUrl().equals("") )
            eventFile.setVisibility(View.GONE);
        else
            eventFile.setText(eventEntity.getEventFileUrl());

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
                    progressBar.setVisibility(View.GONE);
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
