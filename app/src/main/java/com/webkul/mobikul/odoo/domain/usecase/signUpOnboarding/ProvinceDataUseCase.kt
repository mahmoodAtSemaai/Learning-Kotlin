package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.StateListEntity
import com.webkul.mobikul.odoo.domain.repository.ProvinceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProvinceDataUseCase @Inject constructor(
    private val provinceRepository: ProvinceRepository
) {
    operator fun invoke(
        companyId: Int
    ): Flow<Resource<StateListEntity>> = flow {

        emit(Resource.Loading)
        val result = provinceRepository.getStates(companyId)
        emit(result)

    }.flowOn(Dispatchers.IO)
}