package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OnboardingDataList
import com.webkul.mobikul.odoo.data.localSource.localDataSource.OnboardingLocalDataSource
import com.webkul.mobikul.odoo.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(
    onboardingLocalDataSource: OnboardingLocalDataSource
) : OnboardingRepository, BaseRepository<OnboardingDataList, Any, Any, OnboardingDataList>() {
    override val entityParser
        get() = ModelEntityParser(OnboardingDataList::class.java, OnboardingDataList::class.java)

    override var dataSource: IDataSource<OnboardingDataList, Any, Any> = onboardingLocalDataSource

    override suspend fun get(): Resource<OnboardingDataList> {
        return super<BaseRepository>.get()
    }
}