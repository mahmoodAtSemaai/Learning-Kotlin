package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity

interface SellerProductRepository : Repository {

    suspend fun get(sellerId: Int): Resource<ProductListEntity>

}