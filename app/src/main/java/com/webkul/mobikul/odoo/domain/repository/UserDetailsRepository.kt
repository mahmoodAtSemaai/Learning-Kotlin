package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserDetailsEntity
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest

interface UserDetailsRepository : Repository {
    suspend fun setUserDetails(userId: String,partnerId: String,userDetailsRequest: UserDetailsRequest): Resource<UserDetailsEntity>
}