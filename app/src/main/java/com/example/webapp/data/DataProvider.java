package com.example.webapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.webapp.utils.ProductRA;
import com.example.webapp.config.HttpUrl;
import com.example.webapp.models.Product;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DataProvider {
    private final RequestQueue queue;
    private final Context _context;
    private final RecyclerView.Adapter<ProductRA.ViewHolder> _adapter;
    private final String _filter;
    public List<Product> products = new ArrayList<Product>();
    private int page = 1;
    private boolean hasNextPage = true;

    public DataProvider(Context context, RecyclerView.Adapter<ProductRA.ViewHolder> recyclerViewAdapter, String filter) {
        _context = context;
        queue = Volley.newRequestQueue(context);
        _adapter = recyclerViewAdapter;
        _filter = filter;
        loadObjects();
    }

    private void loadObjects() {
        if (!hasNextPage) return;
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, HttpUrl.OBJECT_URL + this.page, null,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        hasNextPage = response.getBoolean("hasNext");
                        List<Product> p = new ArrayList<Product>();
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                products.add(new Product(data.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        products = filterData(products);
                        _adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.w("INTERNET_OBJECT", "ERROR = " + error.getMessage())
        );
        queue.add(request);
    }

    private List<Product> filterData(List<Product> p) {
        switch (_filter) {
            case "ALL":
                return p;
            case "PRICE":
                p.sort(this::compareProducts);
                return p;
            default:
                return p.stream().filter(product -> product.subCategori.title.equals(_filter)).collect(Collectors.toList());
        }
    }

    // Decrease Sort
    private int compareProducts(Product p1, Product p2) {
        if (p1.price == p2.price) return 0;
        if (p1.price > p2.price) return -1;
        if (p1.price < p2.price) return 1;
        return 2;
    }

    public void loadImage(ImageView image, int position) {
        Glide.with(this._context).load(HttpUrl.IMAGE + products.get(position).image).into(image);
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void loadNextPage() {
        this.page++;
        loadObjects();
        Log.i("TAG", "loadNextPage: " + Integer.toString(page));
    }
}
