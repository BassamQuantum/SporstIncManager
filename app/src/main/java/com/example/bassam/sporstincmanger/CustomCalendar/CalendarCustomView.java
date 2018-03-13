package com.example.bassam.sporstincmanger.CustomCalendar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.Activities.ClassesDetailsActivity;
import com.example.bassam.sporstincmanger.CustomView.ItemOffsetDecoration;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.MyItemClickListener;
import com.example.bassam.sporstincmanger.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bassam on 12/27/2017.
 */

public class CalendarCustomView extends LinearLayout {
    private static final String TAG = CalendarCustomView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private RecyclerView calendarGridView;
    public RecyclerView calendarEvents;

    private static final int MAX_CALENDAR_COLUMN = 42;

    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private GridViewAdapter mAdapter;
    private HashMap<String, List<classesEntity> > MyEvents;
    public String SelectedDate, Today;
    private Calendar SelectedDay;
    private int PreviousPostion ;
    private MyItemClickListener EventsListener ;

    public CalendarCustomView(Context context) {
        super(context);
    }
    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        MyEvents = new HashMap<>();

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Today = df.format(c.getTime());
        SelectedDate =Today;
        PreviousPostion = -1;

        SelectedDay = c;

        Log.d(TAG,"In Constructor");

        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();

    }

    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HashMap<String, List<classesEntity>> getMyEvents() {
        return MyEvents;
    }

    public void setMyEvents(HashMap<String, List<classesEntity>> myEvents) {
        MyEvents = myEvents;
        setUpCalendarAdapter();
        setUpEventsAapter(SelectedDate);
    }

    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        calendarGridView = (RecyclerView)view.findViewById(R.id.calendar_grid);
        calendarEvents = (RecyclerView)view.findViewById(R.id.calendar_events);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        calendarEvents.setLayoutManager(layoutManager);
        calendarEvents.setNestedScrollingEnabled(true);
        calendarEvents.setItemAnimator(new DefaultItemAnimator());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),7);
        calendarGridView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        calendarGridView.addItemDecoration(itemDecoration);
        calendarGridView.setNestedScrollingEnabled(true);
    }

    private void UpdateCalendar(int i){
        cal.add(Calendar.MONTH, i);
        setUpCalendarAdapter();
    }
    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedDay.add(Calendar.MONTH,-1);
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                SelectedDate = df.format(SelectedDay.getTime());
                UpdateCalendar(-1);
            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedDay.add(Calendar.MONTH,1);
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                SelectedDate = df.format(SelectedDay.getTime());
                UpdateCalendar(1);
            }
        });
    }

    private void setUpCalendarAdapter(){

        List<Date> dayValueInCells = new ArrayList<Date>();

        Calendar mCal = (Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        // Set time fields to zero
        mCal.set(Calendar.HOUR_OF_DAY, 0);
        mCal.set(Calendar.MINUTE, 0);
        mCal.set(Calendar.SECOND, 0);
        mCal.set(Calendar.MILLISECOND, 0);
        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            Log.d(TAG,"  => "+mCal.getTime());
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }


        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new GridViewAdapter(context, Today, SelectedDate ,dayValueInCells, cal, MyEvents);
        calendarGridView.setAdapter(mAdapter);
        mAdapter.setClickListener(new MyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                calendarItemClick(view,position);
            }
        });

        PreviousPostion = mAdapter.getPosition(SelectedDate);

        setUpEventsAapter(SelectedDate);
    }

    public void setEventsListener(MyItemClickListener listener){
        EventsListener = listener;
    }

    public void setUpEventsAapter(String Mydate){
        List<classesEntity> classesList = MyEvents.get(Mydate);

        if(classesList == null)
            classesList = new ArrayList<>();
        ListViewAdapter adapter = new ListViewAdapter(getContext(),R.layout.classes_list_items,classesList);

        calendarEvents.setAdapter(adapter);
        if (EventsListener != null)
            adapter.setClickListener(EventsListener);
    }




    public void calendarItemClick(View view, int position) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mAdapter.getItem(position));
        int CurrentMonth = mAdapter.getCurrentDate().get(Calendar.MONTH);
        int SelectedMonth = dateCal.get(Calendar.MONTH);
        int CurrentYear = mAdapter.getCurrentDate().get(Calendar.YEAR);
        int SelectedYear = dateCal.get(Calendar.YEAR);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SelectedDate = df.format(dateCal.getTime());
        setUpEventsAapter(SelectedDate);
        SelectedDay.setTime(mAdapter.getItem(position));

        if(SelectedYear > CurrentYear || (SelectedYear == CurrentYear &&CurrentMonth < SelectedMonth) ){
            UpdateCalendar(1);
        }
        else if (SelectedYear < CurrentYear || CurrentMonth > SelectedMonth) {
            UpdateCalendar(-1);
        }
        else {
            mAdapter.setSelectedItem(view, (ViewGroup) view.getParent(),position,PreviousPostion);
            PreviousPostion = position;
        }
    }
}
