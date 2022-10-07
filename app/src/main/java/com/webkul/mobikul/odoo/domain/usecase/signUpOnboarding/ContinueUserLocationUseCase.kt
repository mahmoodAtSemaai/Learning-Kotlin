package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserLocationEntity
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.repository.UserLocationRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinueUserLocationUseCase @Inject constructor(
    private val userLocationRepository: UserLocationRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(
        userLocationRequest: UserLocationRequest
    ): Flow<Resource<UserLocationEntity>> = flow {

        emit(Resource.Loading)
        val result = userLocationRepository.create(userLocationRequest)
        when (result) {
            is Resource.Success -> {
                userRepository.update(UserRequest(isUserOnboarded = true))
            }
        }
        emit(result)

    }.flowOn(Dispatchers.IO)
}