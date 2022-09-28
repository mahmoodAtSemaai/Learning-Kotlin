package com.webkul.mobikul.odoo.ui.seller

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SellerDetailsEntity
import com.webkul.mobikul.odoo.domain.enums.ReadMoreType
import com.webkul.mobikul.odoo.domain.usecase.product.CheckChatFeatureEnabledUseCase
import com.webkul.mobikul.odoo.domain.usecase.seller.GetSellerProductUseCase
import com.webkul.mobikul.odoo.domain.usecase.seller.GetSellerUseCase
import com.webkul.mobikul.odoo.domain.usecase.utils.WebViewReadMoreUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.IsWishListAllowedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerProfileViewModel @Inject constructor(
	private val checkChatFeatureEnabledUseCase : CheckChatFeatureEnabledUseCase,
	private val sellerUseCase : GetSellerUseCase,
	private val getSellerProductUseCase : GetSellerProductUseCase,
	private val webViewReadMoreUseCase : WebViewReadMoreUseCase,
	private val isWishListAllowedUseCase : IsWishListAllowedUseCase,
) : BaseViewModel(), IModel<SellerProfileState, SellerProfileIntent, SellerProfileEffect> {
	
	
	override val intents : Channel<SellerProfileIntent> = Channel(Channel.UNLIMITED)
	
	private val _state = MutableStateFlow<SellerProfileState>(SellerProfileState.Idle)
	override val state : StateFlow<SellerProfileState>
		get() = _state
	
	private val _effect = Channel<SellerProfileEffect>()
	override val effect : Flow<SellerProfileEffect>
		get() = _effect.receiveAsFlow()
	
	var sellerDetailsEntity : SellerDetailsEntity? = null
	private val REQUEST_CODE_LOGIN = 10001
	
	
	init {
		handlerIntent()
	}
	
	override fun handlerIntent() {
		viewModelScope.launch {
			intents.consumeAsFlow().collect {
				when (it) {
					is SellerProfileIntent.CloseSearch -> closeSearch()
					is SellerProfileIntent.IsChatFeatureEnabled -> checkChatFeatureEnabled()
					is SellerProfileIntent.OpenSearch -> openSearch()
					is SellerProfileIntent.SetActionBar -> setActionBar()
					is SellerProfileIntent.GetSellerProfileData -> getSellerProfile(it.sellerId)
					is SellerProfileIntent.InitOnclickListeners -> initOnclickListeners()
					is SellerProfileIntent.NavigateToChatActivity -> navigateToChatActivity(
						it.sellerId,
						it.sellerName,
						it.sellerProfileImage
					)
					is SellerProfileIntent.ReadMore -> readMore(it.params)
					is SellerProfileIntent.ViewSellerCollections -> viewSellerCollections(it.sellerId)
					is SellerProfileIntent.ViewSellerPolicies -> viewSellerPolicies(
						it.returnPolicy,
						it.shippingPolicy
					)
					is SellerProfileIntent.ViewSellerReviews -> viewSellerReviews(
						it.sellerId,
						it.size
					)
					is SellerProfileIntent.GetSellerProducts -> getSellerProducts(it.sellerId)
					is SellerProfileIntent.NavigateToWishList -> navigateToWishList()
					is SellerProfileIntent.IsWishlistAllowed -> isWishListAllowed()
					is SellerProfileIntent.CheckActivityResult -> checkActivityResult(
						it.requestCode,
						it.resultCode
					)
				}
			}
		}
	}
	
	private suspend fun checkActivityResult(requestCode : Int, resultCode : Int) {
		if (requestCode == REQUEST_CODE_LOGIN) {
			if (resultCode == AppCompatActivity.RESULT_OK) {
				_effect.send(SellerProfileEffect.RecreateActivity)
			}
		}
	}
	
	private suspend fun navigateToWishList() {
		_effect.send(SellerProfileEffect.NavigateToWishList)
	}
	
	private fun getSellerProducts(sellerId : Int?) {
		viewModelScope.launch {
			_state.value = SellerProfileState.Loading
			_state.value = try {
				val intent = getSellerProductUseCase(sellerId)
				var sellerProfileState : SellerProfileState = SellerProfileState.Idle
				
				intent.collect {
					sellerProfileState = when (it) {
						is Resource.Default -> SellerProfileState.Idle
						is Resource.Failure -> SellerProfileState.Error(
							it.message,
							it.failureStatus
						)
						is Resource.Loading -> SellerProfileState.Loading
						is Resource.Success -> {
							_effect.send(SellerProfileEffect.FetchedSellerProducts(it.value))
							SellerProfileState.Idle
						}
					}
				}
				
				sellerProfileState
			} catch (e : Exception) {
				SellerProfileState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
			
		}
	}
	
	private fun getSellerProfile(sellerId : Int?) {
		viewModelScope.launch {
			_state.value = SellerProfileState.Loading
			_state.value = try {
				val intent = sellerUseCase(sellerId)
				var sellerProfileState : SellerProfileState = SellerProfileState.Idle
				
				intent.collect {
					sellerProfileState = when (it) {
						is Resource.Default -> SellerProfileState.Idle
						is Resource.Failure -> SellerProfileState.Error(
							it.message,
							it.failureStatus
						)
						is Resource.Loading -> SellerProfileState.Loading
						is Resource.Success -> {
							sellerDetailsEntity = it.value
							_effect.send(SellerProfileEffect.FetchedSellerDetails(it.value))
							SellerProfileState.Idle
						}
					}
				}
				
				sellerProfileState
			} catch (e : Exception) {
				SellerProfileState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
			
		}
	}
	
	private fun isWishListAllowed() {
		viewModelScope.launch {
			try {
				when (val intent = isWishListAllowedUseCase()) {
					is Resource.Failure -> _effect.send(SellerProfileEffect.HideWishlistButton)
					is Resource.Success -> {
						if (intent.value) _effect.send(SellerProfileEffect.ShowWishlistButton)
						else _effect.send(SellerProfileEffect.HideWishlistButton)
					}
				}
			} catch (e : Exception) {
				_effect.send(SellerProfileEffect.HideWishlistButton)
			}
		}
	}
	
	private suspend fun viewSellerReviews(sellerId : Int?, size : Int?) {
		if (sellerId != null && size != null)
			_effect.send(SellerProfileEffect.ViewSellerReviews(sellerId, size))
		else _effect.send(SellerProfileEffect.Error(FailureStatus.API_FAIL, null))
	}
	
	private suspend fun viewSellerPolicies(returnPolicy : String?, shippingPolicy : String?) {
		if (!returnPolicy.isNullOrBlank() && !shippingPolicy.isNullOrBlank())
			_effect.send(SellerProfileEffect.ViewSellerPolicies(returnPolicy, shippingPolicy))
		else _effect.send(SellerProfileEffect.Error(FailureStatus.API_FAIL, null))
		
	}
	
	private suspend fun viewSellerCollections(sellerId : Int?) {
		_effect.send(SellerProfileEffect.ViewSellerCollections(sellerId))
	}
	
	private fun readMore(params : ViewGroup.LayoutParams) {
		viewModelScope.launch {
			_state.value = SellerProfileState.Loading
			_state.value = try {
				var sellerProfileState : SellerProfileState = SellerProfileState.Idle
				webViewReadMoreUseCase(params).collect {
					sellerProfileState = when (it) {
						is Resource.Default -> SellerProfileState.Idle
						is Resource.Failure -> SellerProfileState.Error(
							it.message,
							it.failureStatus
						)
						is Resource.Loading -> SellerProfileState.Loading
						is Resource.Success -> {
							when (it.value.readType) {
								ReadMoreType.READ_MORE.ordinal -> _effect.send(
									SellerProfileEffect.ReadMore(
										it.value.params
									)
								)
								ReadMoreType.READ_LESS.ordinal -> _effect.send(
									SellerProfileEffect.ReadLess(
										it.value.params
									)
								)
							}
							SellerProfileState.Idle
						}
					}
				}
				sellerProfileState
			} catch (e : Exception) {
				SellerProfileState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
		}
	}
	
	private suspend fun navigateToChatActivity(
		sellerId : Int?,
		sellerName : String?,
		sellerProfileImage : String?
	) {
		if (sellerId != null) {
			_effect.send(
				SellerProfileEffect.NavigateToChatActivity(
					sellerId, sellerName ?: "", sellerProfileImage ?: ""
				)
			)
		} else _effect.send(SellerProfileEffect.Error(FailureStatus.API_FAIL, null))
	}
	
	private fun initOnclickListeners() {
		_state.value = SellerProfileState.SetOnclickListeners
	}
	
	private fun setActionBar() {
		_state.value = SellerProfileState.SetActionBar
	}
	
	private suspend fun openSearch() {
		_effect.send(SellerProfileEffect.OpenSearch)
	}
	
	private suspend fun closeSearch() {
		_effect.send(SellerProfileEffect.CloseSearch)
	}
	
	
	private fun checkChatFeatureEnabled() {
		viewModelScope.launch {
			_state.value = SellerProfileState.Loading
			_state.value = try {
				val intent = checkChatFeatureEnabledUseCase()
				var sellerProfileState : SellerProfileState = SellerProfileState.Idle
				
				intent.collect {
					sellerProfileState = when (it) {
						is Resource.Default -> SellerProfileState.Idle
						is Resource.Failure -> SellerProfileState.HideChatButton
						is Resource.Loading -> SellerProfileState.Loading
						is Resource.Success -> if (it.value) SellerProfileState.ShowChatButton else SellerProfileState.HideChatButton
					}
				}
				
				sellerProfileState
			} catch (e : Exception) {
				SellerProfileState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
			
		}
	}
}