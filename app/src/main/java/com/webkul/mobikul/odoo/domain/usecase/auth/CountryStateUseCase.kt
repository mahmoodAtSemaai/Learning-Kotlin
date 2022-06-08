package com.webkul.mobikul.odoo.domain.usecase.auth


import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CountryStateUseCase @Inject constructor(private val addressRepository: AddressRepository) {

    operator fun invoke(): Flow<Resource<CountryEntity>> = flow {

        emit(Resource.Loading)
        val result = addressRepository.getCountryStateData()
        emit(result)

    }.flowOn(Dispatchers.IO)

}