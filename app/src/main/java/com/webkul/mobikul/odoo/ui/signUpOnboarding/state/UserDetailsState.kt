package com.webkul.mobikul.odoo.ui.signUpOnboarding.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus

sealed class UserDetailsState : IState {
    object Idle : UserDetailsState()
    object Loading : UserDetailsState()
    object FetchViews : UserDetailsState()
    object ValidReferralCode : UserDetailsState()
    object InvalidReferralCode : UserDetailsState()
    object EnableContinue : UserDetailsState()
    object DisableContinue : UserDetailsState()
    object UserPicture : UserDetailsState()
    object CompletedUserDetails : UserDetailsState()
    object CloseApp : UserDetailsState()
    data class SetUpViews(val viewDetails:Pair<Boolean,List<String>>) : UserDetailsState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : UserDetailsState()
}