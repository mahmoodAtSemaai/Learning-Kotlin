package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictListEntity

interface SubDistrictRepository : Repository {
    suspend fun getSubDistricts(districtId: Int) : Resource<SubDistrictListEntity>
}