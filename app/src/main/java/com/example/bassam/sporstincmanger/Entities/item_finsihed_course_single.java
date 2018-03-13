package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bassam on 12/3/2018.
 */

public class item_finsihed_course_single implements Serializable {

    String class_name, class_date,coach_note , attendance_percentage;
    int class_id;

    public item_finsihed_course_single(String class_number, String class_date, String attendance_percentage) {
        this.class_name = class_number;
        this.class_date = class_date;
        this.attendance_percentage = attendance_percentage;
    }

    public item_finsihed_course_single(JSONObject jsonObject) {
        try {
            class_id = jsonObject.getInt("class_id");
            class_name = "class" + jsonObject.getString("class_name");
            coach_note = jsonObject.getString("class_note");
            String dateformate = jsonObject.getString("class_date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date myDate = df.parse(dateformate);
            df = new SimpleDateFormat("dd/MM/yyyy");
            class_date = df.format(myDate);

            int totalTrainee = jsonObject.getInt("total_trainee");
            int totalAttend = jsonObject.getInt("Attended_trainee");

            float precent = (totalAttend * 100.0f)/totalTrainee;

            attendance_percentage = ""+String.format("%.0f",precent)+" %";
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_date() {
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public String getCoach_note() {
        return coach_note;
    }

    public void setCoach_note(String coach_note) {
        this.coach_note = coach_note;
    }

    public String getAttendance_percentage() {
        return attendance_percentage;
    }

    public void setAttendance_percentage(String attendance_percentage) {
        this.attendance_percentage = attendance_percentage;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }
}
