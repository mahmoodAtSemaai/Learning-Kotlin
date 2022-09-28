package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApplicationSingleton
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.local.SaveData
import com.webkul.mobikul.odoo.core.utils.LocaleManager
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SplashPageRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SplashDataRepository
import javax.inject.Inject

class SplashDataRepositoryImpl @Inject constructor(
        private val remoteDataSource: SplashPageRemoteDataSource,
        private val saveData: SaveData,
        private val appPreferences: AppPreferences,
        private val localeManager: LocaleManager,
        private val resourcesProvider: ResourcesProvider
) : SplashDataRepository {

    override suspend fun getSplashPageData(): Resource<SplashEntity> {
        val result = remoteDataSource.getSplashPageData()
        when (result) {
            is Resource.Success -> {
                saveData.saveSplashEntity(result.value)
                setAppPreference(result.value)
            }
        }
        return result
    }

    private fun setAppPreference(splashEntity: SplashEntity) {
        appPreferences.isAllowResetPassword = splashEntity.isAllowResetPwd
        appPreferences.isAllowedGuestCheckout = splashEntity.isAllowGuestCheckout
        appPreferences.isAllowedSignup = splashEntity.isAllowSignup
        appPreferences.isAllowedWishlist = splashEntity.isAllowWishlistModule()
        appPreferences.isAllowedReview = splashEntity.isAllowReviewModule()
        appPreferences.isEmailVerified = splashEntity.isEmailVerified()
        appPreferences.isMarketplaceAllowed = splashEntity.isMarketplaceAllowed()
        appPreferences.isAllowShipping = splashEntity.isAllowShipping
        appPreferences.isUserApproved = splashEntity.isUserApproved
        appPreferences.isUserOnboarded = splashEntity.isUserOnboarded
        appPreferences.customerGroupName = splashEntity.customerGroupName
        appPreferences.groupName = splashEntity.groupName
        if(splashEntity.customerGroupId == null){
            appPreferences.customerGroupId = -1
        }else{
            appPreferences.customerGroupId = splashEntity.customerGroupId!!
        }
        appPreferences.userName = splashEntity.userName

        appPreferences.userId = splashEntity.userId
        if (appPreferences.languageCode.isNullOrBlank() and splashEntity.defaultLanguage.isNotEmpty()) {
            appPreferences.languageCode = splashEntity.defaultLanguage[0]
            localeManager.setLocale(false)
        }
        appPreferences.isLanguageChange = false
        appPreferences.isTermAndCondition = splashEntity.termsAndConditions
        appPreferences.privacyPolicyUrl = splashEntity.privacyPolicyUrl

        ApplicationSingleton.getInstance().sortData = splashEntity.sortData
        ApplicationSingleton.getInstance().ratingStatus = splashEntity.ratingStatus

        if (!splashEntity.customerName.isNullOrBlank()) {
            //TODO optimize changes
            if(appPreferences.groupName == resourcesProvider.getString(R.string.toko_tani) || appPreferences.groupName == resourcesProvider.getString(
                    R.string.kelompok_tani)){
                appPreferences.customerName = splashEntity.userName
            }else{
                appPreferences.customerName = splashEntity.customerName
            }
        }
        if (!splashEntity.customerEmail.isNullOrBlank()) {
            appPreferences.customerEmail = splashEntity.customerEmail
        }
        if (!splashEntity.customerPhoneNumber.isNullOrBlank()) {
            appPreferences.customerPhoneNumber = splashEntity.customerPhoneNumber
        }
        if (!splashEntity.customerProfileImage.isNullOrBlank()) {
            appPreferences.customerProfileImage = splashEntity.customerProfileImage
        }
        if (!splashEntity.customerBannerImage.isNullOrBlank()) {
            appPreferences.customerBannerImage = splashEntity.customerBannerImage
        }
        if (splashEntity.customerId.isNotBlank()) {
            appPreferences.customerId = splashEntity.customerId
        }
        if (BuildConfig.isMarketplace) {
            appPreferences.isSeller = splashEntity.isSeller
        }
    }
}