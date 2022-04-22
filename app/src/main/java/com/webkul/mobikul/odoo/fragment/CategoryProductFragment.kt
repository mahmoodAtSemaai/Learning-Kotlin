package com.webkul.mobikul.odoo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.material_search_view.*


class CategoryProductFragment : Fragment() {


    private var mIsFirstCall = true
    lateinit var catalogResponse: CatalogProductResponse
    lateinit var binding : FragmentCategoryProductBinding
    private var mOffset = 0
    lateinit var id : String
    var pos : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_product,container,false)
        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pos = arguments?.getInt(POSITION_ARG)!!
        id = arguments?.getString(CATEGORY_ARG)!!

        mIsFirstCall = true
        mOffset = 0

        callApi()
    }

    fun callApi() {
        ApiConnection.getCategoryProducts(requireContext(), id, mOffset, AppSharedPref.getItemsPerPage(requireContext()))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CatalogProductResponse?>(requireContext()) {

                override fun onNext(catalogProductResponse: CatalogProductResponse) {
                    super.onNext(catalogProductResponse)
                    Log.e("TAGTAGTAG", "onNext: $pos")
                    binding.progressBar.visibility = View.VISIBLE
                    if (catalogProductResponse.isAccessDenied) {
                        redirectToSignUp()
                    } else {
                        if (mIsFirstCall) {
                            mIsFirstCall = false
                            binding.data = catalogProductResponse
                            catalogProductResponse.setWishlistData()
                            catalogResponse = catalogProductResponse
                            val requestTypeIdentifier = CatalogProductRequestType.FEATURED_CATEGORY.toString()
                            SaveData(activity, catalogProductResponse, requestTypeIdentifier)
                            if (catalogResponse.products!!.isEmpty()) {
                                showEmptyFragment()
                            } else {
                                initProductCatalogRv(id)
                            }
                        } else {
                            /*update offset from new response*/
                            catalogProductResponse.setWishlistData()
                            catalogResponse.offset = catalogProductResponse.offset + 10
                            catalogResponse.limit = catalogProductResponse.limit
                            catalogResponse.products.addAll(catalogProductResponse.products)
                            binding.productRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }

                override fun onError(t: Throwable) {
                }
            })


    }

    private fun showEmptyFragment() {
        val bundle = Bundle()
        bundle.putInt(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID, R.drawable.ic_vector_empty_product_catalog)
        bundle.putString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID, getString(R.string.empty_product_catalog))
        bundle.putString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID, getString(R.string.try_different_category_or_search_keyword_maybe))
        bundle.putBoolean(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN, false)
        bundle.putInt(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TYPE, EmptyFragment.EmptyFragType.TYPE_CATALOG_PRODUCT.ordinal)
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


    private fun initProductCatalogRv(id: String?) {
        Toast.makeText(requireContext(),"AA gaya",Toast.LENGTH_SHORT).show()

        binding.productRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        binding.productRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                if (!binding.data?.isLazyLoading!! &&
                    lastCompletelyVisibleItemPosition == binding.productRecyclerView.adapter?.itemCount!! - 1 &&
                    binding.productRecyclerView.adapter?.itemCount!! < binding.data?.totalCount!!
                ) {
                    mOffset += AppSharedPref.getItemsPerPage(context)
                    callApi()
                }
            }
        })
        binding.productRecyclerView.adapter = CategoryProductListAdapter(requireContext(), catalogResponse.products)

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