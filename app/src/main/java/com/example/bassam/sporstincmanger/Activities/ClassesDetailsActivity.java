package com.example.bassam.sporstincmanger.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.CustomLoadingView;
import com.example.bassam.sporstincmanger.Entities.classesEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ClassesDetailsActivity extends AppCompatActivity {
    TextView  start ,end ,coach ,admin ,classname ,coursename, postpond ,reason;
    TextView groupName , PoolName;

    CustomLoadingView loadingView;
    Button postponedClass , cancelClass;
    LinearLayout buttons;
    int LoadingTime = 1200;
    PopupWindow popupWindow;
    private ProgressDialog progressDialog;
    private classesEntity myclass;

    private int result = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Class Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(ClassesDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        loadingView = findViewById(R.id.LoadingView);
        start = findViewById(R.id.class_start_Text);
        end = findViewById(R.id.class_end_Text);
        coach = findViewById(R.id.class_coach_Text);
        admin = findViewById(R.id.class_admin_Text);
        classname = findViewById(R.id.class_name_Text);
        coursename = findViewById(R.id.class_course_Text);
        groupName= findViewById(R.id.class_group_Text);
        PoolName = findViewById(R.id.class_pool_Text);
        postpond = findViewById(R.id.class_postpond_Text);
        reason = findViewById(R.id.class_reason_Text);

        buttons = findViewById(R.id.ChangeStatusButtons);
        cancelClass = findViewById(R.id.class_cancel_btn);
        postponedClass = findViewById(R.id.class_postpond_btn);

        myclass = (classesEntity) getIntent().getSerializableExtra(getString(R.string.Key_ClassEntity));

        cancelClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = 1;
                writeNote(result);
            }
        });

        postponedClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = 2;
                postpondClass();
            }
        });

        if (savedInstanceState != null)
            LoadingTime = 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            if (myclass != null)
                fillView(myclass);
            else
                loadingView.fails(); }
        }, LoadingTime);

    }

    private void dismissButtons(){
        buttons.setVisibility(View.GONE);
        popupWindow.dismiss();
    }

    private void cancelClass(String note) {
        try {
            JSONObject values = new JSONObject();
            values.put("status",1);
            values.put("class_notes",note);

            JSONObject where = new JSONObject();
            where.put("id",myclass.getClass_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_Table),getString(R.string.Table_classes));
            params.put(getString(R.string.parameter_where),where.toString());
            params.put(getString(R.string.parameter_values),values.toString());

            httpCall.setParams(params);

            progressDialog.show();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        show_toast(getResources().getString(R.string.session_has_been_cancelled));
                    }else {
                        show_toast(getResources().getString(R.string.failed_to_cancel_session));
                    }
                    progressDialog.dismiss();
                    dismissButtons();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (result.equals(getString(R.string.ResponseSuccess)))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void show_toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


    private void fillView(classesEntity myclass) {
        try {
            Calendar c = Calendar.getInstance();
            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            // and get that as a Date
            Date today = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(myclass.getClassdate());

            start.setText(myclass.getStartTime());
            end.setText(myclass.getEndTime());
            coach.setText(myclass.getCoachName());
            admin.setText(myclass.getAdminName());
            classname.setText(myclass.getClassName());
            coursename.setText(myclass.getCourseName());
            groupName.setText(myclass.getGroupName());
            PoolName.setText(myclass.getPoolName());
            if(myclass.getStatus().equals(getString(R.string.StatusCanceled))){
                LinearLayout reasonlayout = findViewById(R.id.canceled_reason);
                reasonlayout.setVisibility(View.VISIBLE);
                reason.setText(myclass.getReason());
                buttons.setVisibility(View.GONE);
            }
            else if(myclass.getStatus().equals(getString(R.string.StatusPostponed))){
                LinearLayout postpondlayout = findViewById(R.id.postponded_Time);
                postpondlayout.setVisibility(View.VISIBLE);
                postpond.setText(myclass.getPostpondDate()+"\n\t"+myclass.getPostpondStartTime()+" ~ "+myclass.getPostpondEndTime());
                LinearLayout reasonlayout = findViewById(R.id.canceled_reason);
                reasonlayout.setVisibility(View.VISIBLE);
                reason.setText(myclass.getReason());
                buttons.setVisibility(View.GONE);
            }
            else if (myclass.getStatus().equals(getString(R.string.StatusFinished))){
                LinearLayout noteLayout = findViewById(R.id.canceled_reason);
                noteLayout.setVisibility(View.VISIBLE);

                TextView noteLable = findViewById(R.id.class_reason_Lable);
                noteLable.setText(getString(R.string.SessionNote));
                reason.setText(myclass.getReason());
                buttons.setVisibility(View.GONE);
            }
            else if (date.before(today))
                buttons.setVisibility(View.GONE);

            loadingView.success();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (result != -1) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(getString(R.string.SessionStatus), result);
            setResult(AppCompatActivity.RESULT_OK, returnIntent);
        }
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void writeNote(final int status){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_write_note_layout,null);

        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }

        final EditText note_edit_text =  customView.findViewById(R.id.noteEditText_notewindow);
        Button done_button =  customView.findViewById(R.id.doneButton_notewindow);

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String note = note_edit_text.getText().toString();
                switch (status){
                    case 1:
                        cancelClass(note);
                        break;
                    case 2:
                        savePostponedTime(note);
                        break;
                }
            }
        } );

        LinearLayout parentView = findViewById(R.id.class_view);
        popupWindow.showAtLocation(parentView, Gravity.CENTER,0,0);
        popupWindow.setFocusable(true);
        note_edit_text.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.update();
    }

    int Year ,Month ,Day ,Hour ,Minute;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private void postpondClass(){
        Calendar calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Minute = calendar.get(Calendar.MINUTE);
        initialDateListener();
        initialTimeListener();
        myDatePicker();
    }

    private void initialDateListener() {
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Year = year;
                Month = month+1;
                Day = day;

                myTimePicker();
            }
        };
    }

    private void initialTimeListener() {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Hour = hour;
                Minute = minute;
                writeNote(2);
            }

        };
    }

    private void myDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ClassesDetailsActivity.this, dateSetListener,Year,Month,Day);
        datePickerDialog.show();
    }

    private void myTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(ClassesDetailsActivity.this, timeSetListener ,Hour ,Minute ,true);
        timePickerDialog.show();
    }

    private Date showPostponedTime() {
        Calendar dateCal = Calendar.getInstance();
        dateCal.set(Calendar.YEAR,Year);
        dateCal.set(Calendar.MONTH,Month);
        dateCal.set(Calendar.DAY_OF_MONTH,Day);
        dateCal.set(Calendar.HOUR,Hour);
        dateCal.set(Calendar.MINUTE,Minute);

        return dateCal.getTime();
    }

    private void savePostponedTime(String note) {
        try {
            Date PostponeDate = showPostponedTime();

            SimpleDateFormat df = new SimpleDateFormat(getString(R.string.server_dateTimeFormate), Locale.ENGLISH);
            String date = df.format(PostponeDate);
            final String msg = myclass.getClassName()+getString(R.string.Postpone_msg)+date;

            df = new SimpleDateFormat(getString(R.string.server_dateFormate), Locale.ENGLISH);
            String postponedDate = df.format(PostponeDate);
            df = new SimpleDateFormat(getString(R.string.TimeFormate), Locale.ENGLISH);
            String postponedTime = df.format(PostponeDate);

            JSONObject values = new JSONObject();
            values.put(getString(R.string.select_sessionStatus),2);
            values.put(getString(R.string.select_sessionPostponeDate),postponedDate);
            values.put(getString(R.string.select_sessionPostponeTime),postponedTime);
            values.put(getString(R.string.select_sessionNote),note);

            JSONObject where = new JSONObject();
            where.put(getString(R.string.select_sessionID),myclass.getClass_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);

            final HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_Table),getString(R.string.Table_classes));
            params.put(getString(R.string.parameter_where),where.toString());
            params.put(getString(R.string.parameter_values),values.toString());

            httpCall.setParams(params);

            progressDialog.show();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        show_toast(msg);
                    }else {
                        show_toast(getResources().getString(R.string.failed_to_postpone_session));
                    }
                    progressDialog.dismiss();
                    dismissButtons();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
