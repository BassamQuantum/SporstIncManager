package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 1/3/2018.
 */

public class CoursesAdapter extends ArrayAdapter<CourseEntity> {
    Context context ;
    List<CourseEntity> mycourses;


    public CoursesAdapter(@NonNull Context context, int resource, List<CourseEntity> mycourses) {
        super(context, resource);
        this.context = context;
        this.mycourses = mycourses;
    }

    @Override
    public int getCount() {
        return mycourses.size();
    }

    @Nullable
    @Override
    public CourseEntity getItem(int position) {
        return mycourses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, null);
        }
        CourseEntity mycourse = getItem(position);

        TextView Title = view.findViewById(R.id.course_item_name);
        TextView level = view.findViewById(R.id.course_item_level);
        TextView price = view.findViewById(R.id.course_item_price);
        ImageView icon = view.findViewById(R.id.Course_icon);

        String name = mycourse.getCourseName();
       // fillImage(name , icon);
        String ImageUrl = mycourse.getImageUrl();
        if(!ImageUrl.equals("")) {
            Picasso.with(context).load(Constants.others_host + ImageUrl).into(icon, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ","ERROR In Loading");
                }
            });
        }

        Title.setText(name);
        level.setText(mycourse.getLevel());
        price.setText("$ "+mycourse.getPrice());

        return  view;
    }

    public void fillImage(String name , ImageView levelImage){
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
}