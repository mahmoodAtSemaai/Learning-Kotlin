package com.webkul.mobikul.odoo.ui.category

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.ProductEntity

sealed class CategoryProductIntent : IIntent {
	
	data class CheckArguments(val arguments : Bundle?) : CategoryProductIntent()
	data class GetProducts(val categoryId : String, val offset : Int, val limit : Int) : CategoryProductIntent()
	data class OpenProduct(val data : ProductEntity) : CategoryProductIntent()
	data class RefreshProductList(
		val lastItemPosition : Int,
		val rvItemCount : Int,
		val tCount : Int,
		val dataRequested : Boolean
	) : CategoryProductIntent()
	
}
	

