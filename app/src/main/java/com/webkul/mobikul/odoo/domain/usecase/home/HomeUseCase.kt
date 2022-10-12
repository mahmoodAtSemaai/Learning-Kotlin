package com.webkul.mobikul.odoo.domain.usecase.home

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.repository.HomeDataRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val homeDataRepository: HomeDataRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<HomePageResponse>> = flow {
        emit(Resource.Loading)
        val result = homeDataRepository.get()
        when (result) {
            is Resource.Success -> {
                userRepository.update(
                    UserRequest(
                        isUserOnboarded = result.value.isUserOnboarded,
                        isUserApproved = result.value.isUserApproved
                    )
                )
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

}