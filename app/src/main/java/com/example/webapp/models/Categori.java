package com.example.webapp.models;

import org.json.JSONObject;

public class Categori extends BaseModel {
    int categoriID;
    String title;

    public Categori(JSONObject data) {
        super(data);
    }
}
