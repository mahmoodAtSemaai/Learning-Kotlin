package com.webkul.mobikul.odoo.domain.usecase.home

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeLocalDataUseCase @Inject constructor(
        private val sqlLiteDbHelper: SqlLiteDbHelper
) {
    operator fun invoke(): Flow<Resource<HomePageResponse>> = flow {
        sqlLiteDbHelper.homeScreenData?.let { emit(Resource.Success(it)) }
                ?: emit(Resource.Failure(FailureStatus.NO_INTERNET, null, ""))

    }.flowOn(Dispatchers.IO)
}