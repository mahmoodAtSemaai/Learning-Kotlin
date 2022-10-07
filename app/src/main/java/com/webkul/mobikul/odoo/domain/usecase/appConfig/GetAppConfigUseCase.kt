package com.webkul.mobikul.odoo.domain.usecase.appConfig

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AppConfigEntity
import com.webkul.mobikul.odoo.domain.repository.AppConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAppConfigUseCase @Inject constructor(
        private val appConfigRepository: AppConfigRepository
) {

    operator fun invoke(): Flow<Resource<AppConfigEntity>> = flow {
        emit(Resource.Loading)
        val result = appConfigRepository.get()
        emit(result)
    }.flowOn(Dispatchers.IO)
}