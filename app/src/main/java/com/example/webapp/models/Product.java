package com.example.webapp.models;

import org.json.JSONObject;

public class Product extends BaseModel {
    public int objectID;
    public String title;
    public String description;
    public String image;
    public double price;
    public int discount;
    public Attribute attribute;
    public SubCategori subCategori;

    public Product(JSONObject data) {
        super(data);
    }
}
