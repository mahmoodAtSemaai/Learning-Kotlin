package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.FCMTokenRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.FCMTokenRepository
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import javax.inject.Inject

class FCMTokenRepositoryImpl @Inject constructor(
    remoteDataSource: FCMTokenRemoteDataSource
) : FCMTokenRepository, BaseRepository<Any, Any, RegisterDeviceTokenRequest, Any>() {

    override val entityParser = ModelEntityParser(Any::class.java, Any::class.java)
    override var dataSource: IDataSource<Any, Any, RegisterDeviceTokenRequest> = remoteDataSource

    override suspend fun create(request: RegisterDeviceTokenRequest): Resource<Any> {
        return super<BaseRepository>.create(request)
    }
}