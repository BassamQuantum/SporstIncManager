package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 12/3/2018.
 */

public class item_trainee_attendance {
    String name;
    int trainee_id;
    boolean attended;

    public item_trainee_attendance(String name, boolean attended) {
        this.name = name;
        this.attended = attended;
    }

    public item_trainee_attendance(JSONObject jsonObject) {
        try {
            name = jsonObject.getString("trainee_name");
            trainee_id = jsonObject.getInt("trainee_id");
            int attend = jsonObject.getInt("trainee_attend");
            if (attend == 1)
                attended = true;
            else
                attended = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getTrainee_id() {
        return trainee_id;
    }

    public boolean isAttended() {
        return attended;
    }
}
