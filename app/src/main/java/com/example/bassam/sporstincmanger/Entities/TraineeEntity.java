package com.example.bassam.sporstincmanger.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Bassam on 19/3/2018.
 */

public class TraineeEntity implements Serializable {
    int TraineeID;
    String TraineeName;
    int PaidStatus;
    boolean checked;

    public TraineeEntity(int TraineeID, String traineeName, int paidStatus) {
        this.TraineeID = TraineeID;
        this.TraineeName = traineeName;
        this.PaidStatus = paidStatus;
        this.checked = false;
    }

    public TraineeEntity(JSONObject jsonObject) {
        try {
            this.TraineeID = jsonObject.getInt("trainee_id");
            this.TraineeName = jsonObject.getString("trainee_name");
            this.PaidStatus = jsonObject.getInt("status");
            this.checked = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTraineeName() {
        return TraineeName;
    }

    public int getPaidStatus() {
        return PaidStatus;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getTraineeID() {
        return TraineeID;
    }

    public boolean isChecked() {
        return checked;
    }
}
