package com.webkul.mobikul.odoo.domain.usecase.address

import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StateListUseCase @Inject constructor(private val repository : AddressRepository) : UseCase {
	
	operator fun invoke(companyId : Int) : Flow<Resource<StateListEntity>> = flow {
		
		emit(Resource.Loading)
		val result = repository.getStates(companyId)
		emit(result)
	}
}