package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserDetailEntity

interface UserDetailRepository : Repository<Any,Any,Any> {

    suspend fun getUser(userId: String): Resource<UserDetailEntity>
    suspend fun getUserDetails(): Resource<UserDetailEntity>
}