package com.webkul.mobikul.odoo.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.SellerEntity
import com.webkul.mobikul.odoo.databinding.ItemSellerBinding
import com.webkul.mobikul.odoo.ui.cart.CartProductAdapter.ShowDialogFragmentListener


class SellerAdapter(
    val context: Context,
    private val sellerList: ArrayList<SellerEntity>,
    val listener: ShowDialogFragmentListener,
    private val clickListener: (SellerEntity) -> Unit
) :
    RecyclerView.Adapter<SellerAdapter.SellerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerHolder {
        return SellerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_seller, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SellerHolder, position: Int) {
        holder.binding.apply {
            val seller = sellerList[position]
            seller.isSellerChecked = ObservableBoolean(false)
            rvProducts.adapter = CartProductAdapter(context, listener, seller.products, position)
            data = seller
            cbSellerSelection.setOnClickListener {
                val isChecked = (it as AppCompatCheckBox).isChecked
                seller.isSellerChecked.set(isChecked)
                seller.products.forEach { cartEntity ->
                    if (isChecked && !cartEntity.isOutOfStock())
                        cartEntity.isChecked.set(true)
                    else
                        cartEntity.isChecked.set(false)
                }
                (listener as CartFragment).selectAllProductItems(position)
            }
            tvSellerName.setOnClickListener {
                clickListener(seller)
            }
            ivGoToSellerPage.setOnClickListener {
                clickListener(seller)
            }
        }
    }


    override fun getItemCount(): Int = sellerList.size

    inner class SellerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemSellerBinding = DataBindingUtil.bind(itemView)!!

        fun setChecked(boolean: Boolean) {
            binding.cbSellerSelection.isChecked = boolean
        }
    }

    interface SelectSellerItems {
        fun selectAllProductItems(sellerPosition: Int)
    }

}

@BindingAdapter("checkedIfTrue")
fun checkedIfTrue(checkBox: AppCompatCheckBox, boo: Boolean) {
    checkBox.isChecked = boo
}
