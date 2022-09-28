package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity

interface ProductSellersRepository : Repository {

    suspend fun get(globalProductID: Int, fromProductTemplate: Int): Resource<ProductListEntity>

}