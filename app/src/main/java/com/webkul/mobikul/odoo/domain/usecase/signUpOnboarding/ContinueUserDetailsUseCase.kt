package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinueUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userRequest: UserRequest): Flow<Resource<UserEntity>> = flow {

        emit(Resource.Loading)
        val result = userRepository.updateUserDetails(userRequest)
        when(result){
            is Resource.Success -> {
                userRepository.update(userRequest)
                emit(result)
            }
            else -> emit(result)
        }

    }.flowOn(Dispatchers.IO)
}