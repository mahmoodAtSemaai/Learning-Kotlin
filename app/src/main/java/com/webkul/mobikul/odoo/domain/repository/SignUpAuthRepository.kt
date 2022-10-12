package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest

interface SignUpAuthRepository : Repository<SignUpV1Entity, Any, SignUpOtpAuthRequest>