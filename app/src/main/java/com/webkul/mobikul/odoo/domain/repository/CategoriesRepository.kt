package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductCategoriesEntity

interface CategoriesRepository : Repository<Any,Any,Any> {

    suspend fun getCategories(): Resource<ProductCategoriesEntity>
}