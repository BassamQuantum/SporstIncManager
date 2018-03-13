package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class CourseEntity implements Serializable {

    int id;
    int paid_count , trainees_count;
    String ImageUrl;
    String CourseName;
    String PaidPrecentage;
    Date StartDate;
    Date EndDate;
    String price;
    String level;
    String classes_Num;
    String description;
    List<GroupEntity> groups;

    public CourseEntity() {
    }

    public CourseEntity(String courseName, String paidPrecentage, Date startDate, Date endDate, List<GroupEntity> groups) {
        CourseName = courseName;
        PaidPrecentage = paidPrecentage;
        StartDate = startDate;
        EndDate = endDate;
        this.groups = groups;
    }

    public CourseEntity(String courseName, String paidPrecentage, Date startDate, Date endDate) {
        ImageUrl = "";
        CourseName = courseName;
        PaidPrecentage = paidPrecentage;
        StartDate = startDate;
        EndDate = endDate;
        this.groups = new ArrayList<>();
    }

    public CourseEntity(String courseName, Date startDate, Date endDate, String price, String level, String classes_Num, String description) {
        ImageUrl = "";
        CourseName = courseName;
        StartDate = startDate;
        EndDate = endDate;
        this.price = price;
        this.level = level;
        this.classes_Num = classes_Num;
        this.description = description;
    }

    public CourseEntity(JSONObject object){
        try {
            id = object.getInt("id");
            ImageUrl = object.getString("ImageUrl");
            CourseName = object.getString("name");
            String dateFormated =  object.getString("start_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            StartDate = formatter.parse(dateFormated);
            dateFormated =  object.getString("end_date");
            EndDate = formatter.parse(dateFormated);
            this.price = object.getString("price");
            this.level = object.getString("level");
            this.classes_Num = object.getString("no_of_classes");;
            this.description = object.getString("description");;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CourseEntity){
            CourseEntity entity = (CourseEntity) obj;
            if (entity.id == this.id)
                return true;
            return false;
        }
        return true;
    }

    public void initData(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("course_id");
            CourseName = jsonObject.getString("course_name");
            /*String dateFormated =  jsonObject.getString("start_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            StartDate = formatter.parse(dateFormated);
            dateFormated =  jsonObject.getString("end_date");
            EndDate = formatter.parse(dateFormated);*/
            paid_count = jsonObject.getInt("total_paid");
            trainees_count = jsonObject.getInt("total_trainees");
            float total = (paid_count*100.0f)/trainees_count;
            PaidPrecentage = String.format("%.0f",total)+" %";
        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getPaidPrecentage() {
        return PaidPrecentage;
    }

    public void setPaidPrecentage(String paidPrecentage) {
        PaidPrecentage = paidPrecentage;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public List<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupEntity> groups) {
        this.groups = groups;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClasses_Num() {
        return classes_Num;
    }

    public void setClasses_Num(String classes_Num) {
        this.classes_Num = classes_Num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
