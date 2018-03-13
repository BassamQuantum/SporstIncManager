package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.NewsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 1/3/2018.
 */

public class NewsAdapter extends ArrayAdapter<NewsEntity> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    Context context ;
    List<NewsEntity> newsEntityList;


    public NewsAdapter(@NonNull Context context, int resource, List<NewsEntity> newsEntityList) {
        super(context, resource);
        this.context = context;
        this.newsEntityList = newsEntityList;
    }

    @Override
    public int getCount() {
        return newsEntityList.size();
    }

    @Nullable
    @Override
    public NewsEntity getItem(int position){return  newsEntityList.get(position);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, null);
        }
        NewsEntity eventEntity = getItem(position);

        TextView Title = view.findViewById(R.id.newsContent);
        ImageView imageView = view.findViewById(R.id.newsImage);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        String ImageUrl = eventEntity.getImg();

        if(!ImageUrl.equals("")) {
            Picasso.with(context).load(Constants.others_host + ImageUrl).into(imageView, new com.squareup.picasso.Callback() {
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

        Title.setText(eventEntity.getContent());
        return  view;
    }
}