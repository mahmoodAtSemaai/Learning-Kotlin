package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.repository.CustomerGroupListResponse
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.response.UserCustomerGroupResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserCategoryServices {

	@GET(ApiInterface.CUSTOMER_GROUPS)
	suspend fun getCustomerGroups(): BaseUserOnboardingResponse<CustomerGroupListResponse>

	@POST(ApiInterface.USER_CUSTOMER_GROUP)
	suspend fun setUserCustomerGroup(
		@Path(ApiInterface.USER_ID) userId: String,
		@Body userCustomerGroupRequest: String
	): BaseUserOnboardingResponse<UserCustomerGroupResponse>
}