package com.webkul.mobikul.odoo.domain.usecase.user

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
        private val userRepository: UserRepository
) {

    operator fun invoke() : Flow<Resource<UserEntity>> = flow {
        emit(Resource.Loading)
        val result = userRepository.get()
        emit(result)
    }.flowOn(Dispatchers.IO)
}