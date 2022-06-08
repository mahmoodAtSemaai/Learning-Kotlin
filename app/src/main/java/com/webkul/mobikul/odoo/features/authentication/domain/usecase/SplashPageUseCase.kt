package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.domain.repo.SplashPageRepository
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SplashPageUseCase @Inject constructor(
    private val splashPageRepository: SplashPageRepository
) {

    operator fun invoke(): Flow<Resource<SplashScreenResponse>> = flow {
        emit(Resource.Loading)
        val result = splashPageRepository.getSplashPageData()
        emit(result)
    }.flowOn(Dispatchers.IO)

}