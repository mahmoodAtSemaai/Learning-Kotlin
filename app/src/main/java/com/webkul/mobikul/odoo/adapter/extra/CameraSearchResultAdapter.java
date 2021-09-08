package com.webkul.mobikul.odoo.adapter.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CameraSearchActivity;

import java.util.List;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class CameraSearchResultAdapter extends RecyclerView.Adapter<CameraSearchResultAdapter.ViewHolder> {
    private Context context;
    private List<String> labelList;

    public CameraSearchResultAdapter(Context context, List<String> labelList) {
        this.context = context;
        this.labelList = labelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.camera_simple_spinner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ((TextView) holder.itemView.findViewById(R.id.label_tv)).setText(labelList.get(position));
        holder.itemView.findViewById(R.id.label_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraSearchActivity) context).sendResultBack(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

