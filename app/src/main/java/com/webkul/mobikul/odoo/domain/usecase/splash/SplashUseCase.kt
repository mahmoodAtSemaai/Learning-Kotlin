package com.webkul.mobikul.odoo.domain.usecase.splash

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.domain.repository.SplashDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SplashUseCase @Inject constructor(
        private val splashDataRepository: SplashDataRepository,
        private val splashLocalDataUseCase: SplashLocalDataUseCase
) {

    operator fun invoke(): Flow<Resource<SplashEntity>> = flow {
        emit(Resource.Loading)

        //TODO -> handling local SQlite thing. Need to move this in repo layer.
        when (val result = splashDataRepository.getSplashPageData()) {
            is Resource.Failure -> {
                when (result.failureStatus) {
                    FailureStatus.NO_INTERNET -> {
                        splashLocalDataUseCase().collect {
                            emit(it)
                        }
                    }
                    else -> emit(result)
                }
            }
            else -> emit(result)
        }
    }.flowOn(Dispatchers.IO)
}