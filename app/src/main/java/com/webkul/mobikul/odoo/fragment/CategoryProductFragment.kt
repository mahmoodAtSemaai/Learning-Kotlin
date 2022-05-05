package com.webkul.mobikul.odoo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.adapter.home.CategoryProductListAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.database.SaveData
import com.webkul.mobikul.odoo.databinding.FragmentCategoryProductBinding
import com.webkul.mobikul.odoo.helper.*
import com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CategoryProductFragment : Fragment() {


    private var mIsFirstCall = true
    lateinit var catalogResponse: CatalogProductResponse
    lateinit var binding: FragmentCategoryProductBinding
    private var mOffset = 0
    lateinit var id: String
    var pos: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_product, container, false)
        getArgs()
        return binding.root
    }

    private fun getArgs() {
        pos = arguments?.getInt(POSITION_ARG)!!
        id = arguments?.getString(CATEGORY_ARG)!!
        mIsFirstCall = true
        mOffset = 0
        callApi()
    }

    fun callApi() {
        binding.pbCenter.visibility = View.VISIBLE
        ApiConnection.getCategoryProducts(requireContext(), id, mOffset, 10)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CatalogProductResponse?>(requireContext()) {

                override fun onNext(catalogProductResponse: CatalogProductResponse) {
                    super.onNext(catalogProductResponse)
                    if (catalogProductResponse.isAccessDenied) {
                        redirectToSignUp()
                    } else {
                        binding.pbCenter.visibility = View.GONE
                        if (mIsFirstCall) {

                            isFirstCall(catalogProductResponse)

                        } else {
                            updateCatalogProductResponse(catalogProductResponse)
                        }
                    }
                }
                override fun onError(t: Throwable) {
                }
            })


    }

    private fun updateCatalogProductResponse(catalogProductResponse: CatalogProductResponse) {
        catalogProductResponse.setWishlistData()
        catalogResponse.offset = catalogProductResponse.offset + 10
        catalogResponse.limit = catalogProductResponse.limit
        val initialSize = catalogResponse.products.size
        catalogResponse.products.addAll(catalogProductResponse.products)
        val finalSize = catalogResponse.products.size
        binding.productRecyclerView.adapter?.notifyItemRangeChanged(initialSize, finalSize - 1)
    }

    private fun isFirstCall(catalogProductResponse : CatalogProductResponse) {
        mIsFirstCall = false
        binding?.data = catalogProductResponse
        catalogProductResponse.setWishlistData()
        catalogResponse = catalogProductResponse
        val requestTypeIdentifier = CatalogProductRequestType.FEATURED_CATEGORY.toString()
        SaveData(activity, catalogProductResponse, requestTypeIdentifier)
        if (catalogResponse.products!!.isEmpty()) {
            showEmptyFragment()
        } else {
            initProductCatalogRv()
        }
    }

    private fun showEmptyFragment() {
        val bundle = Bundle()
        bundle.apply {
            putInt(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID, R.drawable.ic_vector_empty_product_catalog)
            putString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID, getString(R.string.empty_product_catalog))
            putString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID, getString(R.string.try_different_category_or_search_keyword_maybe))
            putBoolean(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN, false)
            putInt(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TYPE, EmptyFragment.EmptyFragType.TYPE_CATALOG_PRODUCT.ordinal)
        }
        findNavController(requireView()).navigate(R.id.action_homeFragment_to_emptyFragment, bundle)
    }

    private fun redirectToSignUp() {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(requireContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message)) { sweetAlertDialog ->
            sweetAlertDialog.dismiss()
            AppSharedPref.clearCustomerData(requireContext())
            val i = Intent(requireContext(), SignInSignUpActivity::class.java)
            i.putExtra(BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY, CatalogProductActivity::class.java.simpleName)
            startActivity(i)
        }
    }


    private fun initProductCatalogRv() {
        binding.productRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager?)!!.findLastVisibleItemPosition()
                if (!binding.data?.isLazyLoading!! &&
                    lastCompletelyVisibleItemPosition == binding.productRecyclerView.adapter?.itemCount!! - 1 &&
                    binding.productRecyclerView.adapter?.itemCount!! < binding.data?.totalCount!!
                ) {
                    mOffset += 10
                    callApi()
                }
            }
        })
        binding.productRecyclerView.adapter =
            CategoryProductListAdapter(requireContext(), catalogResponse.products)

    }

    companion object {
        var POSITION_ARG = BundleConstant.POSITION_ARG
        var CATEGORY_ARG = BundleConstant.CATEGORY_ARG

        @JvmStatic
        fun newInstance(position: Int, category_id: String) = CategoryProductFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
                putString(CATEGORY_ARG, category_id)
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        view?.requestLayout()
        super.setUserVisibleHint(isVisibleToUser)

    }

}