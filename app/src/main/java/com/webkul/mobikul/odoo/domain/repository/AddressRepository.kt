package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest

interface AddressRepository : Repository {

    suspend fun getAddressBookData(baseLazyRequest: BaseLazyRequest): Resource<AddressEntity>

    suspend fun getCountryStateData(): Resource<CountryEntity>

}