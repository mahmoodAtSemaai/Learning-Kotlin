package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.BannerListEntity

interface BannerRepository : Repository<Any,Any,Any> {

    suspend fun getBanner(): Resource<BannerListEntity>

}