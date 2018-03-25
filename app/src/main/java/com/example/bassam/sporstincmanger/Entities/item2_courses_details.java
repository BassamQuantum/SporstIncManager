package com.example.bassam.sporstincmanger.Entities;

/**
 * Created by Bassam on 25/3/2018.
 */

public class item2_courses_details {
    String coach_name;
    String[] day, time;


    public item2_courses_details(String coach_name, String[] day, String[]time) {
        this.coach_name = coach_name;
        this.day = day;
        this.time = time;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public String[] getDay() {
        return day;
    }

    public String[] getTime() {
        return time;
    }
}
