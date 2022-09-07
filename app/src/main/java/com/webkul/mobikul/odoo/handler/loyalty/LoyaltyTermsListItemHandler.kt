package com.webkul.mobikul.odoo.handler.loyalty

import android.content.Context
import android.content.Intent
import android.util.Base64
import com.bumptech.glide.Glide
import com.webkul.mobikul.odoo.activity.LoyaltyTermsActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ItemLoyaltyBannerBinding
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsData

class LoyaltyTermsListItemHandler(
    private val context: Context,
    private val loyaltyTermsModel: LoyaltyTermsData,
    private val binding: ItemLoyaltyBannerBinding
) {

    fun setView() {
        binding.data = loyaltyTermsModel
        showImage(loyaltyTermsModel.imageUrl)
        binding.clLoyaltyTermsItem.setOnClickListener {
            launchTermsNConditionsActivity()
        }
    }

    private fun launchTermsNConditionsActivity() {
        Intent(context, LoyaltyTermsActivity::class.java).apply {
            putExtra(BundleConstant.BUNDLE_KEY_LOYALTY_BANNER_ID, loyaltyTermsModel.id)
            context.startActivity(this)
        }
    }

    private fun showImage(imageUrl: String){
        val imageByteArray: ByteArray = Base64.decode(imageUrl, Base64.DEFAULT)
        Glide.with(context)
            .asBitmap()
            .load(imageByteArray)
            .into(binding.ivLoyaltyBanner)
    }

}

