package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAnalyticsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserAnalyticsRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.UserAnalyticsRepository
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse
import javax.inject.Inject

class UserAnalyticsRepositoryImpl @Inject constructor(
    userAnalyticsDataSource: UserAnalyticsRemoteDataSource
) : UserAnalyticsRepository,
    BaseRepository<UserAnalyticsEntity, Any, Any, UserAnalyticsResponse>() {

    override val entityParser
        get() = ModelEntityParser(
            UserAnalyticsEntity::class.java,
            UserAnalyticsResponse::class.java
        )

    override var dataSource: IDataSource<UserAnalyticsResponse, Any, Any> = userAnalyticsDataSource

    override suspend fun get(): Resource<UserAnalyticsEntity> {
        return super<BaseRepository>.get()
    }

}