package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.TraineeEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 19/3/2018.
 */

public class PaymentTraineesAdapter extends ArrayAdapter<TraineeEntity> {

    Context context;
    List<TraineeEntity> traineesList;

    public PaymentTraineesAdapter(@NonNull Context context, int resource , List<TraineeEntity> traineesList) {
        super(context, resource);
        this.context = context;
        this.traineesList = traineesList;
    }

    @Override
    public int getCount() {
        return traineesList.size();
    }

    @Nullable
    @Override
    public TraineeEntity getItem(int position) {
        return traineesList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_items_payment_trainee, null);
        }
        final int pos = position;
        TraineeEntity item = getItem(position);

        TextView name = view.findViewById(R.id.TraineeName);
        ImageView paymentStatus = view.findViewById(R.id.paymentStatusIcon);
        final CheckBox select = view.findViewById(R.id.checkBox_item);

        select.setChecked(item.isChecked());
        select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                traineesList.get(pos).setChecked(select.isChecked());
            }
        });

        name.setText(item.getTraineeName());
        int status = item.getPaidStatus();

        if (status == 1){
            paymentStatus.setImageResource(R.drawable.ic_paid_24dp);
        }
        else
            paymentStatus.setImageResource(R.drawable.ic_cart);
        paymentStatus.setColorFilter(Color.parseColor("#001b51"));
        return  view;
    }
}
