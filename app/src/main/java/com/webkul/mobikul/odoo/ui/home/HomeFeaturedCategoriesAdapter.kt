package com.webkul.mobikul.odoo.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.ProductCategoryEntity
import com.webkul.mobikul.odoo.databinding.ItemFeaturedCategoryHomeV1Binding
import com.webkul.mobikul.odoo.helper.BindingAdapterUtils
import com.webkul.mobikul.odoo.helper.ImageHelper

class HomeFeaturedCategoriesAdapter(
		val context : Context,
		private val categories : List<ProductCategoryEntity>,
		private val listener : HomeFeaturedCategoryListener
) : RecyclerView.Adapter<HomeFeaturedCategoriesAdapter.ViewHolder>() {
	
	private var selectedPos = 0
	private var previousSelectedPos = 0
	private var firstTimeSetup = true
	
	override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_featured_category_home_v1, parent, false)
		return ViewHolder(view)
	}
	
	override fun onBindViewHolder(holder : ViewHolder, position : Int) {
		ViewHolder(holder.itemView).onBind(categories[position], position, holder)
		holder.itemView.isSelected = selectedPos == position
	}
	
	override fun getItemCount() : Int = categories.size
	
	
	inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		val binding : ItemFeaturedCategoryHomeV1Binding = DataBindingUtil.bind(itemView)!!
		
		fun onBind(productCategoryEntity : ProductCategoryEntity, position : Int, holder : ViewHolder) {
			
			binding.productIv.apply {
				ImageHelper.load(
						this,
						productCategoryEntity.icon ,
						null,
						DiskCacheStrategy.AUTOMATIC,
						false,
						ImageHelper.ImageType.PRODUCT_TINY
				)
			}
			
			binding.categoryName.apply {
				BindingAdapterUtils.setHtmlText(this, productCategoryEntity.name)
			}
			
			binding.container.setBackgroundResource(if (selectedPos == position) R.drawable.featured_categories_selected_item_bg else Color.TRANSPARENT)
			
			binding.categoryName.setTextColor(if (selectedPos == position) ContextCompat.getColor(context, R.color.background_orange) else ContextCompat.getColor(context, R.color.black))
			
			if (firstTimeSetup) {
				listener.onCategoryClick(position, previousSelectedPos)
				firstTimeSetup = false
			}
			
			binding.container.setOnClickListener {
				previousSelectedPos = selectedPos
				listener.onCategoryClick(position,previousSelectedPos)
				selectedPos = holder.adapterPosition
			}
			
		}
	}
	
	
	fun removePreviousSelectedItemMargin(previousPosition : Int) {
		notifyItemChanged(previousPosition)
	}
	
	fun addCurrentSelectedItemMargin(currentPosition : Int) {
		notifyItemChanged(currentPosition)
	}
	
	interface HomeFeaturedCategoryListener {
		fun onCategoryClick(position : Int, previousPosition : Int)
	}
	
}