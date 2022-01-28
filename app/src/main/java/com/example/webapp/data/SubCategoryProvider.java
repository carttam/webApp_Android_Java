package com.example.webapp.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.webapp.config.HttpUrl;
import com.example.webapp.models.SubCategori;
import com.example.webapp.utils.ISetup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryProvider {
    public static void load(Context context, ISetup setup){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, HttpUrl.CATEGORI_URL, null,
                response -> {
                    List<String> subCategorise = new ArrayList<String>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONArray data = response.getJSONObject(i).getJSONArray("subCategoris");
                            for (int j = 0; j < data.length(); j++) {
                                subCategorise.add((new SubCategori(data.getJSONObject(j))).title);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setup.setup(subCategorise);
                },
                error -> Log.w("INTERNET_SUBCAT", "ERROR = " + error.getMessage())
        );
        queue.add(request);
    }
}
