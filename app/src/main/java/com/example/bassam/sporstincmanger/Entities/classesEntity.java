package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bassam on 12/24/2017.
 */

public class classesEntity implements Serializable {
    String courseName, groupName, className, status, coachName, adminName, poolName;
    String startTime, endTime, reason, postpondStartTime, postpondEndTime , postpondDate ,classdate;
    int class_id ,Group_id ,Course_id ,Admin_id ,Coach_id , state;

    public classesEntity() {
    }

    public classesEntity(JSONObject jsonObject) {
        try {
            this.class_id = jsonObject.getInt("class_id");
            this.className = "Session "+jsonObject.getInt("class_number");
            String dateFormated = jsonObject.getString("class_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.classdate = formatter.format(date);
            this.courseName = jsonObject.getString("Courses_name");
            this.groupName = jsonObject.getString("Groups_Name");
            state = jsonObject.getInt("status");
            switch (state) {
                case 0:
                    this.status = "Running";
                    break;
                case 1:
                    this.status = "Canceled";
                    break;
                case 2:
                    this.status = "Postponed";
                    break;
                case 3:
                    this.status = "UpComing";
                    break;
                case 4:
                    this.status = "Finished";
                    break;

            }
            this.Coach_id = jsonObject.getInt("coach_id");
            this.coachName = jsonObject.getString("coach_name");
            this.Admin_id = jsonObject.getInt("admin_id");
            this.adminName = jsonObject.getString("admin_name");
            this.poolName = jsonObject.getString("Pool_Name");
            this.startTime = jsonObject.getString("class_time");
            this.startTime = startTime.substring(0,startTime.length()-3);
            this.endTime= jsonObject.getString("class_end_time");
            this.endTime = endTime.substring(0,endTime.length()-3);
            this.reason = jsonObject.getString("class_notes");
            dateFormated = jsonObject.getString("postpone_date");
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.postpondDate = formatter.format(date);
            this.postpondStartTime = jsonObject.getString("postpone_start_time");
            postpondStartTime = postpondStartTime.substring(0,postpondStartTime.length()-3);
            this.postpondEndTime = jsonObject.getString("postpone_end_time");
            postpondEndTime = postpondEndTime.substring(0,postpondEndTime.length()-3);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public classesEntity(classesEntity entity) {
        this.class_id = entity.class_id;
        this.className = entity.className;
        this.classdate = entity.postpondDate;
        this.courseName = entity.courseName;
        this.groupName = entity.groupName;
        this.state = entity.state;
        this.status = "UpComing";
        this.Coach_id = entity.Coach_id;
        this.coachName = entity.coachName;
        this.Admin_id = entity.Admin_id;
        this.adminName = entity.adminName;
        this.poolName = entity.poolName;
        this.startTime = entity.postpondStartTime;
        this.endTime= entity.postpondEndTime;
        this.reason = entity.reason;
        this.postpondDate = entity.postpondDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof classesEntity){
            classesEntity entity = (classesEntity) obj;
            if (this.class_id == entity.class_id)
                return true;
            return false;
        }
        return true;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPostpondDate() {
        return postpondDate;
    }

    public void setPostpondDate(String postpondDate) {
        this.postpondDate = postpondDate;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getGroup_id() {
        return Group_id;
    }

    public void setGroup_id(int group_id) {
        Group_id = group_id;
    }

    public int getCourse_id() {
        return Course_id;
    }

    public void setCourse_id(int course_id) {
        Course_id = course_id;
    }

    public int getAdmin_id() {
        return Admin_id;
    }

    public void setAdmin_id(int admin_id) {
        Admin_id = admin_id;
    }

    public int getCoach_id() {
        return Coach_id;
    }

    public void setCoach_id(int coach_id) {
        Coach_id = coach_id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPostpondStartTime() {
        return postpondStartTime;
    }

    public void setPostpondStartTime(String postpondStartTime) {
        this.postpondStartTime = postpondStartTime;
    }

    public String getPostpondEndTime() {
        return postpondEndTime;
    }

    public void setPostpondEndTime(String postpondEndTime) {
        this.postpondEndTime = postpondEndTime;
    }

    public String getClassdate() {
        return classdate;
    }

    public void setClassdate(String classdate) {
        this.classdate = classdate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case 0:
                this.status = "Running";
                break;
            case 1:
                this.status = "Canceled";
                break;
            case 2:
                this.status = "Postponed";
                break;
            case 3:
                this.status = "UpComing";
                break;
            case 4:
                this.status = "Finished";
                break;

        }
    }
}
