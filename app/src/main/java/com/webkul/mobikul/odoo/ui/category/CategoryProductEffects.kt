package com.webkul.mobikul.odoo.ui.category

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.data.entity.ProductEntity

sealed class CategoryProductEffects : IEffect {
	data class OpenProduct(val data : ProductEntity) : CategoryProductEffects()
	object RefreshProductsList : CategoryProductEffects()
}
