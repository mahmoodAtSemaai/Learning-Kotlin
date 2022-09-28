package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity

interface CategoryProductRepository : Repository {
    suspend fun get(categoryId: Int, globalProductsEnabled : Boolean, offset: Int, limit: Int): Resource<ProductListEntity>
}