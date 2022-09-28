package com.webkul.mobikul.odoo.ui.category

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.ui.home.HomeState

sealed class CategoryProductState : IState {
	
	object Idle : CategoryProductState()
	object Loading : CategoryProductState()
	
	object CheckedArguments : CategoryProductState()
	data class ProductListSuccess(val productListEntity : ProductListEntity) : CategoryProductState()
	
	data class Error(val message : String?, val failureStatus : FailureStatus) : CategoryProductState()
	
}
