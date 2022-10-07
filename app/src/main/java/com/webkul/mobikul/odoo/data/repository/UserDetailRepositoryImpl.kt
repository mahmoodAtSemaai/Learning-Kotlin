package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddOnsEntity
import com.webkul.mobikul.odoo.data.entity.LanguageEntity
import com.webkul.mobikul.odoo.data.entity.UserDetailEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.UserDetailRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.UserDetailRepository
import javax.inject.Inject

class UserDetailRepositoryImpl @Inject constructor(
    private val userDetailRemoteDataSource: UserDetailRemoteDataSource,
    private val appPreferences: AppPreferences
) : UserDetailRepository {
    override suspend fun getUser(userId: String): Resource<UserDetailEntity> {
        val result = userDetailRemoteDataSource.get(userId)
        when (result) {
            is Resource.Success -> saveUserDetails(result.value)
        }
        return result
    }

    override suspend fun getUserDetails(): Resource<UserDetailEntity> {
        return Resource.Success(
            UserDetailEntity(
                AddOnsEntity(
                    appPreferences.isAllowedWishlist,
                    appPreferences.isAllowedReview,
                    appPreferences.isEmailVerified,
                    appPreferences.isMarketplaceAllowed,
                    false,
                    false,
                    false,
                    false
                ),
                appPreferences.customerId?.toInt() ?: -1,
                appPreferences.userId ?: "",
                appPreferences.cartCount,
                appPreferences.isUserApproved,
                0,
                appPreferences.isSeller,
                LanguageEntity(appPreferences.languageCode ?: "", ""),
                emptyList(),
                appPreferences.isTermAndCondition,
                appPreferences.newCartCount,
                appPreferences.cartId
            )
        )
    }

    private fun saveUserDetails(value: UserDetailEntity) {
        appPreferences.apply {
            isAllowedWishlist = value.addons.wishlist
            isAllowedReview = value.addons.review
            isEmailVerified = value.addons.emailVerification
            isMarketplaceAllowed = value.addons.odooMarketplace
            customerId = value.customerId.toString()
            userId = value.userId
            cartCount = value.cartCount
            newCartCount = value.cartCount
            isUserApproved = value.isApproved
            isSeller = value.isSeller
            if (languageCode.isNullOrBlank() and value.defaultLanguage.languageCode.isNotBlank()) {
                languageCode = value.defaultLanguage.languageCode
            }
            isLanguageChange = false
            isTermAndCondition = value.termsAndConditions
        }
    }
}