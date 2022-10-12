package com.webkul.mobikul.odoo.data.localSource.localDataSource

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.localSource.localDataSourceInterface.HomeDataCache
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import javax.inject.Inject

class HomeLocalDataSource @Inject constructor(
        private val sqlLiteDbHelper: SqlLiteDbHelper,
) : HomeDataCache {

    override suspend fun update(request: HomePageResponse): Resource<HomePageResponse> {
        sqlLiteDbHelper.insertHomePageData(request)
        return Resource.Success(request)
    }
}