package com.webkul.mobikul.odoo.domain.usecase.splash

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.domain.repository.SplashDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val splashDataRepository: SplashDataRepository
) {

    operator fun invoke(): Flow<Resource<SplashEntity>> = flow {
        emit(Resource.Loading)
        val result = splashDataRepository.get()
        emit(result)
    }.flowOn(Dispatchers.IO)
}