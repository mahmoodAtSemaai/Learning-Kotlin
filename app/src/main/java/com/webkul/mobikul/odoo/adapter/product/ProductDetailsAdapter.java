package com.webkul.mobikul.odoo.adapter.product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.ArrayList;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder>{

    private final Context mContext;
    ArrayList<ArrayList<String>> productDetails = new ArrayList<>();

    public ProductDetailsAdapter(Context context, ArrayList<ArrayList<String>> productDetails){
        this.mContext = context;
        this.productDetails = productDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.product_details_item, parent, false);
        return new ProductDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.key.setText(productDetails.get(i).get(0));
        viewHolder.value.setText(productDetails.get(i).get(1));
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView key, value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.text_id);
            value = itemView.findViewById(R.id.text_value);
        }
    }
}
