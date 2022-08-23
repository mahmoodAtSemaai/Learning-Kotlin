package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest

interface UserAddressRepository : Repository {
    suspend fun setUserAddress(
        partnerId: String,
        userAddressRequest: UserAddressRequest
    ): Resource<UserAddressEntity>
}