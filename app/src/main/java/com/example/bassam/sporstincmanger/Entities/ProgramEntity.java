package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ProgramEntity implements Serializable {
    int id;
    String name , imgURL;

    public ProgramEntity(int id, String name, String imgURL) {
        this.id = id;
        this.name = name;
        this.imgURL = imgURL;
    }

    public ProgramEntity(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.imgURL = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        imgURL = imgURL;
    }
}
