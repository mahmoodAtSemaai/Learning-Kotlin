package com.webkul.mobikul.odoo.domain.usecase.splash

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SplashLocalDataUseCase @Inject constructor(
        private val sqlLiteDbHelper: SqlLiteDbHelper
) {

    operator fun invoke(): Flow<Resource<SplashEntity>> = flow {
        sqlLiteDbHelper.splashPageEntity?.let { emit(Resource.Success(it)) }
                ?: emit(Resource.Failure(FailureStatus.NO_INTERNET, null, ""))
    }.flowOn(Dispatchers.IO)

}