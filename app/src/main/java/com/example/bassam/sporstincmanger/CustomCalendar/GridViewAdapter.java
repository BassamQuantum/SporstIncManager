package com.example.bassam.sporstincmanger.CustomCalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.MyItemClickListener;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassam on 12/31/2017.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    private static final String TAG = GridViewAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private HashMap<String, List<classesEntity> > AllEvents = new HashMap<>();
    private String TodayDate ,SelectedDate;
    Context mContext;

    MyItemClickListener clickListener;

    public void setClickListener(MyItemClickListener clickListener) {
        Log.d(TAG,"In Setting Listener");
        this.clickListener = clickListener;
    }

    public GridViewAdapter(Context context, String TodayDate, String SelectedDate , List<Date> monthlyDates, Calendar currentDate, HashMap<String, List<classesEntity> > AllEvents) {

        this.TodayDate = TodayDate;
        this.SelectedDate = SelectedDate;
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.AllEvents = AllEvents;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_cell_layout, parent, false);

        return new ViewHolder(view,parent);
    }

    @Override
    public void onBindViewHolder(GridViewAdapter.ViewHolder viewHolder, int position) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(mDate);

        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        viewHolder.cellNumber.setText(String.valueOf(dayValue));

        if(displayMonth == currentMonth && displayYear == currentYear){
            viewHolder.cellNumber.setTextColor(Color.parseColor("#000000"));
        }else{
            viewHolder.cellNumber.setTextColor(Color.parseColor("#cccccc"));
        }
        if (TodayDate.equals(formattedDate))
            viewHolder.cellNumber.setTextColor(Color.parseColor("#932121"));

        if(AllEvents.get(formattedDate) != null) {
            //view.setBackgroundColor(Color.parseColor("#e0c5e7"));
            viewHolder.eventIndicator.setBackgroundColor(Color.parseColor("#932121"));
        }

        if(formattedDate.equals(SelectedDate)) {
            Log.d(TAG,SelectedDate+" : "+formattedDate);
            setSelectedItem(viewHolder.view, viewHolder.parent, position, -1);
        }
    }

    public void setSelectedItem(View holder,ViewGroup parent ,int position, int PreviousPostion){

        if (position == PreviousPostion)
            return;

        holder.setBackgroundColor(Color.parseColor("#cccccc"));
        holder.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.default_shape));

        Date mDate = monthlyDates.get(position);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(mDate);

        if(formattedDate.equals(TodayDate)){
            TextView cellNumber = (TextView)holder.findViewById(R.id.calendar_date_id);
            holder.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.today_shape));
            cellNumber.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if(PreviousPostion != -1){
            LinearLayout PrevView = (LinearLayout) parent.getChildAt(PreviousPostion);
            PrevView.setBackgroundDrawable(null);

            mDate = monthlyDates.get(PreviousPostion);
            formattedDate = df.format(mDate);

            if(formattedDate.equals(TodayDate)) {
                LinearLayout prevView = (LinearLayout) parent.getChildAt(PreviousPostion);
                TextView cellNumber = (TextView) prevView.findViewById(R.id.calendar_date_id);
                cellNumber.setTextColor(Color.parseColor("#932121"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return monthlyDates.size();
    }

    public Date getItem(int position) {
        return monthlyDates.get(position);
    }

    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

    public int getPosition(String Date){
        for(int i=0;i<monthlyDates.size();i++){
            Date mDate = monthlyDates.get(i);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(mDate);

            if (formattedDate.equals(Date))
                return i;
        }
        return  -1;
    }

    public Calendar getCurrentDate() {
        return currentDate;
    }
    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView eventIndicator;
        private TextView cellNumber;
        private View view;
        private ViewGroup parent;

        public ViewHolder(View view , ViewGroup parent) {
            super(view);
            this.view = view;
            this.parent = parent;
            cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
            eventIndicator = (TextView)view.findViewById(R.id.event_id);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG,"On Item Click Reycecle");
            if (clickListener != null){
                Log.d(TAG,"Not Null Listener");
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}