package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.DistrictListEntity
import com.webkul.mobikul.odoo.domain.repository.DistrictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DistrictDataUseCase @Inject constructor(
    private val districtRepository: DistrictRepository
) {
    operator fun invoke(
        stateId: Int
    ): Flow<Resource<DistrictListEntity>> = flow {

        emit(Resource.Loading)
        val result = districtRepository.getById(stateId)
        emit(result)

    }.flowOn(Dispatchers.IO)
}