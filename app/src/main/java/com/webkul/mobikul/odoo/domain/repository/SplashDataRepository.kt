package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse

interface SplashDataRepository : Repository<SplashEntity, Any, SplashScreenResponse>