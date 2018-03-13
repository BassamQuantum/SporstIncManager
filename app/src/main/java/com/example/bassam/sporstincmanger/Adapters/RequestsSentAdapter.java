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

import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Bassam on 12/25/2017.
 */

public class RequestsSentAdapter extends ArrayAdapter<requestsEntity> {
    Context context ;
    List<requestsEntity> myrequests;


    public RequestsSentAdapter(@NonNull Context context, int resource, List<requestsEntity> myrequests) {
        super(context, resource);
        this.context = context;
        this.myrequests = myrequests;
    }

    @Override
    public int getCount() {
        return myrequests.size();
        }

    @Nullable
    @Override
    public requestsEntity getItem(int position) {
        return myrequests.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_request_sent, null);
        }
        requestsEntity request = getItem(position);

        TextView Subject = view.findViewById(R.id.requestSubject);
        TextView Date = view.findViewById(R.id.requestdate);
        TextView status = view.findViewById(R.id.request_status);
        TextView person = view.findViewById(R.id.requestPerson);

        String requestStatus = request.getStatus();

        if (requestStatus== null || requestStatus.equals(""))
            requestStatus = "Waiting";

        if (requestStatus.equals("Accepted"))
            status.setTextColor(Color.parseColor("#22a630"));
        else if (requestStatus.equals("Rejected"))
            status.setTextColor(Color.parseColor("#df1b1c"));
        else if (requestStatus.equals("Waiting"))
            status.setTextColor(Color.parseColor("#f98a03"));

        person.setText(request.getPerson());
        Subject.setText(request.getSubject());
        status.setText(requestStatus);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        String ReqDate = formatter.format(request.getDate());
        Date.setText(ReqDate);

        return  view;
    }
}
