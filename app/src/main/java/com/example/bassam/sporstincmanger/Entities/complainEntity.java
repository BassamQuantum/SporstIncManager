package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bassam on 12/25/2017.
 */

public class complainEntity implements Serializable {
    int personType;
    String personName,Title;
    Date date;
    String content;

    public complainEntity(int personType, String personName, Date date, String content) {
        this.personType = personType;
        this.personName = personName;
        this.Title = personName;
        this.date = date;
        this.content = content;
    }

    public complainEntity(JSONObject jsonObject) {
        try {
            this.personType = jsonObject.getInt("type");
            this.personName = jsonObject.getString("name");
            this.Title = jsonObject.getString("title");
            this.content = jsonObject.getString("Content");
            String dateFormated =  jsonObject.getString("c_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            this.date = formatter.parse(dateFormated);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
