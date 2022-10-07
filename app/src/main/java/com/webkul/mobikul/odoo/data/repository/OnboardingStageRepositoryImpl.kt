package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.OnboardingStageRemoteDataSource
import com.webkul.mobikul.odoo.data.response.OnboardingStagesResponse
import com.webkul.mobikul.odoo.domain.repository.OnboardingStageRepository
import javax.inject.Inject

class OnboardingStageRepositoryImpl @Inject constructor(
    private val remoteDataSource: OnboardingStageRemoteDataSource
) : OnboardingStageRepository,
    BaseRepository<OnboardingStagesEntity, Any, Any, OnboardingStagesResponse>() {

    override val entityParser =
        ModelEntityParser(OnboardingStagesEntity::class.java, OnboardingStagesResponse::class.java)
    override var dataSource: IDataSource<OnboardingStagesResponse, Any, Any> = remoteDataSource

    override suspend fun get(): Resource<OnboardingStagesEntity> {
        return super<BaseRepository>.get()
    }
}