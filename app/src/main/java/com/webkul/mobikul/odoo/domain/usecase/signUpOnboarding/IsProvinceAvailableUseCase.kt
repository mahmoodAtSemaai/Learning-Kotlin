package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.data.entity.address.StateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IsProvinceAvailableUseCase @Inject constructor(){
    operator fun invoke(position: Int, addressList: List<StateEntity>): Flow<Boolean> = flow {
        val result = addressList[position].isAvailable
        emit(result)
    }.flowOn(Dispatchers.IO)
}