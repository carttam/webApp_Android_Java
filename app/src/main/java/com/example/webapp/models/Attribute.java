package com.example.webapp.models;

import org.json.JSONObject;

public class Attribute extends BaseModel {
    int attributeID;
    String titles;
    String datas;

    public Attribute(JSONObject data) {
        super(data);
    }
}
