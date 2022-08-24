package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictListEntity
import com.webkul.mobikul.odoo.domain.repository.SubDistrictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SubDistrictDataUseCase @Inject constructor(
    private val subDistrictRepository: SubDistrictRepository
) {
    operator fun invoke(
        districtId: Int
    ): Flow<Resource<SubDistrictListEntity>> = flow {

        emit(Resource.Loading)
        val result = subDistrictRepository.getSubDistricts(districtId)
        emit(result)

    }.flowOn(Dispatchers.IO)
}