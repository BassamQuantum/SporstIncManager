package com.example.bassam.sporstincmanger.Entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bassam on 12/25/2017.
 */

public class NotificationEntity implements Serializable {
    String subject ,content ,date ,from_name ,to_name ;

    int from_id ,to_id ,read , notify_type , updated , type_id , notification_id;

    Date notification_date;

    public NotificationEntity() {
    }

    public NotificationEntity(JSONObject jsonObject) {
        try {
            this.notification_id = jsonObject.getInt("notification_id");
            this.subject = jsonObject.getString("title");
            this.content = jsonObject.getString("message");
            this.date = jsonObject.getString("cTime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            notification_date = df.parse(date);
            df = new SimpleDateFormat("MMM dd, yyyy, h:mm a");
            this.date = df.format(notification_date);
            this.from_name = jsonObject.getString("From_name");
            this.from_id = jsonObject.getInt("from_id");
            this.to_name = jsonObject.getString("to_name");
            this.to_id = jsonObject.getInt("to_id");
            this.notify_type = jsonObject.getInt("type");
            this.type_id = jsonObject.getInt("type_id");
            this.read = jsonObject.getInt("notify_read");
            this.updated = jsonObject.getInt("notify_updated");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from_name;
    }

    public void setFrom(String from) {
        this.from_name = from;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(int notify_type) {
        this.notify_type = notify_type;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public Date getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(Date notification_date) {
        this.notification_date = notification_date;
    }

    public int getNotify_id() {
        return notification_id;
    }

    public void setNotify_id(int notify_id) {
        this.notification_id = notify_id;
    }

}
