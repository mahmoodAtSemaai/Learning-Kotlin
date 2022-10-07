package com.webkul.mobikul.odoo.domain.usecase.fcmToken

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.repository.FCMTokenRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
    private val fcmTokenRepository: FCMTokenRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(registerDeviceTokenRequest: RegisterDeviceTokenRequest): Flow<Resource<Any>> =
        flow {
            emit(Resource.Loading)
            val result = fcmTokenRepository.create(registerDeviceTokenRequest)
            when (result) {
                is Resource.Success -> userRepository.update(UserRequest(fcmTokenSynced = true))
                else -> userRepository.update(UserRequest(fcmTokenSynced = false))
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
}