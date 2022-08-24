package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserLocationEntity
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserLocationRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.UserLocationRepository
import javax.inject.Inject

class UserLocationRepositoryImpl @Inject constructor(private val remoteDataSource: UserLocationRemoteDataSource, private val appPreferences: AppPreferences) :
    UserLocationRepository {
    override suspend fun setUserLocation(
        partnerId: String,
        userLocationRequest: UserLocationRequest
    ): Resource<UserLocationEntity> {
        val result = remoteDataSource.setUserLocation(partnerId, userLocationRequest)
        when (result) {
            is Resource.Success -> {
                appPreferences.isUserOnboarded = true
            }
        }
        return result
    }
}