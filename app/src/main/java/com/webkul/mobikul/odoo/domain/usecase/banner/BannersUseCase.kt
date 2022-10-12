package com.webkul.mobikul.odoo.domain.usecase.banner

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.BannerListEntity
import com.webkul.mobikul.odoo.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BannersUseCase @Inject constructor(
        private val bannerRepository: BannerRepository
) {

    operator fun invoke(): Flow<Resource<BannerListEntity>> = flow {
        emit(Resource.Loading)
        val result = bannerRepository.getBanner()
        emit(result)
    }
}