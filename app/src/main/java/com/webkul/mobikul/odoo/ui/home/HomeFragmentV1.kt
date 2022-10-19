package com.webkul.mobikul.odoo.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.appbar.AppBarLayout
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.LoyaltyTermsActivity
import com.webkul.mobikul.odoo.activity.UpdateAddressActivity
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.databinding.FragmentHomeV1Binding
import com.webkul.mobikul.odoo.domain.enums.HomeRefreshState
import com.webkul.mobikul.odoo.helper.CartUpdateListener
import com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.helper.LoyaltyPointsListener
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import com.webkul.mobikul.odoo.ui.learning_anko.LearningActivity
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragmentV1 @Inject constructor() :
    BindingBaseFragment<FragmentHomeV1Binding>(), IView<HomeIntent, HomeState, HomeEffect>,
    HomeFeaturedCategoriesAdapter.HomeFeaturedCategoryListener,
    HomeBannerAdapterV1.HomeBannerAdapterListener {


    override val layoutId: Int = R.layout.fragment_home_v1
    private val viewModel: HomeViewModel by viewModels()
    private var cartUpdateListener: CartUpdateListener? = null
    lateinit var homeFeaturedCategoriesAdapter: HomeFeaturedCategoriesAdapter
    private var loyaltyPointsListener: LoyaltyPointsListener? = null

    companion object {
        fun newInstance() = HomeFragmentV1()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        startInit()
        binding.fabLearning.setOnClickListener {
            startActivity(Intent(activity, LearningActivity::class.java))
        }
    }

    private fun startInit() {
        triggerIntent(HomeIntent.GetBillingAddress)
        triggerIntent(HomeIntent.SetOnclickListeners)
        triggerIntent(HomeIntent.InitView)
    }

    private fun initView(){
        setHasOptionsMenu(true)
        Helper.hideKeyboard(context)

        getBannerDataFromApi()
        updateUserDetailsFromApi()
        getProductCategoriesFromApi()
    }

    override fun triggerIntent(intent: HomeIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

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

    override fun render(state: HomeState) {
        when (state) {
            is HomeState.Idle -> {
                hideBannerShimmer()
                hideCategoriesShimmer()
            }
            is HomeState.BillingAddressDataSuccess -> checkIfAddressExists(state.addressEntity)
            is HomeState.AddressFormDataSuccess -> getStates(
                state.addressFormEntity,
                state.addressData
            )
            is HomeState.StateListSuccess -> checkForStateAvailability(
                state.stateListEntity,
                state.addressFormEntity,
                state.addressData
            )
            is HomeState.RefreshState -> setRefreshState(state.isRefreshEnable)
            is HomeState.Error -> {
                binding.refreshLayout.isRefreshing = false
                showErrorState(state.failureStatus, state.message)
            }
            is HomeState.Loading -> {
                showBannerShimmer()
                showCategoriesShimmer()
            }
        }
    }

    override fun render(effect: HomeEffect) {
        when (effect) {
            is HomeEffect.ShowPromptToCompleteBillingAddress -> showPromptToCompleteBillingAddress(
                effect.addressData
            )
            is HomeEffect.BannerDataSuccess -> setBanner(effect.value)
            is HomeEffect.ProductCategoriesSuccess -> setCategories(effect.categories)
            is HomeEffect.HideBannerShimmer -> hideBannerShimmer()
            is HomeEffect.HideCategoriesShimmer -> hideCategoriesShimmer()
            is HomeEffect.ShowBannerShimmer -> showBannerShimmer()
            is HomeEffect.ShowCategoriesShimmer -> showCategoriesShimmer()
            is HomeEffect.SelectCategory -> selectCategory(effect.position, effect.previousPosition)
            is HomeEffect.SetOnclickListeners -> setOnclickListeners()
            is HomeEffect.OnBannerClick -> openBanner(effect.bannerEntity)
            is HomeEffect.BackPressed -> backPressed()
            is HomeEffect.Error -> {
                binding.refreshLayout.isRefreshing = false
                showErrorState(effect.failureStatus, effect.msg)
            }
            is HomeEffect.GetLoyaltyPoints -> getLoyaltyPoints()
            is HomeEffect.UpdateCartCount -> updateCartCount()
            is HomeEffect.InitView -> initView()
        }
    }

    private fun getLoyaltyPoints() {
        loyaltyPointsListener?.onRefreshLoyaltyPoints()
    }

    private fun backPressed() {
        activity!!.finishAffinity()
    }

    private fun openBanner(bannerEntity: BannerEntity) {
        AnalyticsImpl.trackPromotionalBannerSelected(
            bannerEntity.bannerId.toString(),
            bannerEntity.bannerName,
            requireContext().getString(R.string.home),
            0
        )
        var intent: Intent? = null

        when (bannerEntity.bannerType) {
            ApplicationConstant.TYPE_CUSTOM -> {
                intent = Intent(requireActivity(), CatalogProductActivity::class.java).apply {
                    putExtra(BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN, bannerEntity.domain)
                    putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_NAME, bannerEntity.bannerName)
                    putExtra(
                        BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE,
                        CatalogProductRequestType.SEARCH_DOMAIN
                    )
                }
            }
            ApplicationConstant.TYPE_PRODUCT -> {
                intent = Intent(requireActivity(), ProductActivityV2::class.java).apply {
                    putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, bannerEntity.productId)
                    putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, bannerEntity.bannerName)
                    try {
                        putExtra(
                            BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID,
                            bannerEntity.bannerId.toInt()

                        )
                    } catch (e: NumberFormatException) {
                    }
                }
            }
            ApplicationConstant.TYPE_CATEGORY -> {
                intent = Intent(requireActivity(), CatalogProductActivity::class.java).apply {
                    putExtra(
                        BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE,
                        CatalogProductRequestType.BANNER_CATEGORY
                    )
                    putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_ID, bannerEntity.bannerId.toInt())
                    putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_NAME, bannerEntity.bannerName)
                }
            }
            ApplicationConstant.TYPE_LOYALTY_TERMS -> {
                intent = Intent(requireActivity(), LoyaltyTermsActivity::class.java).apply {
                    putExtra(
                        BundleConstant.BUNDLE_KEY_LOYALTY_BANNER_ID,
                        bannerEntity.loyaltyTermId.toInt()
                    )
                }
            }
            ApplicationConstant.TYPE_NONE -> {}
        }

        if (intent != null) requireActivity().startActivity(intent)
    }

    private fun selectCategory(position: Int, previousPosition: Int) {
        binding.apply {
            viewPager2.currentItem = position
            featuredCategoriesRv.post {
                homeFeaturedCategoriesAdapter.apply {
                    addCurrentSelectedItemMargin(position)
                    removePreviousSelectedItemMargin(previousPosition)
                }
            }
        }
    }

    private fun setCategories(categories: ProductCategoriesEntity) {
        binding.apply {
            homeFeaturedCategoriesAdapter = HomeFeaturedCategoriesAdapter(
                requireActivity(),
                categories.categories,
                this@HomeFragmentV1
            )
            featuredCategoriesRv.apply {
                adapter = homeFeaturedCategoriesAdapter
                setHasFixedSize(true)

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        triggerIntent(HomeIntent.RefreshStateChanged(
                            HomeRefreshState.SCROLL_STATE.apply { state1 = newState }
                        ))
                    }
                })
            }

            viewPager2.apply {
                isUserInputEnabled = false
                adapter = CategoryProductAdapterV1(
                    requireActivity() as AppCompatActivity,
                    categories.categories
                )
            }

            refreshLayout.isRefreshing = false
        }
    }

    private fun setBanner(bannerData: BannerListEntity) {
        for (i in bannerData.banners.indices) {
            if (bannerData.banners[i].url != "false" && !bannerData.banners[i].url.isNullOrBlank()) {
                viewModel.isImageVisible = true
                break
            }
        }

        binding.apply {
            if (!viewModel.isImageVisible) appBarLayout.visibility = View.GONE

            bannerDotsTabLayout.setupWithViewPager(bannerViewPager, true)
            bannerViewPager.adapter = HomeBannerAdapterV1(bannerData.banners, this@HomeFragmentV1)

            //TODO-> Handle with Price Comparison 1B
            val runnable = Runnable {
                val position = bannerViewPager.currentItem
                if (position < bannerData.banners.size - 1)
                    bannerViewPager.currentItem = position + 1 else bannerViewPager.currentItem = 0
            }

            //TODO-> Handle with Price Comparison 1B
            bannerViewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (position == 0) viewModel.handler.postDelayed(runnable, 5000)
                }

                //TODO-> Handle with Price Comparision 1B
                override fun onPageSelected(position: Int) {
                    viewModel.handler.removeCallbacks(runnable)
                    viewModel.handler.postDelayed(runnable, 5000)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    triggerIntent(HomeIntent.RefreshStateChanged(
                        HomeRefreshState.PAGE_SCROLL_STATE.apply {
                            state1 = viewModel.appBarOffset
                            state2 = state
                        }
                    ))
                }
            })
        }
    }

    private fun getBannerDataFromApi() = triggerIntent(HomeIntent.GetBannerData)

    private fun updateUserDetailsFromApi() = triggerIntent(HomeIntent.UpdateUserDetails)

    private fun getProductCategoriesFromApi() = triggerIntent(HomeIntent.GetProductCategories)

    private fun showPromptToCompleteBillingAddress(addressData: AddressData) {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.billing_address_incomplete))
            .setContentText(getString(R.string.no_address_text))
            .setConfirmText(getString(R.string.add_now))
            .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                startActivity(
                    Intent(requireActivity(), UpdateAddressActivity::class.java).putExtra(
                        BundleConstant.BUNDLE_KEY_URL,
                        addressData.url
                    )
                )
                sweetAlertDialog.dismiss()
            }
            .show()
    }

    private fun checkForStateAvailability(
        stateListEntity: StateListEntity,
        addressFormEntity: AddressFormEntity,
        addressData: AddressData
    ) = triggerIntent(
        HomeIntent.CheckStateAvailability(
            stateListEntity,
            addressFormEntity,
            addressData
        )
    )

    private fun checkIfAddressExists(addressEntity: AddressEntity) {
        if (addressEntity.addresses.isEmpty()) showMissingBillingAddressPrompt()
        else getAddressFormData(addressEntity.addresses[0])
    }

    private fun showMissingBillingAddressPrompt() {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.billing_address_missing))
            .setContentText(getString(R.string.no_address_text))
            .setConfirmText(getString(R.string.add_now))
            .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                startActivity(Intent(requireActivity(), UpdateAddressActivity::class.java))
                sweetAlertDialog.dismiss()
            }.show()
    }

    private fun getAddressFormData(addressData: AddressData) =
        triggerIntent(HomeIntent.GetAddressFormData(addressData))

    private fun getStates(addressFormEntity: AddressFormEntity, addressData: AddressData) =
        triggerIntent(HomeIntent.GetStates(1, addressFormEntity, addressData))

    private fun setOnclickListeners() {

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            viewModel.appBarOffset = verticalOffset
            triggerIntent(HomeIntent.RefreshStateChanged(
                HomeRefreshState.OFF_SET_CHANGED_STATE.apply { state1 = verticalOffset }
            ))
            binding.refreshLayout.isEnabled = viewModel.appBarOffset == 0
        })

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                triggerIntent(HomeIntent.BackPressed)
            }
        })

        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.background_orange
            )
        )

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = true
            getProductCategoriesFromApi()
            getBannerDataFromApi()
            getLoyaltyPointsFromApi()
        }
    }

    private fun getLoyaltyPointsFromApi() = triggerIntent(HomeIntent.GetLoyaltyPoints)

    private fun showBannerShimmer() {
        binding.bannerViewPager.visibility = View.INVISIBLE
        binding.shimmerBanner.visibility = View.VISIBLE
        binding.shimmerBanner.startShimmer()
    }

    private fun showCategoriesShimmer() {
        binding.featuredCategoriesRv.visibility = View.INVISIBLE
        binding.shimmerFeaturedCategories.visibility = View.VISIBLE
        binding.shimmerFeaturedCategories.startShimmer()
    }

    private fun hideCategoriesShimmer() {
        binding.shimmerFeaturedCategories.stopShimmer()
        binding.shimmerFeaturedCategories.visibility = View.GONE
        binding.featuredCategoriesRv.visibility = View.VISIBLE
    }

    private fun hideBannerShimmer() {
        binding.shimmerBanner.stopShimmer()
        binding.bannerViewPager.visibility = View.VISIBLE
        binding.shimmerBanner.visibility = View.GONE

    }


    private fun isShimmerVisible(): Boolean {
        return binding.shimmerBanner.isShimmerVisible || binding.shimmerFeaturedCategories.isShimmerVisible
    }

    override fun onCategoryClick(position: Int, previousPosition: Int) {
        triggerIntent(HomeIntent.SelectCategory(position, previousPosition))
    }

    override fun onBannerClick(bannerEntity: BannerEntity) {
        triggerIntent(HomeIntent.OnBannerClick(bannerEntity))
    }

    override fun onAttach(context: Context) {
        if (context is CartUpdateListener) {
            cartUpdateListener = context
        }
        if (context is LoyaltyPointsListener) {
            loyaltyPointsListener = context
        }
        super.onAttach(context)
    }

    private fun updateCartCount() {
        cartUpdateListener?.updateCart()
    }


    private fun setRefreshState(refreshEnable: Boolean) {
        binding.refreshLayout.isEnabled = refreshEnable
    }
}