package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddToWishlistEntity
import com.webkul.mobikul.odoo.data.entity.RemoveFromWishlistEntity
import com.webkul.mobikul.odoo.data.request.WishListRequest
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.request.AddToWishlistRequest

interface WishListRepository : Repository {

    suspend fun update(wishListRequest: WishListRequest) : Resource<AddToWishlistEntity>

    suspend fun delete(wishListRequest: WishListRequest) : Resource<RemoveFromWishlistEntity>
    
    suspend fun isWishlistAllowed() : Resource<Boolean>
    
}