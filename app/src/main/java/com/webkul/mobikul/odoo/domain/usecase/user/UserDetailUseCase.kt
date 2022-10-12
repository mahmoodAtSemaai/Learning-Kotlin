package com.webkul.mobikul.odoo.domain.usecase.user

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserDetailEntity
import com.webkul.mobikul.odoo.domain.repository.UserDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserDetailUseCase @Inject constructor(
    private val userDetailRepository: UserDetailRepository,
    private val appPreferences: AppPreferences
) {

    operator fun invoke(): Flow<Resource<UserDetailEntity>> = flow {
        emit(Resource.Loading)
        val result = userDetailRepository.getUser(appPreferences.userId ?: "")
        emit(result)
    }
}