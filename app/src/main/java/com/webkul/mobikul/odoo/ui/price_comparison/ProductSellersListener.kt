package com.webkul.mobikul.odoo.ui.price_comparison

interface ProductSellersListener {
    fun onSellerProductPlusClick(position:Int)
    fun onSellerProductMinusClick(position:Int)
    fun onSellerProductEditTextClick(qty : Int, position : Int)
    fun onSellerProductAddToCartClick(position:Int)
    fun onSellerProductSellerNameClick(position: Int)
    fun onSellerProductChatClick(position: Int)
}