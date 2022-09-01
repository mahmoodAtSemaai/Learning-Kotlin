package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.VillageListEntity

interface VillageRepository : Repository {
    suspend fun getVillages(subDistrictId: Int) : Resource<VillageListEntity>
}