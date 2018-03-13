package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Activities.EventsDetailsActivity;
import com.example.bassam.sporstincmanger.Activities.NewsDetailsActivity;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Entities.NewsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 2/7/2018.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<NewsEntity> newsList;

    public NewsRecyclerAdapter(Context context, List<NewsEntity> newsList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_recycler_news, parent, false);
        NewsViewHolder holder = new NewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsEntity entity = newsList.get(position);
        holder.setData(entity ,context);
        holder.setListener(entity,context);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        private ImageView newsImage;
        private TextView newsTitle;
        private ProgressBar progressBar;
        public NewsViewHolder(View view) {
            super(view);
            newsImage = view.findViewById(R.id.newsListImage);
            newsTitle = view.findViewById(R.id.newsListContent);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }

        public void setData(NewsEntity item , Context context){
            String ImageUrl = item.getImg();

            if(!ImageUrl.equals("")) {
                Picasso.with(context).load(Constants.others_host + ImageUrl).into(newsImage, new com.squareup.picasso.Callback() {
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

            newsTitle.setText(item.getContent());
        }

        public void setListener(final NewsEntity entity , final Context context){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("MyNews",entity);
                    context.startActivity(intent);
                }
            });
        }
    }
}
