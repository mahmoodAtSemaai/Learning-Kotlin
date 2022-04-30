package com.webkul.mobikul.odoo.features.auth.domain.repo

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse

interface LoginRepository : Repository {

  suspend fun logIn(username: String, password: String): Resource<LoginResponse>
}