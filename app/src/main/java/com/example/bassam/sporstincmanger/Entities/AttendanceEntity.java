package com.example.bassam.sporstincmanger.Entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Bassam on 12/26/2017.
 */

public class AttendanceEntity implements Serializable {
    String className,ClassDate , coachNote ;
    int classId, group_Id, Attend_Num;
    float Atten_precent;

    public AttendanceEntity() {
    }

    public AttendanceEntity(String className, String date, int attend_Num) {
        this.className = className;
        ClassDate = date;
        Attend_Num = attend_Num;
    }

    public AttendanceEntity(JSONObject jsonObject , int totalNum) {
        try {
            classId = jsonObject.getInt("id");
            className = "Session "+ jsonObject.getString("class_number");
            coachNote = jsonObject.getString("class_notes");
            String dateFormated = jsonObject.getString("class_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            ClassDate = formatter.format(date);
            Attend_Num = jsonObject.getInt("class_attent");
            Log.d("AttendanceClass","Total: "+totalNum+" ,"+Attend_Num);
            Atten_precent = (Attend_Num * 100.0f) / totalNum;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttendanceEntity){
            AttendanceEntity entity = (AttendanceEntity) obj;
            if (entity.classId == this.classId)
                return true;
            return false;
        }
        return true;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return ClassDate;
    }

    public void setDate(String date) {
        ClassDate = date;
    }

    public int getAttend_Num() {
        return Attend_Num;
    }

    public void setAttend_Num(int attend_Num) {
        Attend_Num = attend_Num;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public float getAtten_precent() {
        return Atten_precent;
    }

    public void setAtten_precent(float atten_precent) {
        Atten_precent = atten_precent;
    }

    public String getCoach_note() {
        return coachNote;
    }
}
