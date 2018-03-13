package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bassam on 12/25/2017.
 */

public class requestsEntity implements Serializable {
    String subject;
    Date date;
    String personType;
    String person;
    String status;
    int state;
    String content;
    String course;
    String group;
    String creation_date;

    int request_ID ;

    public requestsEntity() {
    }

    public requestsEntity(String subject, Date date, String personType, String person, String status, String content, String course) {
        this.subject = subject;
        this.date = date;
        this.personType = personType;
        this.person = person;
        this.status = status;
        this.content = content;
        this.course = course;
    }

    public requestsEntity(String subject, Date date, String personType, String person, String status, String content, String course, String group) {
        this.subject = subject;
        this.date = date;
        this.personType = personType;
        this.person = person;
        this.status = status;
        this.content = content;
        this.course = course;
        this.group = group;
    }
    public requestsEntity(JSONObject object){
        try {
            request_ID = object.getInt("requests_id");
            Date date;
            DateFormat outdateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            DateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.creation_date = object.getString("c_date");
            date = creationDateFormat.parse(creation_date);
            this.creation_date = outdateFormat.format(date);

            this.subject = object.getString("title");
            String dateFormated =  object.getString("date_request");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            this.date = formatter.parse(dateFormated);
            this.personType = object.getString("type");;
            this.person = object.getString("name");
            state = object.getInt("status");
            status = "";
            if(state == 0)
                this.status = "Rejected";
            else if (state == 1)
                this.status = "Accepted";

            this.content = object.getString("content");;
            this.course = object.getString("course_name");;
            this.group = object.getString("sub_course_name");;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getRequest_ID() {
        return request_ID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getState() {
        return state;
    }

    public String getCreation_date() {
        return creation_date;
    }

    @Override
    public String toString() {
        return this.course+" "+this.getGroup()+" "+this.state;
    }
}
