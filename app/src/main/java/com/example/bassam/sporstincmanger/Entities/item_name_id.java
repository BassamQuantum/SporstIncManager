package com.example.bassam.sporstincmanger.Entities;

/**
 * Created by Bassam on 25/3/2018.
 */

public class item_name_id {
    int id; String name;
    boolean selected;

    public item_name_id(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
