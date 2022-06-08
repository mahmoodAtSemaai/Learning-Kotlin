package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomePageDataUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {

    operator fun invoke(): Flow<Resource<HomePageResponse>> = flow {
        emit(Resource.Loading)
        val result = homePageRepository.getHomePageData()
        emit(result)
    }.flowOn(Dispatchers.IO)

}