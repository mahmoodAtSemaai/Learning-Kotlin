package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ReferralEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ReferralCodeRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ReferralCodeRepository
import com.webkul.mobikul.odoo.model.ReferralResponse
import javax.inject.Inject

class ReferralCodeRepositoryImpl @Inject constructor(
    remoteDataSource: ReferralCodeRemoteDataSource
) : ReferralCodeRepository, BaseRepository<ReferralEntity, String, Any, ReferralResponse>() {

    override val entityParser =
        ModelEntityParser(ReferralEntity::class.java, ReferralResponse::class.java)
    override var dataSource: IDataSource<ReferralResponse, String, Any> = remoteDataSource

    override suspend fun getById(id: String): Resource<ReferralEntity> {
        return super<BaseRepository>.getById(id)
    }
}