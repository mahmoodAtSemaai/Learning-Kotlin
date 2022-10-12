package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.data.entity.AppConfigEntity
import com.webkul.mobikul.odoo.data.entity.UserEntity
import com.webkul.mobikul.odoo.data.request.AppConfigRequest
import com.webkul.mobikul.odoo.data.request.UserRequest

interface AppConfigRepository : Repository<AppConfigEntity, String, AppConfigRequest>