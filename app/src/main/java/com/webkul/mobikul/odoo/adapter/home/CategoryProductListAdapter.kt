package com.webkul.mobikul.odoo.adapter.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ItemCategoryProductsBinding
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.model.generic.ProductData
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2

class CategoryProductListAdapter(
	val context : Context,
	val productList : ArrayList<ProductData>
) :
	RecyclerView.Adapter<CategoryProductListAdapter.ViewHolder>() {
	
	
	override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
		return ViewHolder(
			LayoutInflater.from(context).inflate(R.layout.item_category_products, parent, false)
		)
	}
	
	override fun onBindViewHolder(holder : ViewHolder, position : Int) {
		bindData(holder, productList.get(position))
	}
	
	private fun bindData(holder : ViewHolder, data : ProductData) {
		holder.binding.data = data
		holder.binding.root.setOnClickListener {
			redirectToProductDetailActivtiy(data)
		}
		holder.binding.root.setOnClickListener {
			redirectToProductDetailActivtiy(data)
		}
	}
	
	private fun redirectToProductDetailActivtiy(data : ProductData) {
		AnalyticsImpl.trackProductItemSelected(
			Helper.getScreenName(context),
			data.productId,
			data.name
		)
		Intent(context, ProductActivityV2::class.java).apply {
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, data.productId)
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, data.name)
			try {
				putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID, data.templateId.toInt())
			} catch (ignored : NumberFormatException) {
			}
			context.startActivity(this)
		}
	}
	
	
	override fun getItemCount() : Int = productList.size
	
	
	inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		val binding : ItemCategoryProductsBinding = DataBindingUtil.bind(itemView)!!
	}
	
	
}