package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.data.entity.address.VillageEntity
import com.webkul.mobikul.odoo.domain.enums.AddressFieldValidation
import com.webkul.mobikul.odoo.domain.enums.AddressFieldValidationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class VillageSearchUseCase @Inject constructor() {
    @Throws(AddressFieldValidationException::class)
    operator fun invoke(query: String, villageData: List<VillageEntity>): Flow<List<VillageEntity>> = flow {
        var filteredList: List<VillageEntity> = emptyList()
        for (item in villageData) {
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