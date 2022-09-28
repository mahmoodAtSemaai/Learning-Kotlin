package com.webkul.mobikul.odoo.domain.usecase.cart

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.repository.BagItemsCountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BagItemsCountUseCase @Inject constructor(
    private val bagItemsCountRepository: BagItemsCountRepository,
    private val resourcesProvider: ResourcesProvider
) : UseCase {

    operator fun invoke(): Flow<Resource<String>> = flow {
        emit(Resource.Loading)

        when (val result = bagItemsCountRepository.get()) {
            is Resource.Failure -> emit(result)
            is Resource.Success -> {
                if (result.value != ApplicationConstant.MIN_ITEM_TO_BE_SHOWN_IN_CART) {
                    if (result.value < ApplicationConstant.MAX_ITEM_TO_BE_SHOWN_IN_CART)
                        emit(Resource.Success(result.value.toString()))
                    else emit(Resource.Success(resourcesProvider.getString(R.string.text_nine_plus)))
                } else emit(Resource.Default)
            }
        }

    }.flowOn(Dispatchers.IO)
}