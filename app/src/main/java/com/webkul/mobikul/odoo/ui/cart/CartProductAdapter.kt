package com.webkul.mobikul.odoo.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.data.entity.CartProductEntity
import com.webkul.mobikul.odoo.databinding.ItemCartBinding
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.helper.StringUtil
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2

class CartProductAdapter(
    val context: Context,
    val listener: ShowDialogFragmentListener,
    private val products: ArrayList<CartProductEntity>,
    private val sellerPosition: Int
) :
    RecyclerView.Adapter<CartProductAdapter.ProductHolder>(){

    private var cartItemUpdateListener: ShowDialogFragmentListener = listener

    private val TAG = "CartProductAdapter"
    private val NEVER = "never"
    private val ALWAYS = "always"
    private val THRESHOLD = "threshold"
    private val PREORDER = "pre-order"

    val languageCode by lazy {
        AppSharedPref.getLanguageCode(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    @SuppressLint("LogNotTimber")
    override fun onBindViewHolder(holder: ProductHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.apply {
            val product = products[position]
            product.isChecked = ObservableBoolean(false)
            product.bindingQuantity = product.quantity
            product.localQuantity = product.quantity
            data = product
            tvMoveToWishlist.setOnClickListener {
                addItemToWishList(this.tvMoveToWishlist, product)

            }
            etQuantity.setText(if(product.isOutOfStock()) context.getString(R.string.quantity_zero) else product.quantity.toString())
            setSubtotal(tvSubTotalValue, context, product)


            cbProductSelection.setOnClickListener {
                clearFocusFromQuantityEdittext(root, etQuantity)
                val isChecked = (it as AppCompatCheckBox).isChecked
                product.isChecked.set(isChecked)
                handleCheckBoxState(isChecked, product, position)
            }


            btnPlus.setOnClickListener {
                clearFocusFromQuantityEdittext(this.root, etQuantity)
                handleQuantityIncrease(holder.binding.tvSubTotalValue, product, position)
            }

            btnMinus.setOnClickListener {
                clearFocusFromQuantityEdittext(this.root, etQuantity)
                handleQuantityDecreased(holder.binding.tvSubTotalValue, product, position)
            }

            ivDelete.setOnClickListener {
                clearFocusFromQuantityEdittext(this.root, etQuantity)
                cartItemUpdateListener.onDeleteClicked(this@CartProductAdapter.sellerPosition, products[position].lineId,position)
            }

            ivProduct.setOnClickListener {
                redirectToProductDetailScreen(product)
            }

            tvProductName.setOnClickListener {
                redirectToProductDetailScreen(product)
            }

            this.root.setOnClickListener {
                if(etQuantity.hasFocus()) {
                    clearFocusFromQuantityEdittext(this.root, etQuantity)
                }
            }

            etQuantity.addTextChangedListener(object : TextWatcher {
                private var originalQuantity = 0

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}

                override fun onTextChanged(quantity: CharSequence?, start: Int, before: Int, count: Int) {
                    if(etQuantity.hasFocus()) {
                        if (quantity.isNullOrBlank()) {
                            product.quantity = 1
                            onQuantityChanged(tvSubTotalValue, product, 1, position)
                        } else {
                            val qty = quantity.toString().toInt()
                            onQuantityChanged(tvSubTotalValue, product, qty, position)
                        }
                    }
                }
            })

            etQuantity.setOnEditorActionListener { _, actionId, _ ->
                checkQuantityActionDone(tvSubTotalValue, product, etQuantity, actionId, position)
                false
            }

        }
    }

    private fun addItemToWishList(tvMoveToWishlist: AppCompatTextView, product: CartProductEntity) {
        if(product.isWishListed.not()) {
            cartItemUpdateListener.addItemToWishList(product.productId)
            tvMoveToWishlist.apply {
                text = context.getString(R.string.already_favorite)
                tvMoveToWishlist.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGrey))
            }
            product.isWishListed = true
        }
    }

    private fun handleQuantityDecreased(tvSubTotalValue: AppCompatTextView, product: CartProductEntity, position: Int) {
        var qty = product.quantity
        if(qty <= 1)
            cartItemUpdateListener.onDeleteClicked(position, products[position].lineId, position)
        else {
            qty--
            product.quantity = qty
            setSubtotal(tvSubTotalValue, context, product)
            if(product.isChecked.get()) {
                product.localQuantity = qty
                cartItemUpdateListener.updateCartItemQuantity(
                    this@CartProductAdapter.sellerPosition, position, product.lineId, qty, 0, true)
            }
        }
    }

    private fun handleQuantityIncrease(
        tvSubTotalValue: AppCompatTextView,
        product: CartProductEntity,
        position: Int
    ) {
        var qty = product.quantity
        if (qty >= product.availableQuantity && product.inventoryAvailability.equals(ALWAYS))
            cartItemUpdateListener.showQuantityExceedingError()
        else {
            qty++
            product.quantity = qty
            setSubtotal(tvSubTotalValue, context, product)
            if(product.isChecked.get()) {
                product.localQuantity = qty
                cartItemUpdateListener.updateCartItemQuantity(
                    this@CartProductAdapter.sellerPosition, position, product.lineId, qty, 0, true)
            }
        }
    }

    private fun handleCheckBoxState(isChecked: Boolean, product: CartProductEntity, position: Int) {
        val isAllChecked = !(products.any { !it.isChecked.get() })
        cartItemUpdateListener.allProductSelected(this@CartProductAdapter.sellerPosition, isAllChecked)
        if(isChecked) {
            if(product.localQuantity == product.quantity)
                cartItemUpdateListener.onProductSelected(sellerPosition)
            else {
                product.localQuantity = product.quantity
                cartItemUpdateListener.updateCartItemQuantity(sellerPosition, position,
                    product.lineId, product.localQuantity, 0, true)
            }
        }
        else{
            cartItemUpdateListener.onProductSelected(sellerPosition)
        }
    }


    private fun clearFocusFromQuantityEdittext(root: View, etQuantity: AppCompatEditText) {
        Helper.hideKeyboard(root.context)
        etQuantity.clearFocus()
    }

    @SuppressLint("SetTextI18n")
    private fun setSubtotal(tvSubTotalValue: AppCompatTextView, context: Context, product: CartProductEntity) {
        if(languageCode == context.getString(R.string.ind_lang))
            tvSubTotalValue.text = context.getString(R.string.rp) + StringUtil.getPriceInBahasa(
                if (product.priceReduce.isEmpty()) product.priceUnit else product.priceReduce, product.quantity)
        else
            tvSubTotalValue.text = context.getString(R.string.rp) + StringUtil.getPriceInEnglish(
                if (product.priceReduce.isEmpty()) product.priceUnit else product.priceReduce, product.quantity)
    }

    private fun checkQuantityActionDone(
        tvSubTotalValue: AppCompatTextView,
        product: CartProductEntity,
        etQuantity: AppCompatEditText,
        actionId: Int,
        position: Int
    ) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            etQuantity.clearFocus()
            if (etQuantity.text.isNullOrEmpty()) {
                onQuantityChanged(tvSubTotalValue, product, 1, position)
            } else {
                val qty = etQuantity.text.toString().toInt()
                onQuantityChanged(tvSubTotalValue, product, qty, position)
            }
        }
    }

    private fun onQuantityChanged(
        tvSubTotalValue: AppCompatTextView,
        product: CartProductEntity,
        qty: Int,
        productPosition: Int
    ) {
        val finalQuantity = isQuantityExceeding(product, qty)
        product.quantity = finalQuantity
        setSubtotal(tvSubTotalValue, context, product)
        if(product.isChecked.get()) {
            //TODO replace this with editText callback
            cartItemUpdateListener.updateCartItemQuantity(sellerPosition,
                productPosition, product.lineId, product.quantity, 0, true)
        }
    }

    private fun isQuantityExceeding(product: CartProductEntity, qty: Int): Int {
        if (qty > product.availableQuantity && product.inventoryAvailability.equals(ALWAYS)) {
            listener.showQuantityExceedingError()
            return product.availableQuantity
        }
        return qty
    }



    private fun redirectToProductDetailScreen(product: CartProductEntity) {
        AnalyticsImpl.trackProductItemSelected(Helper.getScreenName(context), product.productId.toString(), product.name)
        Intent(context, ProductActivityV2::class.java).apply {
            putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_ID, product.productId.toString())
            putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID, product.templateId)
            putExtra(BundleConstant.BUNDLE_KEY_PRODUCT_NAME, product.name)
            context.startActivity(this)
        }
    }


    override fun getItemCount(): Int = products.size

    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemCartBinding = DataBindingUtil.bind(itemView)!!

    }

    interface ShowDialogFragmentListener{
        fun onDeleteClicked(sellerPosition: Int, lineId: Int, productPosition: Int)
        fun showQuantityExceedingError()
        fun onProductSelected(sellerPosition: Int)
        fun addItemToWishList(productId: Int)
        fun allProductSelected(position : Int, isAllChecked : Boolean)
        fun updateCartItemQuantity(sellerPosition : Int, productPosition: Int, lineId: Int, quantity : Int, change : Int, refreshSelectedProductPrice : Boolean)
    }

}