package com.webkul.mobikul.odoo.features.auth.domain.repo

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import com.webkul.mobikul.odoo.model.request.SignUpRequest

interface SignUpRepository : Repository {

  suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse>

  suspend fun getAddressBookData(baseLazyRequest: BaseLazyRequest) : Resource<MyAddressesResponse>

  suspend fun getCountryStateData() : Resource<CountryStateData>

  suspend fun getTermAndCondition() : Resource<TermAndConditionResponse>

  suspend fun getSellerTerms() : Resource<TermAndConditionResponse>
}