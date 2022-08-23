package com.webkul.mobikul.odoo.ui.signUpOnboarding.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.request.UserDetailsRequest

sealed class UserDetailsIntent : IIntent {
    object FetchViews: UserDetailsIntent()
    object SetUserPicture : UserDetailsIntent()
    object GetUserIdCustomerId: UserDetailsIntent()
    object HideInvalidReferralCode: UserDetailsIntent()
    object CloseApp : UserDetailsIntent()
    object CloseCamera : UserDetailsIntent()
    data class Base64UserPicture(val base64Image : String): UserDetailsIntent()
    data class VerifyReferralCode(val referralCode : String) : UserDetailsIntent()
    data class VerifyFields(val name: String,val groupName: String) : UserDetailsIntent()
    data class UserDetails(val userDetailsRequest: UserDetailsRequest) : UserDetailsIntent()
}