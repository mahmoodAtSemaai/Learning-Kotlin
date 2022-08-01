package com.webkul.mobikul.odoo.domain.usecase.fcmToken

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.FCMTokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
        private val fcmTokenRepository: FCMTokenRepository
){
    operator fun invoke(): Flow<Resource<Any>> = flow {
        emit(Resource.Loading)
        val result = fcmTokenRepository.registerDeviceToken()
        emit(result)
    }.flowOn(Dispatchers.IO)
}