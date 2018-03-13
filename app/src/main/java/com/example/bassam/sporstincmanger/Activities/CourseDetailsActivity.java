package com.example.bassam.sporstincmanger.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView CourseName ,ClassesNum ,CoursePrice ,description ,startDate ,endDate ,CourseLevel;
    ImageView levelImage;

    CustomLoadingView loadingView;
    int loadingTime = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //CourseName = findViewById(R.id.course_details_name);
        loadingView = findViewById(R.id.LoadingView);
        levelImage = findViewById(R.id.Course_icon);
        ClassesNum = findViewById(R.id.course_details_no_classes);
        CoursePrice = findViewById(R.id.course_details_price);
        description = findViewById(R.id.course_details_description);
        startDate = findViewById(R.id.course_details_start_date);
        endDate = findViewById(R.id.course_details_end_date);
        CourseLevel = findViewById(R.id.course_details_level);

        final CourseEntity myCourse = (CourseEntity) getIntent().getSerializableExtra("MyCourse");

        if (savedInstanceState!=null)
            loadingTime = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myCourse != null){
                    fillView(myCourse);
                }
                else
                    loadingView.fails(); }
        }, loadingTime);

    }

    private void fillView(CourseEntity courseEntity) {
       // CourseName.setText(courseEntity.getCourseName());
        getSupportActionBar().setTitle(courseEntity.getCourseName());
        //fillImage(courseEntity.getCourseName());
        String ImageUrl = courseEntity.getImageUrl();
        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(Constants.others_host + ImageUrl).into(levelImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ", "ERROR In Loading");
                }
            });
        }
        ClassesNum.setText(courseEntity.getClasses_Num());
        CoursePrice.setText(courseEntity.getPrice());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(courseEntity.getStartDate());
        startDate.setText(date);
        date = df.format(courseEntity.getEndDate());
        endDate.setText(date);
        CourseLevel.setText(courseEntity.getLevel());
        description.setText(courseEntity.getDescription());
        loadingView.success();
    }

    public void fillImage(String name){
        switch (name){
            case "Star fish":
                levelImage.setImageResource(R.drawable.star);
                break;
            case "Dolphin":
                levelImage.setImageResource(R.drawable.dolphin);
                break;
            case "Duck":
                levelImage.setImageResource(R.drawable.duck);
                break;
            case "Frog":
                levelImage.setImageResource(R.drawable.frog);
                break;
            case "Jelly fish":
                levelImage.setImageResource(R.drawable.jellyfish);
                break;
            case "Nemo":
                levelImage.setImageResource(R.drawable.nemo);
                break;
            case "Penguin":
                levelImage.setImageResource(R.drawable.penguin);
                break;
            case "Seal":
                levelImage.setImageResource(R.drawable.seal);
                break;
            case "Shark":
                levelImage.setImageResource(R.drawable.shark);
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
