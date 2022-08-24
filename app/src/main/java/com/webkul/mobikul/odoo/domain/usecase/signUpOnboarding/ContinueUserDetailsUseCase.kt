package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserDetailsEntity
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest
import com.webkul.mobikul.odoo.domain.repository.UserDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinueUserDetailsUseCase @Inject constructor(
    private val userDetailsRepository: UserDetailsRepository
)  {
    operator fun invoke(
        userId: String,
        partnerId:String,
        userDetailsRequest: UserDetailsRequest
    ): Flow<Resource<UserDetailsEntity>> = flow {

        emit(Resource.Loading)
        val result = userDetailsRepository.setUserDetails(userId, partnerId, userDetailsRequest)
        emit(result)

    }.flowOn(Dispatchers.IO)
}