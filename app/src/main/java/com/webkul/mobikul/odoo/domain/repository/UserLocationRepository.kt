package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserLocationEntity
import com.webkul.mobikul.odoo.data.request.UserLocationRequest

interface UserLocationRepository : Repository<UserLocationEntity, Any, UserLocationRequest>