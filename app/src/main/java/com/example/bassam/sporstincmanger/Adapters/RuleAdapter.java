package com.example.bassam.sporstincmanger.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.R;

import java.util.List;

/**
 * Created by Bassam on 1/3/2018.
 */

public class RuleAdapter extends ArrayAdapter<String> {
    Context context ;
    List<String> ruleList;


    public RuleAdapter(@NonNull Context context, int resource, List<String> ruleList) {
        super(context, resource);
        this.context = context;
        this.ruleList = ruleList;
    }

    @Override
    public int getCount() {
        return ruleList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return ruleList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_rule, null);
        }
        String item = getItem(position);

        TextView count = view.findViewById(R.id.ruleCount);
        TextView content = view.findViewById(R.id.ruleContent);

        count.setText(String.valueOf(position+1));
        content.setText(item);

        return  view;
    }
}