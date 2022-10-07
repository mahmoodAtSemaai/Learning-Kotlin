package com.webkul.mobikul.odoo.domain.usecase.account

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAccountInfoDataUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<Resource<AccountInfoEntity>> = flow {
        when (val user = userRepository.get()) {
            is Resource.Success -> {
                emit(
                    Resource.Success(
                        AccountInfoEntity(
                            name = user.value.customerName,
                            phoneNumber = user.value.customerPhoneNumber,
                            groupName = user.value.groupName,
                            customerGroupName = user.value.customerGroupName,
                            userName = user.value.customerName
                        )
                    )
                )
            }
            is Resource.Default -> emit(Resource.Default)
            is Resource.Failure -> emit(
                Resource.Failure(
                    user.failureStatus,
                    user.code,
                    user.message
                )
            )
            is Resource.Loading -> emit(Resource.Loading)
        }
    }.flowOn(Dispatchers.IO)
}