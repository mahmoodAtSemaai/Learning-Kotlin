package com.webkul.mobikul.odoo.data.localSource.localDataSource

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.data.response.UserResponse
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
	private val appPreferences: AppPreferences
) : IDataSource<UserResponse, String, UserRequest> {

	override suspend fun get(): Resource<UserResponse> {
		return Resource.Success(getUser())
	}

	private fun getUser(): UserResponse {
		return UserResponse(
			customerLoginToken = appPreferences.customerLoginToken ?: "",
			customerJWTToken = appPreferences.authToken ?: "",
			customerName = appPreferences.customerName ?: "",
			customerId = if (appPreferences.customerId.isNullOrBlank()) -1 else appPreferences.customerId!!.toInt(),
			customerEmail = appPreferences.customerEmail ?: "",
			customerPhoneNumber = appPreferences.customerPhoneNumber ?: "",
			customerProfileImage = appPreferences.customerProfileImage ?: "",
			customerBannerImage = appPreferences.customerBannerImage ?: "",
			userAnalyticsId = appPreferences.analyticsId ?: "",
			isSeller = appPreferences.isSeller,
			isUserApproved = appPreferences.isUserApproved,
			isUserLoggedIn = appPreferences.isLoggedIn,
			isUserOnboarded = appPreferences.isUserOnboarded,
			customerGrpId = appPreferences.customerGroupId,
			customerGroupName = appPreferences.customerGroupName,
			groupName = appPreferences.groupName,
			userId = appPreferences.userId ?: ""
		)
	}

	override suspend fun update(request: UserRequest): Resource<UserResponse> {
		return Resource.Success(updateUser(request))
	}

	private fun updateUser(request: UserRequest): UserResponse {
		request.apply {
			customerLoginToken?.let { appPreferences.customerLoginToken = it }
			customerJWTToken?.let { appPreferences.authToken = it }
			customerName?.let { appPreferences.customerName = it }
			customerId?.let { appPreferences.customerId = it }
			customerEmail?.let { appPreferences.customerEmail = it }
			customerPhoneNumber?.let { appPreferences.customerPhoneNumber = it }
			customerProfileImage?.let { appPreferences.customerProfileImage = it }
			customerBannerImage?.let { appPreferences.customerBannerImage = it }
			userAnalyticsId?.let { appPreferences.analyticsId = it }
			isSeller?.let { appPreferences.isSeller = it }
			isUserApproved?.let { appPreferences.isUserApproved = it }
			isUserOnboarded?.let { appPreferences.isUserOnboarded = it }
			customerGroupName?.let { appPreferences.customerGroupName = it }
			customerGroupId?.let { appPreferences.customerGroupId = it }
			groupName?.let { appPreferences.groupName = it }
			userId?.let { appPreferences.userId = it }
			appPreferences.isFCMTokenSynced = fcmTokenSynced
		}
		return getUser()
	}
}