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
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 2/7/2018.
 */

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.EventsViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<EventEntity> eventsList;

    public EventsRecyclerAdapter(Context context, List<EventEntity> eventsList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.eventsList = eventsList;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_recycler_event, parent, false);
        EventsViewHolder holder = new EventsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        EventEntity entity = eventsList.get(position);
        holder.setData(entity ,context);
        holder.setListener(entity,context);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder{

        private ImageView newsImage;
        private TextView newsTitle;
        private ProgressBar progressBar;
        private TextView Month , Day;
        public EventsViewHolder(View view) {
            super(view);
            newsImage = view.findViewById(R.id.eventsListImage);
            newsTitle = view.findViewById(R.id.eventsListContent);
            progressBar = view.findViewById(R.id.progressBar1);
            Month = view.findViewById(R.id.eventMonth);
            Day = view.findViewById(R.id.eventDay);
        }

        public void setData(EventEntity item , Context context){
            String ImageUrl = item.getImgUrl();

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

            newsTitle.setText(item.getTitle());
            Date mDate = item.getDate();
            SimpleDateFormat df = new SimpleDateFormat("E MMM dd yyyy");
            String formattedDate = df.format(mDate);
            String[] data = formattedDate.split(" ");

            Month.setText(data[1]);
            Day.setText(data[2]);
        }

        public void setListener(final EventEntity entity , final Context context){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EventsDetailsActivity.class);
                    intent.putExtra("MyEvent",entity);
                    context.startActivity(intent);
                }
            });
        }
    }
}

