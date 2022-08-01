package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.FCMTokenRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.FCMTokenRepository
import javax.inject.Inject

class FCMTokenRepositoryImpl @Inject constructor(
        private val remoteDataSource: FCMTokenRemoteDataSource,
        private val appPreferences: AppPreferences
) : FCMTokenRepository {
    override suspend fun registerDeviceToken(): Resource<Any> {
        val result = remoteDataSource.registerDeviceToken()
        when (result) {
            is Resource.Success -> appPreferences.isFCMTokenSynced = true
            else -> appPreferences.isFCMTokenSynced = false
        }
        return result
    }


}