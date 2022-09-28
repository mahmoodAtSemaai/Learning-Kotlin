package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.data.response.ProductCategoriesResponse
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew
import retrofit2.http.GET

interface CategoriesServices {

    companion object {
        const val GET_PRODUCT_CATEGORIES = "v1/product-categories"
        const val GET_FEATURED_CATEGORIES = "$GET_PRODUCT_CATEGORIES?type=featured"
    
    }

    @GET(GET_FEATURED_CATEGORIES)
    suspend fun getProductCategories(): BaseResponseNew<ProductCategoriesResponse>
}