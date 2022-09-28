package com.webkul.mobikul.odoo.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.databinding.FragmentCategoryPoductV1Binding
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryProductFragmentV1 @Inject constructor() :
	BindingBaseFragment<FragmentCategoryPoductV1Binding>(),
	IView<CategoryProductIntent, CategoryProductState, CategoryProductEffects>,
	CategoryProductsAdapter.CategoryProductsInterface {
	
	
	override val layoutId : Int = R.layout.fragment_category_poduct_v1
	private val viewModel : CategoryProductViewModel by viewModels()
	
	
	companion object {
		fun newInstance(position : Int, categoryId : String) =
			CategoryProductFragmentV1().also { homeFragment ->
				val bundle = Bundle()
				bundle.putInt(BundleConstant.POSITION_ARG, position)
				bundle.putString(BundleConstant.CATEGORY_ARG, categoryId)
				homeFragment.arguments = bundle
			}
	}
	
	override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		setObservers()
		startInit()
	}
	
	
	override fun triggerIntent(intent : CategoryProductIntent) {
		lifecycleScope.launch {
			viewModel.intents.send(intent)
		}
	}
	
	private fun setObservers() {
		lifecycleScope.launch {
			viewModel.state.collect {
				render(it)
			}
		}
		
		lifecycleScope.launch {
			viewModel.effect.collect {
				render(it)
			}
		}
	}
	
	private fun startInit() {
		triggerIntent(CategoryProductIntent.CheckArguments(arguments))
	}
	
	override fun render(state : CategoryProductState) {
		when (state) {
			is CategoryProductState.Error -> {
				hideShimmer()
				showErrorState(state.failureStatus, state.message)
			}
			
			is CategoryProductState.Idle -> {}
			is CategoryProductState.Loading -> showShimmer()
			is CategoryProductState.CheckedArguments -> getProductsFromApi()
			is CategoryProductState.ProductListSuccess -> setProductList(state.productListEntity)
		}
	}
	
	
	override fun render(effect : CategoryProductEffects) {
		when (effect) {
			is CategoryProductEffects.OpenProduct -> openProduct(effect.data)
			is CategoryProductEffects.RefreshProductsList -> getProductsFromApi()
		}
	}
	
	private fun openProduct(data : ProductEntity) {
		AnalyticsImpl.trackProductItemSelected(
			Helper.getScreenName(context),
			data.productId ?: "",
			data.name ?: ""
		)
		
		Intent(context, ProductActivityV2::class.java).apply {
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, data.productId)
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID, data.templateId)
			putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, data.name)
			startActivity(this)
		}
	}
	
	
	private fun getProductsFromApi() = triggerIntent(
		CategoryProductIntent.GetProducts(
			viewModel.categoryId,
			viewModel.offset,
			viewModel.limit
		)
	)
	
	private fun setProductList(productListEntity : ProductListEntity) {
		hideShimmer()
		if (viewModel.mIsFirstCall) isFirstCall()
		else updateCatalogProductResponse()
	}
	
	private fun hideShimmer() {
		binding.shimmerProgressBar.stopShimmer()
		binding.shimmerProgressBar.visibility = View.GONE
	}
	
	private fun showShimmer() {
		if(viewModel.mIsFirstCall) {
			binding.shimmerProgressBar.visibility = View.VISIBLE
			binding.shimmerProgressBar.startShimmer()
		}
	}
	
	private fun isFirstCall() {
		if (isAdded) {
			viewModel.mIsFirstCall = false
			
			/*  Todo(""we are not locally saving data currently ")
			val requestTypeIdentifier = CatalogHelper.CatalogProductRequestType.FEATURED_CATEGORY.toString()
			SaveData(activity, catalogProductResponse, requestTypeIdentifier)*/
			
			if (viewModel.products.isNullOrEmpty()) showNoProductsFoundLayout()
			else initProductCatalogRv()
			
			viewModel.dataRequested = false
		}
	}
	
	private fun updateCatalogProductResponse() {
		binding.productRecyclerView.adapter?.notifyItemRangeChanged(
			viewModel.initialSize,
			viewModel.finalSize - 1
		)
		viewModel.dataRequested = false
	}
	
	private fun showNoProductsFoundLayout() {
		binding.productRecyclerView.visibility = View.GONE
		binding.noProductsFountLl.visibility = View.VISIBLE
	}
	
	private fun initProductCatalogRv() {
		
		binding.productRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			
			override fun onScrollStateChanged(recyclerView : RecyclerView, newState : Int) {
				super.onScrollStateChanged(recyclerView, newState)
				
				val lastItemPosition =
					(recyclerView.layoutManager as GridLayoutManager?)!!.findLastVisibleItemPosition()
				
				val rvItemCount = binding.productRecyclerView.adapter?.itemCount!!
				val tCount = viewModel.productListEntity.tCount
				
				triggerIntent(
					CategoryProductIntent.RefreshProductList(
						lastItemPosition,
						rvItemCount,
						tCount,
						viewModel.dataRequested
					)
				)
				
			}
		})
		
		binding.productRecyclerView.adapter =
			CategoryProductsAdapter(requireContext(), viewModel.products, this)
		
		binding.noProductsFountLl.visibility = View.GONE
		binding.productRecyclerView.visibility = View.VISIBLE
	}
	
	override fun onProductClick(data : ProductEntity) {
		triggerIntent(CategoryProductIntent.OpenProduct(data))
	}
	
}