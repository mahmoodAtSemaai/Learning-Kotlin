package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.DistrictListEntity

interface DistrictRepository : Repository {
    suspend fun getDistricts(stateId: Int) : Resource<DistrictListEntity>
}