package com.webkul.mobikul.odoo.domain.usecase.address

import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckStateAvailabilityUseCase @Inject constructor() : UseCase {
	
	operator fun invoke(
			stateListEntity : StateListEntity,
			addressFormEntity : AddressFormEntity,
	) : Flow<Resource<Boolean>> = flow {
		
		emit(Resource.Loading)
		
		var result = true
		
		for (stateData in stateListEntity.data) {
			if (stateData.isAvailable && (stateData.id.toString() == addressFormEntity.stateId || isStateDataMissing(addressFormEntity)) && areFieldsNullOrEmpty(addressFormEntity)) {
				emit(Resource.Success(false))
				result = false
				break
			}
		}
		
		if (result) emit(Resource.Success(true))
		
	}
	
	private fun areFieldsNullOrEmpty(addressFormEntity : AddressFormEntity) : Boolean {
		return addressFormEntity.districtId == null || addressFormEntity.districtId.isEmpty() || addressFormEntity.subDistrictId == null || addressFormEntity.subDistrictId.isEmpty() || addressFormEntity.villageId == null || addressFormEntity.villageId.isEmpty()
	}
	
	private fun isStateDataMissing(addressFormEntity : AddressFormEntity) : Boolean {
		return addressFormEntity.stateId == null || addressFormEntity.stateId.isEmpty()
	}
}