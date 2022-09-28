package com.webkul.mobikul.odoo.ui.seller

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.databinding.ItemSellerProfileProductBinding
import com.webkul.mobikul.odoo.helper.ImageHelper


class SellerProfileProductsAdapter(
        val products: List<ProductEntity>,
        val listener: SellerProfileProductsListener
) : RecyclerView.Adapter<SellerProfileProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seller_profile_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ViewHolder(holder.itemView).onBind(products[position], holder.itemView.context)
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemSellerProfileProductBinding = DataBindingUtil.bind(itemView)!!


        fun onBind(data : ProductEntity, context : Context, ) {
            binding.apply {


                ImageHelper.load(
                        binding.ivProduct,
                        data.thumbnail ?: "",
                        ContextCompat.getDrawable(context, R.drawable.ic_vector_odoo_placeholder_grey400_48dp),
                        DiskCacheStrategy.NONE,
                        true,
                        ImageHelper.ImageType.PRODUCT_GENERIC
                )


                tvPreorder.apply {
                    text = if (data.isPreOrder()) context.getString(R.string.pre_order) else context.getString(R.string.out_of_stock)
                    background = if (data.isPreOrder()) ContextCompat.getDrawable(context,R.drawable.bg_pre_order) else ContextCompat.getDrawable(context,R.drawable.bg_out_of_stock)
                    visibility = if (data.isNever() || data.isCustom() || data.isService()) View.GONE else if (data.isPreOrder()) View.VISIBLE else if (data.isInStock()) View.GONE else View.VISIBLE
                }

                productName.text = data.name

                tvPriceReduce.apply {
                    text = data.priceReduce
                    visibility = if (data.priceReduce.isNullOrBlank()) View.GONE else View.VISIBLE
                }

                tvPriceUnit.apply {
                    text = data.priceUnit
                    setTypeface(this.typeface, if (data.priceReduce.isNullOrBlank()) Typeface.BOLD else Typeface.NORMAL)
                    background = if (data.priceReduce.isNullOrBlank()) null else ContextCompat.getDrawable(context,R.drawable.bg_strikethrough)
                }



                llProduct.setOnClickListener {
                    listener.viewProduct(data)
                }

            }
        }


    }


    interface SellerProfileProductsListener {
        fun viewProduct(data: ProductEntity)
    }


}
