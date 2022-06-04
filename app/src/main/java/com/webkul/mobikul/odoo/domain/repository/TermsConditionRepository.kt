package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity

interface TermsConditionRepository : Repository {

    suspend fun getTermAndCondition(): Resource<TermsAndConditionsEntity>

    suspend fun getSellerTerms(): Resource<TermsAndConditionsEntity>

}
