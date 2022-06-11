package com.webkul.mobikul.odoo.adapter.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemProductDetailCategoryBinding
import com.webkul.mobikul.odoo.databinding.ItemProductDetailSuitableCropsBinding
import com.webkul.mobikul.odoo.databinding.ItemProductDetailV1Binding

class ProductDetailsAdapterV1 (private val productDetailsList : Map<String,List<String>>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val categoryDetailsViewType = -2
    private val suitableCropDetailsViewType = -1
    private val TAG = "ProductDetailsAdapterV1"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == suitableCropDetailsViewType) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail_suitable_crops, parent, false)
            return SuitableCropViewHolder(view)
        }
        else if(viewType == categoryDetailsViewType){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail_category, parent, false)
            return CategoryViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail_v1, parent, false)
            return RecyclerViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == suitableCropDetailsViewType){
            SuitableCropViewHolder(holder.itemView).onBind(position)
        }
        else if(getItemViewType(position) == categoryDetailsViewType){
            CategoryViewHolder(holder.itemView).onBind(position)
        }
        else{
            RecyclerViewHolder(holder.itemView).onBind(position)
        }
    }

    override fun getItemCount(): Int {
        return productDetailsList.size
    }

    override fun getItemViewType(position: Int): Int {

        if(productDetailsList.keys.elementAt(position) == context.getString(R.string.product_details_crops)){
            return suitableCropDetailsViewType;
        }

        if((productDetailsList.keys.elementAt(position) == context.getString(R.string.product_details_brand)) or (productDetailsList.keys.elementAt(position) == context.getString(R.string.product_details_category))){
            return categoryDetailsViewType;
        }

        return position;
    }

    inner class RecyclerViewHolder constructor(itemView: View) : RecyclerView.ViewHolder (itemView) {
        val binding  : ItemProductDetailV1Binding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvValue.text = productDetailsList.values.elementAt(position)[0]
            binding.tvId.text = productDetailsList.keys.elementAt(position).toString()
        }
    }

    inner class SuitableCropViewHolder constructor(itemView: View) : RecyclerView.ViewHolder (itemView) {
        val binding  : ItemProductDetailSuitableCropsBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvId.text = productDetailsList.keys.elementAt(position).toString()
            val layoutManager = FlexboxLayoutManager(itemView.context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            binding.rvSuitableCrops.layoutManager = layoutManager
            val crops = productDetailsList.values.elementAt(position)
            val updatedCrops = mutableListOf<String>()
            var adapter = ProductDetailsSuitableCropAdapter(crops)
            binding.rvSuitableCrops.adapter = adapter
            if(crops.size > 5){
                for (i in 0..3){
                    updatedCrops.add(crops[i])
                }
                adapter = ProductDetailsSuitableCropAdapter(updatedCrops)
                binding.rvSuitableCrops.adapter = adapter
                binding.tvSeeMore.visibility = View.VISIBLE
            }
            binding.tvSeeMore.setOnClickListener {
                adapter = ProductDetailsSuitableCropAdapter(crops)
                binding.rvSuitableCrops.adapter = adapter
                binding.tvSeeMore.visibility = View.GONE
            }
        }
    }

    inner class CategoryViewHolder constructor(itemView: View) : RecyclerView.ViewHolder (itemView) {
        val binding  : ItemProductDetailCategoryBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvValue.text = productDetailsList.values.elementAt(position)[0]
            binding.tvId.text = productDetailsList.keys.elementAt(position).toString()
        }
    }
}