package com.webkul.mobikul.odoo.ui.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.databinding.ActivityCheckoutV1Binding
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutActivityV1 @Inject constructor() :
    BindingBaseActivity<ActivityCheckoutV1Binding>() {

    override val contentViewId: Int
        get() = R.layout.activity_checkout_v1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeManager.setLocale(false, this)
        addFragmentWithBackStack(
            R.id.fcv_container,
            CheckoutDashboardFragmentV1.newInstance(
                intent.getIntExtra(CART_ID, -1),
                intent.getBooleanExtra(POINTS_REDEEMED, false),
                intent.getIntegerArrayListExtra(LINE_IDS)!!)
            , null)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (this.supportFragmentManager.backStackEntryCount == 0) finish()
    }

    companion object{

        const val CART_ID = "CART_ID"
        const val POINTS_REDEEMED = "POINTS_REDEEMED"
        const val LINE_IDS = "LINE_IDS"

        fun newInstance(context: Context, cartId: Int, pointsRedeemed: Boolean, lineIds: ArrayList<Int>) : Intent {
            return Intent(context, CheckoutActivityV1::class.java).apply {
                putExtra(CART_ID, cartId)
                putExtra(POINTS_REDEEMED, pointsRedeemed)
                putIntegerArrayListExtra(LINE_IDS, lineIds)
            }
        }
    }
}