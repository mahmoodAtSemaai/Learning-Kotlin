package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource

interface ReferralCodeRepository : Repository {
    suspend fun validateReferralCode(referralCode: String): Resource<Any>
}