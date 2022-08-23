package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyUserAddressUseCase @Inject constructor() {
    operator fun invoke(isProvinceAvailable:Boolean, userAddressRequest: UserAddressRequest): Flow<Boolean> = flow {
        var result = false
        if(isProvinceAvailable){
            result =  !(userAddressRequest.stateId.isNullOrEmpty() || userAddressRequest.districtId.isNullOrEmpty()
                    || userAddressRequest.subDistrictId.isNullOrEmpty() || userAddressRequest.villageId.isNullOrEmpty()
                    || userAddressRequest.street.isNullOrEmpty())
        }else{
            result = true
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}