package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.domain.repository.UserAddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinueUserAddressUseCase @Inject constructor(
    private val userAddressRepository: UserAddressRepository
) {
    operator fun invoke(
        partnerId: String,
        userAddressRequest: UserAddressRequest
    ): Flow<Resource<UserAddressEntity>> = flow {

        emit(Resource.Loading)
        val result = userAddressRepository.setUserAddress(partnerId, userAddressRequest)
        emit(result)

    }.flowOn(Dispatchers.IO)
}