package com.example.webapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webapp.R;
import com.example.webapp.data.DataProvider;

import org.json.JSONException;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ProductRA extends RecyclerView.Adapter<ProductRA.ViewHolder> {
    private final DataProvider _dataProvider;

    public ProductRA(Context context, String filter) throws JSONException {
        _dataProvider = new DataProvider(context, this, filter);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView title;
        public final TextView subCategori;
        public final TextView description;
        public TextView price = null;
        public final TextView discount;
        public final TextView final_price;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            image = (ImageView) view.findViewById(R.id.card_image);
            title = (TextView) view.findViewById(R.id.card_title);
            subCategori = (TextView) view.findViewById(R.id.card_subCategori);
            description = (TextView) view.findViewById(R.id.card_description);
            price = (TextView) view.findViewById(R.id.card_price);
            discount = (TextView) view.findViewById(R.id.card_discount);
            final_price = (TextView) view.findViewById(R.id.card_final_price);
        }

        public void setPrice(String text) {
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price.setText(text);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_card, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        this._dataProvider.loadImage(viewHolder.image, position);
        viewHolder.title.setText(_dataProvider.products.get(position).title);
        viewHolder.subCategori.setText(_dataProvider.products.get(position).subCategori.title);
        viewHolder.description.setText(_dataProvider.products.get(position).description);
        viewHolder.setPrice(Double.toString(_dataProvider.products.get(position).price));
        viewHolder.discount.setText(Integer.toString(_dataProvider.products.get(position).discount) + " $");
        viewHolder.final_price.setText(Double.toString(_dataProvider.products.get(position).price - (_dataProvider.products.get(position).price * _dataProvider.products.get(position).discount / 100)));
        // Load Next Page
        if (getItemCount() == position + 1 && _dataProvider.hasNextPage())
            _dataProvider.loadNextPage();
        Log.i("TAG", "RView: " + Integer.toString(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _dataProvider.products.size();
    }
}

