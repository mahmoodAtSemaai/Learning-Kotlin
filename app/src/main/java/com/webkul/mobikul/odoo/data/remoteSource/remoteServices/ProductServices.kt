package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.data.response.ProductListResponse
import com.webkul.mobikul.odoo.data.response.ProductResponse
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductServices {
	
	companion object {
		const val QUERY_SEARCH_PRODUCTS = "search"
		const val QUERY_SELLER_PRODUCTS = "seller_id"
		const val QUERY_CATEGORY_PRODUCTS = "cid"
		const val QUERY_GLOBAL_PRODUCT_ID = "global_product_id"
		const val QUERY_GLOBAL_PRODUCTS_ENABLED = "global_products"
		const val QUERY_FROM_PRODUCT_TEMPLATE = "from_product"
		const val QUERY_PRODUCTS_OFFSET = "offset"
		const val QUERY_PRODUCTS_LIMIT = "limit"
		const val PATH_TEMPLATE_ID = "template_id"
		const val GET_PRODUCT = "v1/products"
		const val GET_PRODUCT_DETAILS = "$GET_PRODUCT/{$PATH_TEMPLATE_ID}"
	}
	
	
	@GET(GET_PRODUCT)
	suspend fun getSearchProducts(
			@Query(QUERY_SEARCH_PRODUCTS) searchQuery : String,
			@Query(QUERY_GLOBAL_PRODUCTS_ENABLED) globalProductsEnabled : Boolean,
			@Query(QUERY_PRODUCTS_OFFSET) offset : Int,
			@Query(QUERY_PRODUCTS_LIMIT) limit : Int
	) : BaseResponseNew<ProductListResponse>
	
	
	@GET(GET_PRODUCT)
	suspend fun getSellerProducts(
			@Query(QUERY_SELLER_PRODUCTS) sellerId : Int,
			@Query(QUERY_PRODUCTS_OFFSET) offset : Int,
			@Query(QUERY_PRODUCTS_LIMIT) limit : Int
	) : BaseResponseNew<ProductListResponse>
	
	
	@GET(GET_PRODUCT)
	suspend fun getCategoryProducts(
			@Query(QUERY_CATEGORY_PRODUCTS) categoryId : Int,
			@Query(QUERY_GLOBAL_PRODUCTS_ENABLED) globalProductsEnabled : Boolean,
			@Query(QUERY_PRODUCTS_OFFSET) offset : Int,
			@Query(QUERY_PRODUCTS_LIMIT) limit : Int
	) : BaseResponseNew<ProductListResponse>
	
	
	@GET(GET_PRODUCT_DETAILS)
	suspend fun getProductDetails(
			@Path(PATH_TEMPLATE_ID) templateId : Int
	) : BaseResponseNew<ProductResponse>
	
	
	@GET(GET_PRODUCT)
	suspend fun getProductSellers(
			@Query(QUERY_GLOBAL_PRODUCT_ID) globalProductID : Int,
			@Query(QUERY_FROM_PRODUCT_TEMPLATE) fromProductTemplate : Int
	) : BaseResponseNew<ProductListResponse>
}