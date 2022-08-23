package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.UserDetailsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserDetailsRemoteDataSource
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest
import com.webkul.mobikul.odoo.domain.repository.UserDetailsRepository
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserDetailsRemoteDataSource,
    private val appPreferences: AppPreferences,
    private val resourcesProvider: ResourcesProvider
) : UserDetailsRepository {

    override suspend fun setUserDetails(
        userId: String,
        partnerId: String,
        userDetailsRequest: UserDetailsRequest
    ): Resource<UserDetailsEntity> {
        val result = remoteDataSource.setUserDetailsGroup(userId, partnerId, userDetailsRequest)
        when (result) {
            is Resource.Success -> {
                //TODO optimize
                if(appPreferences.groupName == resourcesProvider.getString(R.string.toko_tani) || appPreferences.groupName == resourcesProvider.getString(R.string.kelompok_tani)){
                    appPreferences.userName = userDetailsRequest.name
                }
                appPreferences.customerName = userDetailsRequest.name
                appPreferences.customerGroupName = userDetailsRequest.groupName
            }
        }
        return result
    }
}