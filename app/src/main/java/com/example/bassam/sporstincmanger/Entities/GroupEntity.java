package com.example.bassam.sporstincmanger.Entities;

import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class GroupEntity implements Serializable {

    String groupName , courseName,CoachName ,AdminName ,PoolName ,AttendacePrecentage;
    int group_id ,course_id ,coach_id ,admin_id ,pool_id;
    int TraineeNum ,PaidNum , attend_count , no_classes;

    List<AttendanceEntity> classes;
    private int coach_attend;
    private String CoachAttendace;

    public GroupEntity() {
    }

    public GroupEntity(String groupName, int traineeNum, int paidNum) {
        this.groupName = groupName;
        TraineeNum = traineeNum;
        PaidNum = paidNum;
        classes = new ArrayList<>();
    }

    public GroupEntity(JSONObject jsonObject) {
        try {
            group_id = jsonObject.getInt("id");
            groupName = jsonObject.getString("name");
            course_id = jsonObject.getInt("course_id");
            coach_id = jsonObject.getInt("coach_id");
            admin_id = jsonObject.getInt("admin_id");
            pool_id = jsonObject.getInt("pool_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GroupEntity(int InitiNum, JSONObject jsonObject){
        switch (InitiNum){
            case 1:
                initData(jsonObject);
                break;
            case 2:
                initData2(jsonObject);
                break;
        }
    }

    private void initData(JSONObject jsonObject) {
        try {
            group_id = jsonObject.getInt("group_id");
            groupName = jsonObject.getString("group_name");
            course_id = jsonObject.getInt("course_id");
            courseName = jsonObject.getString("course_name");
            coach_id = jsonObject.getInt("coach_id");
            CoachName = jsonObject.getString("coach_name");
            admin_id = jsonObject.getInt("admin_id");
            AdminName = jsonObject.getString("admin_name");
            PoolName = jsonObject.getString("pool_name");
            TraineeNum = jsonObject.getInt("Trainee_count");
            String count = jsonObject.getString("class_attent");
            if (count!=null && !count.equals("null"))
                attend_count = Integer.parseInt(count);
            no_classes = jsonObject.getInt("no_of_classes");
            float totalAttend = (float) attend_count / (TraineeNum *no_classes);
            totalAttend*=100;
            AttendacePrecentage = ""+String.format("%.0f",totalAttend)+" %";

            count = jsonObject.getString("coach_attent");
            if (count!=null && !count.equals("null"))
                coach_attend = Integer.parseInt(count);
            totalAttend = (float) coach_attend / no_classes;
            totalAttend*=100;
            CoachAttendace = ""+String.format("%.0f",totalAttend)+" %";
            //Log.d("Attend","ID : "+group_id+" count String:"+count+" counter:"+attend_count+" Trainees: "+TraineeNum+" no.classes: "+no_classes+" Precent: "+AttendacePrecentage+" , "+totalAttend);
            classes = new ArrayList<>();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData2(JSONObject jsonObject) {
        try {
            group_id = jsonObject.getInt("group_id");
            groupName = jsonObject.getString("group_name");
            course_id = jsonObject.getInt("course_id");
            courseName = jsonObject.getString("course_name");
            TraineeNum = jsonObject.getInt("Trainee_count");
            PaidNum = jsonObject.getInt("payment_count");
            classes = new ArrayList<>();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroupEntity){
            GroupEntity entity = (GroupEntity) obj;
            if (entity.group_id == this.group_id)
                return true;
            return false;
        }
        return true;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public int getPool_id() {
        return pool_id;
    }

    public void setPool_id(int pool_id) {
        this.pool_id = pool_id;
    }

    public List<AttendanceEntity> getClasses() {
        return classes;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }

    public String getPoolName() {
        return PoolName;
    }

    public void setPoolName(String poolName) {
        PoolName = poolName;
    }

    public void setClasses(List<AttendanceEntity> classes) {
        this.classes = classes;
    }

    public String getCoachName() {
        return CoachName;
    }

    public void setCoachName(String coachName) {
        CoachName = coachName;
    }

    public String getAttendacePrecentage() {
        return AttendacePrecentage;
    }

    public void setAttendacePrecentage(String attendacePrecentage) {
        AttendacePrecentage = attendacePrecentage;
    }

    public String getName() {
        return groupName;
    }

    public void setName(String name) {
        groupName = name;
    }

    public int getTraineeNum() {
        return TraineeNum;
    }

    public void setTraineeNum(int traineeNum) {
        TraineeNum = traineeNum;
    }

    public int getPaidNum() {
        return PaidNum;
    }

    public void setPaidNum(int paidNum) {
        PaidNum = paidNum;
    }

    public String getCoachAttend() {
        return CoachAttendace;
    }

    public String getCourseName() {
        return courseName;
    }
}
