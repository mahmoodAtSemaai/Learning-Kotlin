package com.webkul.mobikul.odoo.features.authentication.domain.repo

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse

interface HomePageRepository : Repository {

    suspend fun getHomePageData(): Resource<HomePageResponse>

}