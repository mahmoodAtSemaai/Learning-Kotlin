package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SplashEntity

interface SplashDataRepository : Repository {

  suspend fun getSplashPageData(): Resource<SplashEntity>
}