package com.webkul.mobikul.odoo.ui.category

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.databinding.ItemCategoryProductsV1Binding
import com.webkul.mobikul.odoo.helper.EndlessRecyclerViewScrollListener
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.helper.ImageHelper
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2

class CategoryProductsAdapter(
		val context : Context,
		val productList : ArrayList<ProductEntity>,
		val listener : CategoryProductsInterface
) :
		RecyclerView.Adapter<CategoryProductsAdapter.ViewHolder>() {
	
	
	override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
		return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_products_v1, parent, false))
	}
	
	override fun onBindViewHolder(holder : ViewHolder, position : Int) {
		ViewHolder(holder.itemView).onBind(productList[position], position)
	}
	
	
	override fun getItemCount() : Int = productList.size
	
	inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		val binding : ItemCategoryProductsV1Binding = DataBindingUtil.bind(itemView)!!
		
		fun onBind(data : ProductEntity, position : Int) {
			binding.apply {
				
				ivProduct.apply {
					ImageHelper.load(
							this,
							data.thumbnail ?: "",
							ContextCompat.getDrawable(context, R.drawable.ic_vector_odoo_placeholder_grey400_48dp),
							null,
							true,
							ImageHelper.ImageType.PRODUCT_GENERIC
					)
				}
				
				
				tvDiscountPrice.apply {
					text = data.calculateDiscount()
					visibility = if (data.priceReduce.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
				}
				
				flContainer.apply {
					background = if (data.isPreOrder()) ContextCompat.getDrawable(context, R.drawable.bg_pre_order) else ContextCompat.getDrawable(context, R.drawable.bg_out_of_stock)
					visibility = if (data.isNever() || data.isCustom() || data.isService()) View.GONE else if (data.isPreOrder()) View.VISIBLE else if (data.isInStock()) View.GONE else View.VISIBLE
				}
				
				tvPreOrder.text = if (data.isPreOrder()) context.getString(R.string.pre_order) else context.getString(R.string.out_of_stock)
				
				tvProductName.text = data.name
				
				tvReducedPrice.text = if (data.priceReduce.isNullOrBlank()) data.priceUnit else data.priceReduce
				
				tvPriceUnit.apply {
					text = data.priceUnit
					visibility = if (data.priceReduce.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
				}
				
				tvSellerName.apply {
					text = data.brandName
					visibility = if (!data.brandName.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
				}
				
				tvStockValue.apply {
					visibility = if (data.isNever()) View.GONE else if (data.isNever() || data.isCustom() || data.isPreOrder() || data.isService()) View.GONE else View.VISIBLE
					text = if (data.isInStock()) {
								if (data.isThreshold()) {
									if (data.availableQuantity >= data.availableThreshold) {
										context.getString(R.string.in_stock)
									} else {
										String.format(context.getString(R.string.in_stock_quantity_x), data.availableQuantity)
									}
								} else {
									String.format(context.getString(R.string.in_stock_quantity_x), data.availableQuantity)
								}
							} else {
								context.getString(R.string.in_stock_quantity_0)
							}
					
				}
				
				
				tvEstimatedArrival.apply {
					visibility = if (data == null) View.GONE else if (data.isPreOrder()) View.VISIBLE else View.GONE
					text = context.getString(R.string.estimated_arrival_x, data.getDeliveryLeadTime())
				}
				
				
				root.setOnClickListener {
					listener.onProductClick(data)
				}
				
			}
		}
	}
	
	
	interface CategoryProductsInterface {
		fun onProductClick(data : ProductEntity)
	}
	
}