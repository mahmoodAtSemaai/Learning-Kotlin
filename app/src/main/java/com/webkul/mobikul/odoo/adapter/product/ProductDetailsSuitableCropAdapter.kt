package com.webkul.mobikul.odoo.adapter.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemSuitableCropBinding

class ProductDetailsSuitableCropAdapter(private val mCrops : List<String>) : RecyclerView.Adapter<ProductDetailsSuitableCropAdapter.ProductDetailsSuitableCropViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailsSuitableCropAdapter.ProductDetailsSuitableCropViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suitable_crop, parent, false)
        return ProductDetailsSuitableCropViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductDetailsSuitableCropAdapter.ProductDetailsSuitableCropViewHolder,
        position: Int
    ) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return  mCrops.size
    }

    inner class ProductDetailsSuitableCropViewHolder constructor(itemView : View) : RecyclerView.ViewHolder (itemView) {
        val binding  : ItemSuitableCropBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvCrop.text = mCrops[position]
        }
    }
}