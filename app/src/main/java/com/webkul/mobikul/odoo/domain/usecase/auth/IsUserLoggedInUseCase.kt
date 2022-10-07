package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor() {
    operator fun invoke(userEntity: UserEntity?): Flow<Resource<Boolean>> = flow {

        try {
            val result = userEntity?.isUserLoggedIn ?: false
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Failure(FailureStatus.ACCESS_DENIED))
        }
    }
}