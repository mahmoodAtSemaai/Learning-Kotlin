package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserOnboardingStageRemoteDataSource
import com.webkul.mobikul.odoo.data.response.UserOnboardingStageResponse
import com.webkul.mobikul.odoo.domain.repository.UserOnboardingStageRepository
import javax.inject.Inject

class UserOnboardingStageRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserOnboardingStageRemoteDataSource
) : UserOnboardingStageRepository,
    BaseRepository<UserOnboardingStageEntity, String, Any, UserOnboardingStageResponse>() {

    override val entityParser =
        ModelEntityParser(
            UserOnboardingStageEntity::class.java,
            UserOnboardingStageResponse::class.java
        )
    override var dataSource: IDataSource<UserOnboardingStageResponse, String, Any> =
        remoteDataSource

    override suspend fun getById(id: String): Resource<UserOnboardingStageEntity> {
        return super<BaseRepository>.getById(id)
    }
}