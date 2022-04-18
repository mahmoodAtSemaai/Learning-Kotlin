package com.webkul.mobikul.odoo.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.NewHomeActivity
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
import com.webkul.mobikul.odoo.helper.NetworkHelper
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CategoryProductFragment : Fragment() {


    lateinit var  recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_category_product, container, false)
        recyclerView = view.findViewById(R.id.product_recycler_view)
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pos = arguments?.getInt(POSITION_ARG)
        var id = arguments?.getString(CATEGORY_ARG)

        callApi(id)


    }

    private fun callApi(id: String?) {
        val catalogProductDataObservable = ApiConnection.getCategoryProducts(
            context,
            id,
            0,
            AppSharedPref.getItemsPerPage(context)
        )


        catalogProductDataObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe( object : CustomObserver<CatalogProductResponse?>(requireContext()) {

                override fun onComplete() {}

                override fun onNext(catalogProductResponse: CatalogProductResponse) {
                    super.onNext(catalogProductResponse)

                    recyclerView.adapter = CatalogProductListHomeAdapter(
                        context,
                        catalogProductResponse.products,
                        HomeFragment.VIEW_TYPE_LIST
                    )

                    val spanCount = 2
                    val gridLayoutManager = GridLayoutManager(context, spanCount)
                    gridLayoutManager.spanSizeLookup =
                        object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return 1
                            }
                        }
                    recyclerView.layoutManager = gridLayoutManager
                }

                override fun onError(t: Throwable) {
                }
            })

    }

    companion object{
        var POSITION_ARG = "position_arg"
        var CATEGORY_ARG = "category_id"

        @JvmStatic
        fun newInstance(position: Int , category_id : String ) = CategoryProductFragment().apply {
           arguments = Bundle().apply{
               putInt(POSITION_ARG,position)
               putString(CATEGORY_ARG,category_id)
           }
        }
    }


}