package com.webkul.mobikul.odoo.features.auth.domain.usecase


import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CountryStateUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {

    operator fun invoke(): Flow<Resource<CountryStateData>> = flow {

        emit(Resource.Loading)
        val result = signUpRepository.getCountryStateData()
        emit(result)

    }.flowOn(Dispatchers.IO)

}