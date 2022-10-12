package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.data.request.UserRequest

interface UserRepository : Repository<UserEntity, String, UserRequest> {
    suspend fun updateUserDetails(userDetailsRequest: UserRequest): Resource<UserEntity>
}