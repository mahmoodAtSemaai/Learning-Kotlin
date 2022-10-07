package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCustomerGroupEntity
import com.webkul.mobikul.odoo.data.request.CustomerGroupRequest
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.repository.UserCustomerGroupRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinueCustomerGroupUseCase @Inject constructor(
    private val userCustomerGroupRepository: UserCustomerGroupRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(
        customerGroupRequest: CustomerGroupRequest
    ): Flow<Resource<UserCustomerGroupEntity>> = flow {

        emit(Resource.Loading)
        val result = userCustomerGroupRepository.update(customerGroupRequest)
        when (result) {
            is Resource.Success -> {
                userRepository.update(
                    UserRequest(
                        groupName = customerGroupRequest.customerGrpName,
                        customerGroupId = customerGroupRequest.customerGrpType.toInt()
                    )
                )
            }
        }
        emit(result)

    }.flowOn(Dispatchers.IO)
}