package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.domain.repository.CustomerGroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinueCustomerGroupUseCase @Inject constructor(
    private val customerGroupRepository: CustomerGroupRepository
) {
    operator fun invoke(
        userId: String,
        customerGroupRequest: CustomerGroupRequest
    ): Flow<Resource<UserCustomerGroupEntity>> = flow {

        emit(Resource.Loading)
        val result = customerGroupRepository.setUserCustomerGroup(userId, customerGroupRequest)
        emit(result)

    }.flowOn(Dispatchers.IO)
}