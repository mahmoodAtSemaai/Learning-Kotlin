package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.helper.getDeliveryLeadTime

data class ProductEntity(
	@SerializedName("template_id") val templateId: Int? = null,
	@SerializedName("name") var name: String? = null,
	@SerializedName("price_unit") val priceUnit: String? = null,
	@SerializedName("price_reduce") val priceReduce: String? = null,
	@SerializedName("product_id") val productId: String? = null,
	@SerializedName("description") val description: String? = null,
	@SerializedName("thumbnail") val thumbnail: String? = null,
	@SerializedName("sale_delay") val saleDelay: Float = 0f,
	@SerializedName("brand_name") val brandName: String? = null,
	@SerializedName("available_quantity") val availableQuantity: Int = 0,
	@SerializedName("inventory_availability") val inventoryAvailability: String? = null,
	@SerializedName("global_product_id") val globalProductId: String? = null,
	@SerializedName("images") val images: ArrayList<String>? = null,
	@SerializedName("available_threshold") val availableThreshold: Float = 0f,
	@SerializedName("added_to_wishlist") var addedToWishlist: Boolean = false,
	@SerializedName("seller_info") val sellerInfo: SellerDetailsEntity? = null,
	@SerializedName("add_to_cart") val addToCart: Boolean = false,
	@SerializedName("stock_display_msg") val stockDisplayMsg: String? = null,
	@SerializedName("ar_ios") val arIos: String,
	@SerializedName("ar_android") val arAndroid: String,
	@SerializedName("absolute_url") val absoluteUrl: String? = null,
	@SerializedName("mobikul_category_details") val mobikulCategoryDetails: MobikulCategoryDetailEntity? = null,
	@SerializedName("product_type") val productType: String? = null,
	private var quantity: Int = 1
) {

	fun calculateDiscount(): String {
		return if (priceReduce != null) {
			val originalPrice: Double = stringToDouble(priceUnit)
			val reducedPrice: Double = stringToDouble(priceReduce)
			var discount = (originalPrice - reducedPrice) / originalPrice * 100
			discount = Math.round(discount).toDouble()
			"${discount.toInt()}%"
		} else ""
	}


	private fun stringToDouble(str: String?): Double {
		var updatedString = "0"
		if (str.isNullOrBlank().not()) {
			val s = str?.substring(2, str.length - 3)
			for (i in s!!.indices) {
				if (s[i] == ',' || s[i] == '.') continue
				updatedString += s[i]
			}
		}
		return updatedString.toDouble()
	}

	fun isOutOfStock(): Boolean {
		return ((isAlways() || isThreshold()) && availableQuantity == 0)
	}

	fun isInStock(): Boolean {
		return availableQuantity > 0
	}

	fun isAlways(): Boolean {
		return inventoryAvailability == ApplicationConstant.ALWAYS
	}

	fun isThreshold(): Boolean {
		return inventoryAvailability == ApplicationConstant.THRESHOLD
	}

	fun isNever(): Boolean {
		return inventoryAvailability == ApplicationConstant.NEVER
	}

	fun isPreOrder(): Boolean {
		return inventoryAvailability == ApplicationConstant.PRE_ORDER
	}

	fun isCustom(): Boolean {
		return inventoryAvailability == ApplicationConstant.CUSTOM
	}

	fun getDeliveryLeadTime(): String? {
		return getDeliveryLeadTime(saleDelay.toInt())
	}

	fun getCurrentQuantity(): Int {
		if (quantity < ApplicationConstant.QTY_ZERO) {
			return 0
		} else if (quantity == ApplicationConstant.QTY_ZERO) {
			setCurrentQuantity(quantity)
		}
		return quantity
	}

	fun setCurrentQuantity(quantity: Int) {
		if (quantity < ApplicationConstant.QTY_ZERO) {
			return
		} else if (quantity == ApplicationConstant.QTY_ZERO && isOutOfStock()) {
			this.quantity = ApplicationConstant.QTY_ZERO
		} else if (quantity == ApplicationConstant.QTY_ZERO && !isOutOfStock()) {
			this.quantity = 1
		} else {
			this.quantity = quantity
		}
		AnalyticsImpl.trackItemQuantitySelected(quantity, productId!!, name!!)
	}

	fun isService(): Boolean {
		return productType == ApplicationConstant.TYPE_SERVICE
	}

}
