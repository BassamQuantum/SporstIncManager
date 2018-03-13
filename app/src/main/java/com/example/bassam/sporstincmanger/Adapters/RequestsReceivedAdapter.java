package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.requestsEntity;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 2/7/2018.
 */

public class RequestsReceivedAdapter extends ArrayAdapter<requestsEntity> {
    Context context ;
    List<requestsEntity> myrequests;


    public RequestsReceivedAdapter(@NonNull Context context, int resource, List<requestsEntity> myrequests) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_request_received, null);
        }
        // Get the data item for this position
        requestsEntity request = getItem(position);
        // Lookup view for data population
        TextView creation_date =  view.findViewById(R.id.creationDate);
        TextView coursename_and_class =  view.findViewById(R.id.courseNameAndClassNumberTextView);
        TextView request_for =  view.findViewById(R.id.requestforTextView);
        TextView date =  view.findViewById(R.id.classDate);

        ImageButton accept = view.findViewById(R.id.acceptImageButton);

        // Populate the data into the template view using the data object
        creation_date.setText(request.getCreation_date());
        String content = "";
        if (request.getCourse()!=null){
            content += request.getCourse();
            if (request.getGroup()!=null)
                content += ", "+ request.getGroup();
        }
        coursename_and_class.setText(content);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String RequestDate = formatter.format(request.getDate());
        date.setText(RequestDate);
        request_for.setText("Request for:"+request.getSubject());

        if (request.getState() == 0)
        accept.setBackgroundResource(R.drawable.ic_not_checked);
        else if (request.getState() == 2)
        accept.setBackgroundResource(R.drawable.ic_class_waiting);
        else
        accept.setBackgroundResource(R.drawable.ic_check_circle);
        // Return the completed view to render on screen
        return view;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}

