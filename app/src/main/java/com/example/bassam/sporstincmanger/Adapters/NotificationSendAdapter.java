package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.NotificationEntity;
import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Bassam on 12/25/2017.
 */

public class NotificationSendAdapter extends ArrayAdapter<NotificationEntity> {
    Context context ;
    List<NotificationEntity> myNotify;


    public NotificationSendAdapter(@NonNull Context context, int resource, List<NotificationEntity> myNotify) {
        super(context, resource);
        this.context = context;
        this.myNotify = myNotify;
    }

    @Override
    public int getCount() {
        return myNotify.size();
        }

    @Nullable
    @Override
    public NotificationEntity getItem(int position) {
        return myNotify.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification_sent, null);
        }
        NotificationEntity item = getItem(position);

        TextView Subject = view.findViewById(R.id.notificationSubject);
        TextView Date = view.findViewById(R.id.notificationDate);
        TextView status = view.findViewById(R.id.notificationStatus);
        TextView person = view.findViewById(R.id.notificationPerson);

        int notifyStatus = item.getRead();

        String myStatus = "Not Seen";
        if (notifyStatus == 1)
            myStatus = "Seen";

        if (notifyStatus == 1)
            status.setTextColor(Color.parseColor("#22a630"));
        else
            status.setTextColor(Color.parseColor("#df1b1c"));

        person.setText(item.getTo_name());
        Subject.setText(item.getSubject());
        status.setText(myStatus);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        String ReqDate = formatter.format(item.getNotification_date());
        Date.setText(ReqDate);

        return  view;
    }
}
