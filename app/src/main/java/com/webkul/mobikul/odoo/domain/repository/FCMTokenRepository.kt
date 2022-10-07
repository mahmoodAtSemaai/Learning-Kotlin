package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest

interface FCMTokenRepository : Repository<Any,Any, RegisterDeviceTokenRequest>