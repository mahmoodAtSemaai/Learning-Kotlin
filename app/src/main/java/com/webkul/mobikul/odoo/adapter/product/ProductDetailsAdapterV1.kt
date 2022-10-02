package com.webkul.mobikul.odoo.adapter.product

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.*
import com.webkul.mobikul.odoo.helper.CatalogHelper

class ProductDetailsAdapterV1(
    private val productDetailsMap: Map<String, List<String>>,
    private val categoryId : Int,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val CATEGORY_VIEW_TYPE = -2
    private val SUITABLE_CROPS_VIEW_TYPE = -1
    private val BRAND_VIEW_TYPE = -3
    private var seeMoreView = false
    private val TAG = "ProductDetailsAdapterV1"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SUITABLE_CROPS_VIEW_TYPE -> {
                SuitableCropViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_detail_suitable_crops, parent, false))
            }
            CATEGORY_VIEW_TYPE -> {
                CategoryViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_detail_brand_category, parent, false))
            }
            BRAND_VIEW_TYPE -> {
                BrandViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_detail_brand_category, parent, false))
            }
            else -> {
                RecyclerViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_detail_v1, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == SUITABLE_CROPS_VIEW_TYPE -> {
                SuitableCropViewHolder(holder.itemView).onBind(position)
            }
            getItemViewType(position) == CATEGORY_VIEW_TYPE -> {
                CategoryViewHolder(holder.itemView).onBind(position)
            }
            getItemViewType(position) == BRAND_VIEW_TYPE -> {
                BrandViewHolder(holder.itemView).onBind(position)
            }
            else -> {
                RecyclerViewHolder(holder.itemView).onBind(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return productDetailsMap.size
    }

    override fun getItemViewType(position: Int): Int {

        if (productDetailsMap.keys.elementAt(position) == context.getString(R.string.product_details_crops)) {
            return SUITABLE_CROPS_VIEW_TYPE
        }

        if (productDetailsMap.keys.elementAt(position) == context.getString(R.string.product_details_category)) {
            return CATEGORY_VIEW_TYPE
        }

        if (productDetailsMap.keys.elementAt(position) == context.getString(R.string.product_details_brand)) {
            return BRAND_VIEW_TYPE
        }

        return position
    }

    inner class RecyclerViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemProductDetailV1Binding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.apply {
                tvValue.text = productDetailsMap.values.elementAt(position)[0]
                tvDetailId.text = productDetailsMap.keys.elementAt(position).toString()
            }
        }
    }

    inner class SuitableCropViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: ItemProductDetailSuitableCropsBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            val layoutManager = FlexboxLayoutManager(itemView.context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            val crops = productDetailsMap.values.elementAt(position)
            val updatedCrops = mutableListOf<String>()
            binding.apply {
                rvSuitableCrops.layoutManager = layoutManager
                tvSuitableId.text = productDetailsMap.keys.elementAt(position).toString()
                rvSuitableCrops.adapter = ProductDetailsSuitableCropAdapter(crops)
            }
            if (crops.size > 5) {
                for (i in 0..3) {
                    updatedCrops.add(crops[i])
                }
                binding.tvSeeMore.visibility = View.VISIBLE
                setAdapter(ProductDetailsSuitableCropAdapter(updatedCrops), true)
            }
            binding.tvSeeMore.setOnClickListener {
                if (seeMoreView) {
                    binding.tvSeeMore.text = itemView.context.getString(R.string.see_less)
                    setAdapter(ProductDetailsSuitableCropAdapter(crops), false)
                } else {
                    binding.tvSeeMore.text = itemView.context.getString(R.string.see_all)
                    setAdapter(ProductDetailsSuitableCropAdapter(updatedCrops), true)
                }
            }
        }

        private fun setAdapter(
            productDetailsSuitableCropAdapter: ProductDetailsSuitableCropAdapter,
            seeMoreFlag: Boolean
        ) {
            binding.rvSuitableCrops.adapter = productDetailsSuitableCropAdapter
            seeMoreView = seeMoreFlag
        }
    }

    inner class CategoryViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemProductDetailBrandCategoryBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvProductDetailId.text = productDetailsMap.keys.elementAt(position).toString()
            binding.tvProductDetailValue.apply {
                text = productDetailsMap.values.elementAt(position)[0]
                setOnClickListener {
                    val query = categoryId
                    Intent(itemView.context, CatalogProductActivity::class.java).apply {
                        action = Intent.ACTION_SEARCH
                        putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_ID, query)
                        putExtra(
                            BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE,
                            CatalogHelper.CatalogProductRequestType.BANNER_CATEGORY
                        )
                        itemView.context.startActivity(this)
                    }
                }
            }
        }
    }

    inner class BrandViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemProductDetailBrandCategoryBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.apply {
                tvProductDetailValue.text = productDetailsMap.values.elementAt(position)[0]
                tvProductDetailId.text = productDetailsMap.keys.elementAt(position).toString()
                tvProductDetailValue.setOnClickListener {
                    val query = binding.tvProductDetailValue.text.toString()
                    Intent(itemView.context, CatalogProductActivity::class.java).apply {
                        action = Intent.ACTION_SEARCH
                        putExtra(SearchManager.QUERY, query)
                        putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_NAME, query)
                        putExtra(
                            BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE,
                            CatalogHelper.CatalogProductRequestType.SEARCH_QUERY
                        )
                        itemView.context.startActivity(this)
                    }
                }
            }
        }
    }
}