package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.VillageListEntity
import com.webkul.mobikul.odoo.domain.repository.VillageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VillageDataUseCase @Inject constructor(
    private val villageRepository: VillageRepository
) {
    operator fun invoke(
        subDistrictId: Int
    ): Flow<Resource<VillageListEntity>> = flow {

        emit(Resource.Loading)
        val result = villageRepository.getVillages(subDistrictId)
        emit(result)

    }.flowOn(Dispatchers.IO)
}