package com.webkul.mobikul.odoo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.adapter.home.CatalogProductListHomeAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.database.SaveData
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType
import com.webkul.mobikul.odoo.helper.EndlessRecyclerViewScrollListener
import com.webkul.mobikul.odoo.helper.NetworkHelper
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CategoryProductFragment : Fragment() {


    lateinit var recyclerView: RecyclerView
    private var mIsFirstCall = true
    lateinit var catalog_response: CatalogProductResponse
    var currentItems = 0
    var totalItems = 0
    var scrolledItems = 0
    var isScrolling = false
    lateinit var adapter: CatalogProductListHomeAdapter
    lateinit var progressbar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_category_product, container, false)
        recyclerView = view.findViewById(R.id.product_recycler_view)
        progressbar = view.findViewById(R.id.progress_bar)
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pos = arguments?.getInt(POSITION_ARG)
        val id = arguments?.getString(CATEGORY_ARG)

        init(id)
        callApi(id)
    }


    private fun init(id: String?) {
        /*Init Data */
        mIsFirstCall = true
        AppSharedPref.setItemsPerPage(requireContext(), BuildConfig.DEFAULT_ITEM_PER_PAGE)
    }

    fun callApi(id: String?) {
        var offset = 0
        if (!mIsFirstCall) {
            offset = catalog_response.offset + AppSharedPref.getItemsPerPage(requireContext())
        }
        val catalogProductRequestType = CatalogProductRequestType.FEATURED_CATEGORY
        val catalogProductDataObservable = ApiConnection.getCategoryProducts(
            requireContext(),
            id,
            offset,
            AppSharedPref.getItemsPerPage(requireContext())
        )

        catalogProductDataObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CatalogProductResponse?>(
                requireContext()
            ) {

                override fun onComplete() {}

                override fun onNext(catalogProductResponse: CatalogProductResponse) {
                    super.onNext(catalogProductResponse)

                    progressbar.visibility = View.VISIBLE

                    if (catalogProductResponse.isAccessDenied) {
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(
                            requireContext(),
                            getString(R.string.error_login_failure),
                            getString(R.string.access_denied_message)
                        ) { sweetAlertDialog ->
                            sweetAlertDialog.dismiss()
                            AppSharedPref.clearCustomerData(requireContext())
                            val i = Intent(requireContext(), SignInSignUpActivity::class.java)
                            i.putExtra(
                                BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                                CatalogProductActivity::class.java.simpleName
                            )
                            startActivity(i)
                        }
                    } else {
                        if (mIsFirstCall) {
                            catalog_response = catalogProductResponse
                            catalogProductResponse.setWishlistData()

                            /*BETTER REPLACE SOME CONTAINER INSTEAD OF WHOLE PAGE android.R.id.content */
                            val requestTypeIdentifier =
                                CatalogProductRequestType.FEATURED_CATEGORY.toString()
                            SaveData(activity, catalogProductResponse, requestTypeIdentifier)
                            if (catalog_response?.products!!.isEmpty()) {
                                val bundle = Bundle()
                                bundle.putInt(
                                    BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID,
                                    R.drawable.ic_vector_empty_product_catalog
                                )
                                bundle.putString(
                                    BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID,
                                    getString(R.string.empty_product_catalog)
                                )
                                bundle.putString(
                                    BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID,
                                    getString(R.string.try_different_category_or_search_keyword_maybe)
                                )
                                bundle.putBoolean(
                                    BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN,
                                    false
                                )
                                bundle.putInt(
                                    BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TYPE,
                                    EmptyFragment.EmptyFragType.TYPE_CATALOG_PRODUCT.ordinal
                                )
                                findNavController(requireView()).navigate(
                                    R.id.action_homeFragment_to_emptyFragment,
                                    bundle
                                )
                            } else {
                                initProductCatalogRv(id)
                            }
                        } else {
                            /*update offset from new response*/
                            catalogProductResponse.setWishlistData()
                            catalog_response.offset = catalogProductResponse.offset
                            catalog_response.limit = catalogProductResponse.limit
                            catalog_response.products.addAll(catalogProductResponse.products)
//                                initProductCatalogRv(id)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onError(t: Throwable) {


                    if (catalog_response != null) {
//                            catalog_response.isLazyLoading(false)
                    }

                    if (!NetworkHelper.isNetworkAvailable(requireContext())) {
                        val sqlLiteDbHelper = SqlLiteDbHelper(requireContext())
                        val requestTypeIdentifier =
                            CatalogProductRequestType.FEATURED_CATEGORY.toString()
                        val dbResponse =
                            sqlLiteDbHelper.getCatalogProductData(requestTypeIdentifier)
                        if (mIsFirstCall) {
                            if (dbResponse != null) {
                                onNext(dbResponse)
                            } else {
                                super.onError(t)
                            }
                        } else {
                            super.onError(t)
                        }
                    } else {
                        super.onError(t)
                    }
                }
            })


    }

    private fun initProductCatalogRv(id: String?) {

        adapter =
            CatalogProductListHomeAdapter(
                requireContext(),
                catalog_response.products,
                HomeFragment.VIEW_TYPE_LIST
            )

        val spanCount = 2
        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == catalog_response.products.size) {
                    spanCount
                } else 1
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })


    }

    companion object {
        var POSITION_ARG = "position_arg"
        var CATEGORY_ARG = "category_id"

        @JvmStatic
        fun newInstance(position: Int, category_id: String) = CategoryProductFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
                putString(CATEGORY_ARG, category_id)
            }
        }
    }

}