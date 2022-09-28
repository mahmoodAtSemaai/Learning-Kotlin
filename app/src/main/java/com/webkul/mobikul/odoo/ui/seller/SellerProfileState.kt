package com.webkul.mobikul.odoo.ui.seller

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus

sealed class SellerProfileState :IState{
	
	object Idle : SellerProfileState()
	object Loading : SellerProfileState()
	object SetActionBar : SellerProfileState()
	object ShowChatButton : SellerProfileState()
	object HideChatButton : SellerProfileState()
	object SetOnclickListeners : SellerProfileState()
	
	data class Error(val message: String?, val failureStatus: FailureStatus) : SellerProfileState()
	
}
