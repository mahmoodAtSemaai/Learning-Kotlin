package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.BagItemsCountRepository
import javax.inject.Inject

class BagItemsCountRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences,
) : BagItemsCountRepository {

    override suspend fun get(): Resource<Int> {
        return try {
            val result = appPreferences.newCartCount
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Failure(FailureStatus.API_FAIL, null, e.localizedMessage)
        }
    }

}