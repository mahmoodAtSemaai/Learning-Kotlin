package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.data.entity.SignUpEntity
import com.webkul.mobikul.odoo.model.request.SignUpRequest

interface AuthRepository : Repository<Any, Any, Any> {

    suspend fun logIn(username: String, password: String): Resource<LoginEntity>

    suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpEntity>

}