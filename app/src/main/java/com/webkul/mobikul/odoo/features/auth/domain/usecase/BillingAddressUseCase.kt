package com.webkul.mobikul.odoo.features.auth.domain.usecase

import android.content.Context
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BillingAddressUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository){

    operator fun invoke(): Flow<Resource<MyAddressesResponse>> = flow {

        emit(Resource.Loading)
        val result = signUpRepository.getAddressBookData( BaseLazyRequest(0, 1))
        emit(result)

    }.flowOn(Dispatchers.IO)


}