package com.example.webapp.models;

import org.json.JSONObject;

public class SubCategori extends BaseModel {
    public int subCategoriID;
    public String title;
    public String image;

    public SubCategori(JSONObject data) {
        super(data);
    }
}
