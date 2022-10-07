package com.webkul.mobikul.odoo.data.localSource.localDataSource

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApplicationSingleton
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.localSource.localDataSourceInterface.SplashDataCache
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import javax.inject.Inject

class SplashLocalDataSource @Inject constructor(
    private val sqlLiteDbHelper: SqlLiteDbHelper,
    private val appPreferences: AppPreferences,
    private val localeManager: LocaleManager,
    private val resourcesProvider: ResourcesProvider
) : SplashDataCache {

    override suspend fun update(request: SplashScreenResponse): Resource<SplashScreenResponse> {
        sqlLiteDbHelper.insertSplashPageData(request)
        setAppPreference(request)
        return Resource.Success(request)
    }

    private fun setAppPreference(splashEntity: SplashScreenResponse) {
        appPreferences.apply {
            isAllowResetPassword = splashEntity.isAllowResetPwd
            isAllowedGuestCheckout = splashEntity.isAllowGuestCheckout
            isAllowedSignup = splashEntity.isAllowSignup
            isAllowedWishlist = splashEntity.isAllowWishlistModule
            isAllowedReview = splashEntity.isAllowReviewModule
            isEmailVerified = splashEntity.isEmailVerified
            isMarketplaceAllowed = splashEntity.isMarketplaceAllowed
            isAllowShipping = splashEntity.isAllowShipping
            isUserApproved = splashEntity.isUserApproved
            if (languageCode.isNullOrBlank() and splashEntity.defaultLanguage.isNotEmpty()) {
                languageCode = splashEntity.defaultLanguage[0]
                localeManager.setLocale(false)
            }
            isLanguageChange = false
            isTermAndCondition = splashEntity.isTermsAndConditions
            privacyPolicyUrl = splashEntity.isPrivacy_policy_url
            ApplicationSingleton.getInstance().sortData = splashEntity.sortData
            ApplicationSingleton.getInstance().ratingStatus = splashEntity.ratingStatus
            if (splashEntity.customerGroupName.isNullOrBlank().not()) {
                customerGroupName = splashEntity.customerGroupName
            }
            if (splashEntity.groupName.isNullOrBlank().not()) {
                groupName = splashEntity.groupName
            }
            if (splashEntity.groupName.isNullOrBlank().not()) {
                customerName =
                    if (groupName == resourcesProvider.getString(R.string.toko_tani) ||
                        groupName == resourcesProvider.getString(R.string.kelompok_tani)
                    ) {
                        splashEntity.userName
                    } else {
                        splashEntity.customerName
                    }
            }
            if (splashEntity.customerEmail.isNullOrBlank().not()) {
                customerEmail = splashEntity.customerEmail
            }
            if (splashEntity.customerPhoneNumber.isNullOrBlank().not()) {
                customerPhoneNumber = splashEntity.customerPhoneNumber
            }
            if (splashEntity.customerProfileImage.isNullOrBlank().not()) {
                customerProfileImage = splashEntity.customerProfileImage
            }
            if (splashEntity.customerBannerImage.isNullOrBlank().not()) {
                customerBannerImage = splashEntity.customerBannerImage
            }
            if (splashEntity.customerId.isNotBlank().not()) {
                customerId = splashEntity.customerId
            }
            isUserOnboarded = splashEntity.isUserOnboarded
            if (splashEntity.customerGroupId != -1) {
                customerGroupId = splashEntity.customerGroupId
            }
            if (splashEntity.userName.isNullOrBlank().not()) {
                userName = splashEntity.userName
            }
            if (splashEntity.userId.isNullOrBlank().not()) {
                userId = splashEntity.userId
            }
            if (BuildConfig.isMarketplace) {
                isSeller = splashEntity.isSeller
            }
        }
    }


}