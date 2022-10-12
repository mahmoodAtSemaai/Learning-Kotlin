package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity

interface PhoneNumberRepository : Repository<UserCreateDateEntity, String, Any> {
    suspend fun validatePhoneNumber(phoneNumber: String): Resource<UserCreateDateEntity>
}