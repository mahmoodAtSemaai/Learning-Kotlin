package com.webkul.mobikul.odoo.ui.price_comparison

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.databinding.ItemProductPriceComparisonBinding
import java.util.*

class ProductSellersAdapter(
	var sellerProducts : ArrayList<ProductEntity>,
	val listener : ProductSellersListener
) : RecyclerView.Adapter<ProductSellersAdapter.ViewHolder>() {
	
	var highlight : Boolean = false
	
	override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_product_price_comparison, parent, false)
		val finalView = ViewHolder(view)
		finalView.setListeners()
		return finalView
	}
	
	override fun onBindViewHolder(holder : ViewHolder, position : Int) {
		ViewHolder(holder.itemView).onBind(sellerProducts[position], holder.itemView.context)
	}
	
	override fun getItemCount() : Int = sellerProducts.size
	
	fun updateProduct(product : ProductEntity, position : Int) {
		sellerProducts[position] = product
	}
	
	inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		val binding : ItemProductPriceComparisonBinding = DataBindingUtil.bind(itemView)!!
		
		
		fun onBind(product : ProductEntity, context : Context) {
			binding.apply {
				
				if (highlight) {
					highlight = false
					val colorFade = ObjectAnimator.ofObject(
						clProduct, "backgroundColor", ArgbEvaluator(), ContextCompat.getColor(
							context, R.color.background_orange
						), ContextCompat.getColor(
							context, R.color.transparent
						)
					)
					colorFade.apply {
						duration = 250
						startDelay = 3000
						start()
					}
				}
				
				tvSellerName.text = product.sellerInfo?.name ?: ""
				
				etQuantity.setText(product.getCurrentQuantity().toString())
				
				tvPrice.text =
					if (product.priceReduce.isNullOrBlank()) product.priceUnit else product.priceReduce

				tvStock.apply {
					visibility = if (product.isNever() || product.isPreOrder() || product.isCustom() || product.isService()) View.GONE else View.VISIBLE
					text = if (product.isInStock()) {
						if (product.isThreshold()) {
							if (product.availableQuantity >= product.availableThreshold) {
								context.getString(R.string.in_stock)
							} else {
								String.format(
									context.getString(R.string.in_stock_quantity_x),
									product.availableQuantity
								)
							}
						} else {
							String.format(
								context.getString(R.string.in_stock_quantity_x),
								product.availableQuantity.toInt()
							)
						}
					} else {
						context.getString(R.string.in_stock_quantity_0)
					}

				}
				
				ibCartIcon.setImageDrawable(
					ResourcesCompat.getDrawable(
						context.resources,
						if (product.isOutOfStock()) R.drawable.cart_disabled
						else if (product.isPreOrder()) R.drawable.cart_pre_order
						else R.drawable.cart,
						null
					)
				)
				
				tvCartText.apply {
					text = if (product.isOutOfStock()) context.getString(R.string.out_of_stock)
					else if (product.isPreOrder()) context.getString(R.string.pre_order)
					else context.getString(R.string.add)
					
					setTextColor(
						ContextCompat.getColor(
							context,
							if (product.isOutOfStock()) R.color.gray
							else if (product.isPreOrder()) R.color.background_pre_order
							else R.color.background_orange
						)
					)
				}
				
				tvEstimatedArrival.text = String.format(
					Locale.getDefault(),
					context.getString(R.string.estimated_arrival_x),
					product.getDeliveryLeadTime()
				)
				
				clProduct.background =
					ContextCompat.getDrawable(context, R.drawable.bg_item_price_comparison)
				
			}
		}
		
		
		fun setListeners() {
			binding.apply {
				llAddToCart.setOnClickListener {
					listener.onSellerProductAddToCartClick(adapterPosition)
				}
				
				
				btnPlus.setOnClickListener {
					listener.onSellerProductPlusClick(adapterPosition)
				}
				
				btnMinus.setOnClickListener {
					listener.onSellerProductMinusClick(adapterPosition)
				}
				
				llChat.setOnClickListener {
					listener.onSellerProductChatClick(adapterPosition)
				}
				
				tvSellerName.setOnClickListener {
					listener.onSellerProductSellerNameClick(adapterPosition)
				}
				
				binding.etQuantity.apply {
					
					doOnTextChanged { text, _, _, _ ->
						val qty = if (!text.isNullOrBlank()) text.toString().toInt() else null
						if (sellerProducts[adapterPosition].getCurrentQuantity() != qty && qty != null) {
							listener.onSellerProductEditTextClick(qty, adapterPosition)
						}
					}
					
					setOnEditorActionListener { _, actionId, _ ->
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							if (text.isNullOrBlank()) listener.onSellerProductEditTextClick(
								1,
								adapterPosition
							)
							else {
								val qty = text.toString().toInt()
								listener.onSellerProductEditTextClick(qty, adapterPosition)
							}
							return@setOnEditorActionListener true
						}
						false
					}
					
				}
			}
			
		}
		
		
	}
	
	
}
