package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.GroupEntity;
import com.example.bassam.sporstincmanger.R;

import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class PaymentGroupAdapter extends ArrayAdapter<GroupEntity> {
    Context context ;
    List<GroupEntity> mygroup;


    public PaymentGroupAdapter(@NonNull Context context, int resource, List<GroupEntity> mygroup) {
        super(context, resource);
        this.context = context;
        this.mygroup = mygroup;
    }

    @Override
    public int getCount() {
        return mygroup.size();
    }

    @Nullable
    @Override
    public GroupEntity getItem(int position) {
        return mygroup.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.report_list_items, null);
        }
        GroupEntity group = getItem(position);

        TextView GroupName = view.findViewById(R.id.reportName);
        TextView TraineeNum = view.findViewById(R.id.reportNumbers);
        TextView Groups = view.findViewById(R.id.reportNumbersName);
        TextView PaidNum = view.findViewById(R.id.reportPaied);

        Groups.setVisibility(View.VISIBLE);
        TraineeNum.setVisibility(View.VISIBLE);

        Groups.setText("Trainees");
        GroupName.setText(group.getName());
        TraineeNum.setText(String.valueOf(group.getTraineeNum()));
        PaidNum.setText(String.valueOf(group.getPaidNum()));

        return  view;
    }
}
