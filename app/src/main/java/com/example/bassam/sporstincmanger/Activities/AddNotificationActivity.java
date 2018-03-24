package com.example.bassam.sporstincmanger.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.CustomView.MultiSelectSpinner;
import com.example.bassam.sporstincmanger.CustomView.MultiSelectionSpinner;
import com.example.bassam.sporstincmanger.Entities.CourseEntity;
import com.example.bassam.sporstincmanger.Entities.GroupEntity;
import com.example.bassam.sporstincmanger.Entities.UserEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.example.bassam.sporstincmanger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;

public class AddNotificationActivity extends AppCompatActivity  implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    static private String TAG = AddNotificationActivity.class.getSimpleName();

    GlobalVars globalVars;
    int currentType;

    MaterialBetterSpinner SpinnerTo  ,SpinnerCourse ,SpinnerGroup;
    EditText Subject ,Content;

    ArrayList<String> CoursesList, GroupList ;
    List<String> ToList ;

    // Entities for filtering selections...
    ArrayList<CourseEntity> courseEntities;
    ArrayList<UserEntity> userEntities;
    ArrayList<GroupEntity> groupEntities;

    int ReceiverID = 0 ;
    ArrayList<Integer> To_IDs ;
    List<Integer> GroupPositions ,UsersPostions;
    HashMap<Integer,List<Integer> > CoursePerson , CourseGroup ,GroupTrainee;
    private MultiSelectionSpinner SpinnerToAction;

    ArrayAdapter<String> ToAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Compose");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        globalVars = (GlobalVars) getApplication();

        currentType = getIntent().getIntExtra("CurrentItem",0);

        if(currentType == 0)
            currentType = 2;
        else if (currentType == 2)
            currentType = 0;

        SpinnerCourse = findViewById(R.id.NewRequest_Course);
        SpinnerGroup = findViewById(R.id.NewRequest_Group);
        SpinnerTo = findViewById(R.id.NewRequest_To);
        Subject = findViewById(R.id.inputRequest_subject);
        Content = findViewById(R.id.inputRequest_Message);

        GroupPositions = new ArrayList<>();
        UsersPostions = new ArrayList<>();

        CoursePerson = new HashMap<>();
        CourseGroup = new HashMap<>();
        GroupTrainee = new HashMap<>();

        ToList = new ArrayList<>();
        CoursesList = new ArrayList<>();
        GroupList = new ArrayList<>();

        To_IDs = new ArrayList<>();

        ArrayAdapter<String> CoursesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,CoursesList);
        ArrayAdapter<String> GroupAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,GroupList);
         ToAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ToList);

        SpinnerCourse.setAdapter(CoursesAdapter);
        SpinnerTo.setAdapter(ToAdapter);
        SpinnerGroup.setAdapter(GroupAdapter);
        SpinnerToAction = findViewById(R.id.NewRequest_ToAction);
        SpinnerToAction.setItems(ToList);
        SpinnerToAction.setListener(this);


        SpinnerTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    SpinnerToAction.performClick();
                return false;
            }
        });
        initilizeToSpinner();
        initilizeCoursesSpinner();
        initilizeGroupSpinner();
        initilizeGroupTrainee();

        if(currentType == 0)
            SpinnerGroup.setVisibility(View.VISIBLE);


        SpinnerCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(currentType == 0) {
                    SpinnerGroup.setText("");
                    groupFilter(CourseGroup.get(courseEntities.get(position).getId()));
                }
                else {
                    SpinnerTo.setText("");
                    usersFilter(CoursePerson.get(courseEntities.get(position).getId()));
                }
            }
        });

        SpinnerGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SpinnerTo.setText("");
                usersFilter(GroupTrainee.get(GroupPositions.get(position)));
            }
        });

        SpinnerTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ReceiverID = UsersPostions.get(position);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send_message){
            sendRequest();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void groupFilter(List<Integer> selectedIDs) {
        GroupList.clear();
        GroupPositions.clear();
        if(selectedIDs != null){
            for (int i = 0; i < selectedIDs.size(); i++) {
                for (int j = 0; j <groupEntities .size(); j++) {
                    GroupEntity entity = groupEntities.get(j);
                    if (entity.getGroup_id() == selectedIDs.get(i)) {
                        GroupList.add(entity.getName());
                        GroupPositions.add(entity.getGroup_id());
                        break;
                    }
                }
            }
        }
        if (GroupList.size() == 0)
            GroupList.add("");

        ArrayAdapter<String> GroupAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,GroupList);
        SpinnerGroup.setAdapter(GroupAdapter);

    }

    private void usersFilter(List<Integer> selectedIDs){
        ToList.clear();
        UsersPostions.clear();
        if(selectedIDs != null) {
            for (int i = 0; i < selectedIDs.size(); i++) {
                for (int j = 0; j < userEntities.size(); j++) {
                    UserEntity entity = userEntities.get(j);
                    if (entity.getId() == selectedIDs.get(i)) {
                        ToList.add(entity.getName());
                        UsersPostions.add(entity.getId());
                        break;
                    }
                }
            }
        }
        /*if(ToList.size() == 0){
            ToList.add("");
        }
        ArrayAdapter<String> UserAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ToList);
        SpinnerTo.setAdapter(UserAdapter);*/
        SpinnerToAction.setItems(ToList);
    }

    private void initilizeCoursesSpinner(){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","courses");

        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillCoursesAdapter(response);
            }
        }.execute(httpCall);

    }

    private void fillCoursesAdapter(JSONArray response) {
        if(response != null){
            try {
                CoursesList.clear();

                courseEntities = new ArrayList<>();

                for(int i=0;i<response.length();i++){
                    CourseEntity entity =new CourseEntity(response.getJSONObject(i));
                    courseEntities.add(entity);
                    CoursesList.add(entity.getCourseName());
                }

                if(CoursesList.size() == 0){
                    CoursesList.add("");
                }

                ArrayAdapter<String> CourseAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,CoursesList);
                SpinnerCourse.setAdapter(CourseAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initilizeGroupSpinner(){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String, String> params = new HashMap<>();
        params.put("table", "groups");

        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillGroupAdapter(response);
            }
        }.execute(httpCall);

    }

    private void fillGroupAdapter(JSONArray response) {
        if(response != null){
            try {
                GroupList.clear();
                GroupPositions.clear();
                CoursePerson.clear();
                CourseGroup.clear();
                groupEntities = new ArrayList<>();

                for(int i=0;i<response.length();i++){
                    GroupEntity entity =new GroupEntity(response.getJSONObject(i));

                    List<Integer> Groups = CourseGroup.get(entity.getCourse_id());
                    List<Integer> Persons = CoursePerson.get(entity.getCourse_id());

                    if (Groups == null)
                        Groups = new ArrayList<Integer>();

                    Groups.add(entity.getGroup_id());

                    if(Persons == null)
                        Persons = new ArrayList<>();

                    if (currentType == 1)
                        Persons.add(entity.getCoach_id());
                    else if (currentType == 2)
                        Persons.add(entity.getAdmin_id());

                    Set<Integer> hs = new HashSet<>();
                    hs.addAll(Persons);
                    Persons.clear();
                    Persons.addAll(hs);

                    CoursePerson.put(entity.getCourse_id(),Persons);
                    CourseGroup.put(entity.getCourse_id(),Groups);

                    groupEntities.add(entity);
                    GroupPositions.add(entity.getGroup_id());
                    GroupList.add(entity.getName());
                }

                if(GroupList.size() == 0){
                    GroupList.add("");
                }
                ArrayAdapter<String> UserAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,GroupList);
                SpinnerGroup.setAdapter(UserAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void initilizeToSpinner(){
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("type",currentType);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String, String> params = new HashMap<>();
            params.put("table", "users");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillToAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillToAdapter(JSONArray response) {
        if(response != null){
            try {
                ToList.clear();
                UsersPostions.clear();

                userEntities = new ArrayList<>();

                for(int i=0;i<response.length();i++){
                    UserEntity entity =new UserEntity(response.getJSONObject(i));
                    userEntities.add(entity);
                    UsersPostions.add(entity.getId());
                    ToList.add(entity.getName());
                }

                /*if(ToList.size() == 0){
                    ToList.add("");
                }
                *///ArrayAdapter<String> UserAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ToList);
                //SpinnerTo.setAdapter(UserAdapter);

                SpinnerToAction.setItems(ToList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initilizeGroupTrainee(){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","group_trainee");

        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillGroupTraineeMap(response);
            }
        }.execute(httpCall);

    }

    private void fillGroupTraineeMap(JSONArray response) {
        Log.d(TAG,String.valueOf(response));
        if(response != null){
            try {
                GroupTrainee.clear();
                int Group_ID , Trainee_ID;

                for(int i=0;i<response.length();i++){
                    JSONObject object = response.getJSONObject(i);
                    Group_ID = object.getInt("group_id");
                    Trainee_ID = object.getInt("trainee_id");

                    List<Integer> Trainees = GroupTrainee.get(Group_ID);
                    if (Trainees == null)
                        Trainees = new ArrayList<>();

                    Trainees.add(Trainee_ID);

                    GroupTrainee.put(Group_ID,Trainees);
                }
                Log.d(TAG,GroupTrainee.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRequest() {
        JSONObject values = new JSONObject();
        try {
            show_toast("sending...");
            String title = Subject.getText().toString();
            String Message = Content.getText().toString();

            values.put("title",title);
            values.put("message",Message);
            values.put("course_name",SpinnerCourse.getText().toString());
            values.put("sub_course_name",SpinnerGroup.getText().toString());
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String Today = df.format(c.getTime());
            values.put("date_request",Today);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertNotify);
            HashMap<String,String> params = new HashMap<>();

            params.put("from_id",String.valueOf(globalVars.getId()) );
            params.put("multi","true");
            params.put("to_id",String.valueOf(To_IDs) );
            //params.put("to_id",String.valueOf(ReceiverID) );
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response!=null) {
                            String result = String.valueOf(response.get(0));
                            Log.d(TAG, result);
                            if (result.equals("ERROR"))
                                show_toast("Failed to send");
                            else
                                show_toast("Successfully sent");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void show_toast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
        To_IDs.clear();
        for (int i =0 ;i < indices.size() ;i++){
            To_IDs.add(UsersPostions.get(indices.get(i)) );
        }
    }

    @Override
    public void selectedStrings(List<String> strings) {
        String selectedTo = String.valueOf(strings);
        selectedTo = selectedTo.substring(1,selectedTo.length()-1);
        selectedTo =" "+selectedTo+" ";
        SpinnerTo.setText(selectedTo);
    }
}
