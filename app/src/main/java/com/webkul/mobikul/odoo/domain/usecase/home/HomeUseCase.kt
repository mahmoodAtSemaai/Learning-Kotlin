package com.webkul.mobikul.odoo.domain.usecase.home

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.HomeDataRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeUseCase @Inject constructor(
        private val homeDataRepository: HomeDataRepository,
        private val homeLocalDataUseCase: HomeLocalDataUseCase
) {
    operator fun invoke(): Flow<Resource<HomePageResponse>> = flow {

        emit(Resource.Loading)
        //TODO->Handle homepage Response with home revamp
        //TODO -> handling local SQlite thing. Need to move this in repo layer.
        when (val result = homeDataRepository.getHomePageData()) {
            is Resource.Failure -> {
                when (result.failureStatus) {
                    FailureStatus.NO_INTERNET -> {
                        homeLocalDataUseCase().collect {
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