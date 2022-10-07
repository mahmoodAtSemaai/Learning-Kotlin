package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.TermsConditionRemoteDataSource
import com.webkul.mobikul.odoo.data.response.TermsAndConditionsResponse
import com.webkul.mobikul.odoo.domain.repository.TermsConditionRepository
import javax.inject.Inject

class TermsConditionRepositoryImpl @Inject constructor(
    remoteDataSource: TermsConditionRemoteDataSource
) : TermsConditionRepository,
    BaseRepository<TermsAndConditionsEntity, Any, Any, TermsAndConditionsResponse>() {

    override val entityParser: ModelEntityParser<TermsAndConditionsEntity, TermsAndConditionsResponse>
        get() = ModelEntityParser(
            TermsAndConditionsEntity::class.java,
            TermsAndConditionsResponse::class.java
        )

    override var dataSource: IDataSource<TermsAndConditionsResponse, Any, Any> = remoteDataSource

    override suspend fun get(): Resource<TermsAndConditionsEntity> {
        return super<BaseRepository>.get()
    }
}