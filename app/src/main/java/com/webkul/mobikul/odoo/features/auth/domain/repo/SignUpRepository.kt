package com.webkul.mobikul.odoo.features.auth.domain.repo

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.request.SignUpRequest

interface SignUpRepository : Repository {

  suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse>
}