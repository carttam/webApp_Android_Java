package com.example.webapp.models;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.lang.reflect.Field;


public abstract class BaseModel {
    public BaseModel(JSONObject data) {
        this.Bind(data);
    }

    // Bind data's from json object into class extend this class
    protected void Bind(@NonNull JSONObject data) {
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                String f_name = field.getName();
                if (!data.has(f_name) || data.isNull(f_name))
                    continue; // if field doesn't exist or is null return
                if (field.getType().getName().startsWith("com.example.webapp.models"))      // custom class initiate
                    field.set(this, Class.forName(field.getType().getName()).getConstructor(JSONObject.class).newInstance(data.getJSONObject(f_name)));
                else                                                                        // default class set value
                    field.set(this, data.get(f_name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
