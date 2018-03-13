package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bassam on 1/3/2018.
 */

public class NewsEntity implements Serializable {
    String Content, img;

    public NewsEntity() {
    }

    public NewsEntity(String content, String img) {
        this.Content = content;
        this.img = img;
    }


    public NewsEntity(JSONObject object) {
        try {
            Content = object.getString("Content");
            img = object.getString("imgUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
