package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class ValidateMandatoryAddressFieldsUseCase () {

    operator fun invoke(isServiceAvailable: Boolean, addressRequestBody: AddressRequestBody): Flow<Boolean> = flow {

        emit(isValidAddress(isServiceAvailable, addressRequestBody))


    }.flowOn(Dispatchers.Default)

    private fun isValidAddress(isServiceAvailable: Boolean, addressRequestBody: AddressRequestBody) : Boolean {

        return if (isServiceAvailable) {

            addressRequestBody.name.isNullOrEmpty().not()
                    && addressRequestBody.phone.isNullOrEmpty().not()
                    && addressRequestBody.country_id.isNullOrEmpty().not()
                    && addressRequestBody.state_id.isNullOrEmpty().not()
                    && addressRequestBody.district_id.isNullOrEmpty().not()
                    && addressRequestBody.sub_district_id.isNullOrEmpty().not()
                    && addressRequestBody.village_id.isNullOrEmpty().not()
                    && addressRequestBody.zip.isNullOrEmpty().not()

        } else {

            addressRequestBody.name.isNullOrEmpty().not()
                    && addressRequestBody.phone.isNullOrEmpty().not()
                    && addressRequestBody.country_id.isNullOrEmpty().not()
                    && addressRequestBody.state_id.isNullOrEmpty().not()
        }
    }
}
