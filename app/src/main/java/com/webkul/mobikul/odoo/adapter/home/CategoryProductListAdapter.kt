package com.webkul.mobikul.odoo.adapter.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ItemCategoryProductsBinding
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.helper.OdooApplication
import com.webkul.mobikul.odoo.model.generic.ProductData

class CategoryProductListAdapter(
    val context: Context,
    val productList: ArrayList<ProductData>
) :
    RecyclerView.Adapter<CategoryProductListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_category_products, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, productList.get(position))
    }

    private fun bindData(holder: ViewHolder, data: ProductData) {
        holder.binding.data = data
        holder.binding.root.setOnClickListener {
            redirectToProductDetailActivtiy(data)
        }
        holder.binding.root.setOnClickListener {
            redirectToProductDetailActivtiy(data)
        }
    }

    private fun redirectToProductDetailActivtiy(data: ProductData) {
        AnalyticsImpl.trackProductItemSelected(Helper.getScreenName(context), data.productId, data.name)
        val intent = Intent(context, (context.applicationContext as OdooApplication).productActivity)
        intent.putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, data.productId)
        intent.putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID, data.templateId)
        intent.putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, data.name)
        context.startActivity(intent)
    }


    override fun getItemCount(): Int = productList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemCategoryProductsBinding = DataBindingUtil.bind(itemView)!!
    }


}