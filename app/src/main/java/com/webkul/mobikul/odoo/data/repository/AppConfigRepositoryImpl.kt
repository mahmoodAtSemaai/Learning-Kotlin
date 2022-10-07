package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AppConfigEntity
import com.webkul.mobikul.odoo.data.localSource.localDataSource.AppConfigLocalDataSource
import com.webkul.mobikul.odoo.data.request.AppConfigRequest
import com.webkul.mobikul.odoo.data.response.AppConfigResponse
import com.webkul.mobikul.odoo.domain.repository.AppConfigRepository
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(
    appConfigLocalDataSource: AppConfigLocalDataSource
) : AppConfigRepository,
    BaseRepository<AppConfigEntity, String, AppConfigRequest, AppConfigResponse>() {

    override val entityParser: ModelEntityParser<AppConfigEntity, AppConfigResponse>
        get() = ModelEntityParser(AppConfigEntity::class.java, AppConfigResponse::class.java)

    override var dataSource: IDataSource<AppConfigResponse, String, AppConfigRequest> =
        appConfigLocalDataSource

    override suspend fun get(): Resource<AppConfigEntity> {
        return super<BaseRepository>.get()
    }

    override suspend fun update(request: AppConfigRequest): Resource<AppConfigEntity> {
        return super<BaseRepository>.update(request)
    }
}