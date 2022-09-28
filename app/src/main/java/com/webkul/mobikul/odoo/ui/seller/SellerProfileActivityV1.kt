package com.webkul.mobikul.odoo.ui.seller

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.ChatActivity
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.entity.SellerDetailsEntity
import com.webkul.mobikul.odoo.databinding.ActivitySellerProfileV1Binding
import com.webkul.mobikul.odoo.helper.CatalogHelper
import com.webkul.mobikul.odoo.helper.CustomerHelper
import com.webkul.mobikul.odoo.helper.ImageHelper
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SellerProfileActivityV1 @Inject constructor() :
	BindingBaseActivity<ActivitySellerProfileV1Binding>(),
	IView<SellerProfileIntent, SellerProfileState, SellerProfileEffect>,
	SellerProfileProductsAdapter.SellerProfileProductsListener {
	
	override val contentViewId : Int = R.layout.activity_seller_profile_v1
	private val viewModel : SellerProfileViewModel by viewModels()
	
	private lateinit var wishlistMenuItem : MenuItem
	
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		startInit()
		setObservers()
	}
	
	
	override fun onCreateOptionsMenu(menu : Menu) : Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		wishlistMenuItem = menu.findItem(R.id.menu_item_wishlist)
		isWishlistAllowed()
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item : MenuItem) : Boolean {
		when (item.itemId) {
			R.id.menu_item_search -> triggerIntent(SellerProfileIntent.OpenSearch)
			R.id.menu_item_wishlist -> triggerIntent(SellerProfileIntent.NavigateToWishList)
			R.id.menu_item_chat -> openChatScreen()
		}
		return super.onOptionsItemSelected(item)
	}
	
	override fun onBackPressed() {
		if (binding.searchView.isOpen) {
			triggerIntent(SellerProfileIntent.CloseSearch)
			return
		}
		super.onBackPressed()
	}
	
	override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		checkActivityResult(requestCode, resultCode)
	}
	
	private fun checkActivityResult(
		requestCode : Int,
		resultCode : Int
	) = triggerIntent(SellerProfileIntent.CheckActivityResult(requestCode, resultCode))
	
	private fun setObservers() {
		lifecycleScope.launchWhenCreated {
			viewModel.state.collect {
				render(it)
			}
		}
		lifecycleScope.launchWhenCreated {
			viewModel.effect.collect {
				render(it)
			}
		}
	}
	
	override fun render(state : SellerProfileState) {
		when (state) {
			is SellerProfileState.Idle -> {}
			is SellerProfileState.Loading -> showProgressDialog()
			is SellerProfileState.SetActionBar -> setActionBar()
			is SellerProfileState.HideChatButton -> hideChatButton()
			is SellerProfileState.ShowChatButton -> showChatButton()
			is SellerProfileState.SetOnclickListeners -> setOnclickListeners()
			is SellerProfileState.Error -> {
				dismissProgressDialog()
				showErrorState(state.failureStatus, state.message)
			}
		}
	}
	
	
	override fun render(effect : SellerProfileEffect) {
		when (effect) {
			is SellerProfileEffect.CloseSearch -> binding.searchView.closeSearch()
			is SellerProfileEffect.OpenSearch -> binding.searchView.openSearch()
			is SellerProfileEffect.NavigateToChatActivity -> navigateToChatActivity(
				effect.sellerId,
				effect.sellerName,
				effect.sellerProfileImage
			)
			is SellerProfileEffect.ReadLess -> readLessAbout(effect.params)
			is SellerProfileEffect.ReadMore -> readMoreAbout(effect.params)
			is SellerProfileEffect.ViewSellerCollections -> viewSellerCollections(effect.sellerId!!)
			is SellerProfileEffect.ViewSellerPolicies -> {} //viewSellerPolicies(effect.returnPolicy, effect.shippingPolicy)
			is SellerProfileEffect.ViewSellerReviews -> {}//viewSellerReviews(effect.sellerId, effect.size)
			is SellerProfileEffect.FetchedSellerDetails -> fetchedSellerDetails(effect.data)
			is SellerProfileEffect.FetchedSellerProducts -> fetchedSellerProducts(effect.data)
			is SellerProfileEffect.NavigateToWishList -> navigateToWishList()
			is SellerProfileEffect.HideWishlistButton -> hideWishlistButton()
			is SellerProfileEffect.ShowWishlistButton -> showWishlistButton()
			is SellerProfileEffect.RecreateActivity -> recreate()
			is SellerProfileEffect.Error -> {
				dismissProgressDialog()
				showErrorState(effect.failureStatus, effect.message)
			}
		}
	}
	
	private fun showWishlistButton() {
		wishlistMenuItem.isVisible = true
	}
	
	private fun hideWishlistButton() {
		wishlistMenuItem.isVisible = false
	}
	
	private fun navigateToWishList() {
		Intent(this, CustomerBaseActivity::class.java).apply {
			putExtra(
				BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE,
				CustomerHelper.CustomerFragType.TYPE_WISHLIST
			)
			startActivity(this)
		}
	}
	
	private fun viewSellerCollections(sellerId : Int) {
		Intent(this, CatalogProductActivity::class.java).apply {
			putExtra(
				BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE,
				CatalogHelper.CatalogProductRequestType.SELLER_PRODUCTS
			)
			putExtra(BUNDLE_KEY_SELLER_ID, sellerId.toString())
			startActivity(this)
		}
	}
	
	private fun readLessAbout(params : ViewGroup.LayoutParams) {
		binding.wvStoreDescription.layoutParams = params
		binding.tvReadMore.setText(R.string.read_more)
	}
	
	private fun readMoreAbout(params : ViewGroup.LayoutParams) {
		binding.wvStoreDescription.layoutParams = params
		binding.tvReadMore.setText(R.string.read_less)
	}
	
	private fun navigateToChatActivity(
		sellerId : Int,
		sellerName : String,
		sellerProfileImage : String
	) {
		Intent(this, ChatActivity::class.java).apply {
			putExtra(BundleConstant.BUNDLE_KEY_SELLER_ID, sellerId.toString())
			putExtra(BundleConstant.BUNDLE_KEY_CHAT_TITLE, sellerName)
			putExtra(BundleConstant.BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL, sellerProfileImage)
			startActivity(this)
		}
	}
	
	private fun startInit() {
		triggerIntent(SellerProfileIntent.SetActionBar)
	}
	
	override fun triggerIntent(intent : SellerProfileIntent) {
		lifecycleScope.launch {
			viewModel.intents.send(intent)
		}
	}
	
	private fun setActionBar() {
		setSupportActionBar(binding.tbMain)
		checkChatFeatureIsEnabled()
	}
	
	private fun showChatButton() {
		binding.btnSellerChat.makeVisible()
		initOnclickListeners()
	}
	
	private fun hideChatButton() {
		binding.btnSellerChat.makeGone()
		initOnclickListeners()
	}
	
	private fun setOnclickListeners() {
		binding.apply {
			
			tbMain.setNavigationOnClickListener {
				super.onBackPressed()
			}
			
			btnSellerChat.setOnClickListener {
				openChatScreen()
			}
			
			tvReadMore.setOnClickListener {
				triggerIntent(SellerProfileIntent.ReadMore(binding.wvStoreDescription.layoutParams))
			}
			
			tvViewAll.setOnClickListener {
				triggerIntent(SellerProfileIntent.ViewSellerCollections(viewModel.sellerDetailsEntity?.id))
			}
			
			llSellerPolicies.setOnClickListener {
				val sellerInfo = viewModel.sellerDetailsEntity
				triggerIntent(
					SellerProfileIntent.ViewSellerPolicies(
						sellerInfo?.returnPolicy,
						sellerInfo?.shippingPolicy
					)
				)
				
			}
			
			llSellerReviews.setOnClickListener {
				val sellerInfo = viewModel.sellerDetailsEntity
				triggerIntent(
					SellerProfileIntent.ViewSellerReviews(
						sellerInfo?.id,
						sellerInfo?.seller_reviews?.size
					)
				)
				
			}
			
		}
		
		getSellerDetails()
		getSellerProducts()
	}
	
	
	private fun openChatScreen() {
		val sellerInfo = viewModel.sellerDetailsEntity
		triggerIntent(
			SellerProfileIntent.NavigateToChatActivity(
				sellerInfo?.id, sellerInfo?.name, sellerInfo?.profileImage
			)
		)
	}
	
	
	private fun fetchedSellerDetails(data : SellerDetailsEntity) {
		binding.apply {
			
			tbMain.title = data.name
			
			nsvSellerProfile.visibility = if (data != null) View.VISIBLE else View.GONE
			
			profileBanner.apply {
				ImageHelper.load(
					this,
					data.profileBanner ?: "",
					null,
					DiskCacheStrategy.NONE,
					true,
					null
				)
			}
			
			ivSellerProfile.apply {
				ImageHelper.load(
					this,
					data.profileImage ?: "",
					ContextCompat.getDrawable(
						this@SellerProfileActivityV1,
						R.drawable.ic_men_avatar
					),
					DiskCacheStrategy.NONE,
					true,
					ImageHelper.ImageType.PROFILE_PIC_GENERIC
				)
			}
			
			tvSellerName.apply {
				text = data.name
				setTextColor(
					ResourcesCompat.getColor(
						resources,
						if (data.profileImage.isNullOrBlank()) R.color.text_color_primary else R.color.white,
						null
					)
				)
			}
			
			tvSellerCountry.apply {
				text = data.country
				setTextColor(
					ResourcesCompat.getColor(
						resources,
						if (data.profileImage.isNullOrBlank()) R.color.text_color_primary else R.color.white,
						null
					)
				)
				
			}
			
			tvAboutStore.visibility = if (data.profileMsg.isBlank()) View.GONE else View.VISIBLE
			
			cvAboutText.visibility = if (data.profileMsg.isBlank()) View.GONE else View.VISIBLE
			
			wvStoreDescription.loadUrl(if (data.profileMsg.isBlank()) getString(R.string.empty_about_store) else data.profileMsg)
			
			tvReadMore.visibility = if (data.profileMsg.isBlank()) View.GONE else View.VISIBLE
			
		}
	}
	
	private fun fetchedSellerProducts(data : ProductListEntity) {
		dismissProgressDialog()
		binding.apply {
			tvViewAll.visibility = if (data.products.size > 0) View.VISIBLE else View.GONE
			tvNoProducts.visibility = if (data.products.size > 0) View.GONE else View.VISIBLE
			
			rvSellerRecentCollection.adapter =
				SellerProfileProductsAdapter(data.products, this@SellerProfileActivityV1)
		}
	}
	
	private fun checkChatFeatureIsEnabled() =
		triggerIntent(SellerProfileIntent.IsChatFeatureEnabled)
	
	private fun initOnclickListeners() = triggerIntent(SellerProfileIntent.InitOnclickListeners)
	
	private fun getSellerProducts() = triggerIntent(
		SellerProfileIntent.GetSellerProducts(
			intent.extras?.getInt(BUNDLE_KEY_SELLER_ID)
		)
	)
	
	private fun getSellerDetails() = triggerIntent(
		SellerProfileIntent.GetSellerProfileData(
			intent.extras?.getInt(BUNDLE_KEY_SELLER_ID)
		)
	)
	
	private fun isWishlistAllowed() = triggerIntent(SellerProfileIntent.IsWishlistAllowed)
	
	
	override fun viewProduct(data : ProductEntity) {
		Intent(this, ProductActivityV2::class.java).apply {
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, data.productId)
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID, data.templateId)
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, data.name)
			startActivity(this)
		}
		
	}
}
