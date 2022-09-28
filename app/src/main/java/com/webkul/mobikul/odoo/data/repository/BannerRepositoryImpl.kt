package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.BannerListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.BannerRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.BannerRepository
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
        private val bannerRemoteDataSource: BannerRemoteDataSource
) : BannerRepository {
    override suspend fun get(): Resource<BannerListEntity> {
        return bannerRemoteDataSource.get()
    }
}