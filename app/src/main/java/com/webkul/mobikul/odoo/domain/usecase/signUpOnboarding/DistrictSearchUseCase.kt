package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.data.entity.address.DistrictEntity
import com.webkul.mobikul.odoo.domain.enums.AddressFieldValidation
import com.webkul.mobikul.odoo.domain.enums.AddressFieldValidationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class DistrictSearchUseCase @Inject constructor() {
    @Throws(AddressFieldValidationException::class)
    operator fun invoke(query: String, districtData: List<DistrictEntity>): Flow<List<DistrictEntity>> = flow {
        var filteredList: List<DistrictEntity> = emptyList()
        for (item in districtData) {
            if (item.name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))) {
                filteredList = filteredList + item
            }
        }
        if(filteredList.isEmpty()){
            throw AddressFieldValidationException(AddressFieldValidation.NO_RESULTS_FOUND.value.toString())
        }else{
            emit(filteredList)
        }
    }.flowOn(Dispatchers.IO)
}