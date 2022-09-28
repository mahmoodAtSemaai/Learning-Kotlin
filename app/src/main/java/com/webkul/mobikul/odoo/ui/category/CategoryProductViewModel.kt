package com.webkul.mobikul.odoo.ui.category

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.domain.usecase.productCategories.CategoryProductsRefreshAllowedUseCase
import com.webkul.mobikul.odoo.domain.usecase.productCategories.CategoryProductsUseCase
import com.webkul.mobikul.odoo.fragment.CategoryProductFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductViewModel @Inject constructor(
	private val categoryProductsUseCase : CategoryProductsUseCase,
	private val categoryProductsRefreshAllowedUseCase : CategoryProductsRefreshAllowedUseCase
) : BaseViewModel(), IModel<CategoryProductState, CategoryProductIntent, CategoryProductEffects> {
	
	override val intents : Channel<CategoryProductIntent> = Channel(Channel.UNLIMITED)
	
	private val _state = MutableStateFlow<CategoryProductState>(CategoryProductState.Idle)
	override val state : StateFlow<CategoryProductState>
		get() = _state
	
	private val _effect = Channel<CategoryProductEffects>()
	override val effect : Flow<CategoryProductEffects>
		get() = _effect.receiveAsFlow()
	
	lateinit var productListEntity : ProductListEntity
	var products : ArrayList<ProductEntity> = ArrayList()
	
	var offset = 0
	var initialSize = 0
	var finalSize = 0
	var pos : Int = 0
	var limit = 10
	var categoryId = ""
	var mIsFirstCall = true
	var dataRequested = false
	
	init {
		handlerIntent()
	}
	
	override fun handlerIntent() {
		viewModelScope.launch {
			intents.consumeAsFlow().collect {
				when (it) {
					is CategoryProductIntent.CheckArguments -> checkArguments(it.arguments)
					is CategoryProductIntent.GetProducts -> getProducts(
						it.categoryId,
						it.offset,
						it.limit
					)
					is CategoryProductIntent.OpenProduct -> openProduct(it.data)
					is CategoryProductIntent.RefreshProductList -> refreshProductList(
						it.lastItemPosition,
						it.rvItemCount,
						it.tCount,
						it.dataRequested
					)
				}
			}
		}
	}
	
	private suspend fun refreshProductList(
		lastItemPosition : Int,
		rvItemCount : Int,
		tCount : Int,
		dataRequested : Boolean
	) {
		try {
			val intent = categoryProductsRefreshAllowedUseCase(
				lastItemPosition,
				rvItemCount,
				tCount,
				dataRequested
			)
			
			when (intent) {
				is Resource.Success -> {
					if (intent.value) {
						offset += limit
						_effect.send(CategoryProductEffects.RefreshProductsList)
					}
				}
			}
		} catch (e : Exception) {
			CategoryProductState.Error(e.localizedMessage, FailureStatus.OTHER)
		}
	}
	
	private suspend fun openProduct(data : ProductEntity) {
		_effect.send(CategoryProductEffects.OpenProduct(data))
	}
	
	private fun getProducts(categoryId : String, offset : Int, limit : Int) {
		viewModelScope.launch {
			_state.value = CategoryProductState.Loading
			_state.value = try {
				val intent = categoryProductsUseCase(categoryId, offset, limit)
				var categoryProductState : CategoryProductState = CategoryProductState.Idle
				
				intent.collect {
					categoryProductState = when (it) {
						is Resource.Default -> CategoryProductState.Idle
						is Resource.Failure -> CategoryProductState.Error(
							it.message,
							it.failureStatus
						)
						is Resource.Loading -> CategoryProductState.Loading
						is Resource.Success -> {
							productListEntity = it.value
							initialSize = products.size
							products.addAll(it.value.products)
							finalSize = products.size
							CategoryProductState.ProductListSuccess(it.value)
						}
					}
				}
				
				categoryProductState
			} catch (e : Exception) {
				CategoryProductState.Error(e.localizedMessage, FailureStatus.OTHER)
			}
			
		}
	}
	
	private fun checkArguments(arguments : Bundle?) {
		pos = arguments?.getInt(CategoryProductFragment.POSITION_ARG)!!
		categoryId = arguments.getString(CategoryProductFragment.CATEGORY_ARG)!!
		mIsFirstCall = true
		_state.value = CategoryProductState.CheckedArguments
	}
}