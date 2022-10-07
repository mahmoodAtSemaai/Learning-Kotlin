package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.data.entity.VillageListEntity
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchVillagesUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {

    operator fun invoke(subDistrictId: Int): Flow<Resource<VillageListEntity>> = flow {

        emit(Resource.Loading)
        val result = addressRepository.getVillages(subDistrictId)
        emit(result)

    }.flowOn(Dispatchers.IO)

}