package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.OtpRemoteDataSource
import com.webkul.mobikul.odoo.data.response.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.domain.repository.OtpRepository
import javax.inject.Inject

class OtpRepositoryImpl @Inject constructor(
    private val remoteDataSource: OtpRemoteDataSource
) : OtpRepository, BaseRepository<Any, String, String, Any>() {

    override val entityParser
        get() = ModelEntityParser(Any::class.java, Any::class.java)

    override var dataSource: IDataSource<Any, String, String> = remoteDataSource

    override suspend fun getById(id: String): Resource<Any> {
        return super<BaseRepository>.getById(id)
    }

    override suspend fun create(request: String): Resource<Any> {
        return super<BaseRepository>.create(request)
    }
}