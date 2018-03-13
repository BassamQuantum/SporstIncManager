package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.NotificationEntity;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 12/25/2017.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationEntity> {
    Context context ;
    List<NotificationEntity> mynotification;


    public NotificationAdapter(@NonNull Context context, int resource, List<NotificationEntity> mynotification) {
        super(context, resource);
        this.context = context;
        this.mynotification = mynotification;
    }

    @Override
    public int getCount() {
        return mynotification.size();
    }

    @Nullable
    @Override
    public NotificationEntity getItem(int position) {
        return mynotification.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification, null);
        }
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String Today = df.format(c.getTime());

        NotificationEntity notificationEntity = getItem(position);

        LinearLayout background = view.findViewById(R.id.notification_layout);
        TextView title = view.findViewById(R.id.notificationTitle);
        TextView subject = view.findViewById(R.id.notificationSubject);
        TextView data = view.findViewById(R.id.notificationDate);

        title.setText(notificationEntity.getSubject());
        subject.setText(notificationEntity.getContent());

        Date notifyDate = notificationEntity.getNotification_date();
        String time;
        String date = df.format(notifyDate);
        if (date.equals(Today)){
            df = new SimpleDateFormat("h:mm a");
            time = df.format(notifyDate);
        }else {
            df = new SimpleDateFormat("MMM d");
            time = df.format(notifyDate);
        }

        data.setText(time);

        int read = notificationEntity.getRead();

        if(read == 0)
            background.setBackgroundColor(Color.parseColor("#ebf1fc"));
        else
            background.setBackgroundColor(Color.parseColor("#FFFFFF"));

        return  view;
    }
}
