package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAnalyticsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserAnalyticsRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.UserAnalyticsRepository
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse
import javax.inject.Inject

class UserAnalyticsRepositoryImpl @Inject constructor(
    private val userAnalyticsDataSource: UserAnalyticsRemoteDataSource
) : UserAnalyticsRepository {

    override suspend fun getUserAnalytics(): Resource<UserAnalyticsEntity> {
        val result = userAnalyticsDataSource.getUserAnalytics()

        return result
    }


}