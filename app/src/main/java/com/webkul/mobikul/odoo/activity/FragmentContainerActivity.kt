package com.webkul.mobikul.odoo.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ActivityPaymentContainerBinding
import com.webkul.mobikul.odoo.fragment.*
import com.webkul.mobikul.odoo.helper.FragmentHelper
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData
import com.webkul.mobikul.odoo.model.payments.Result

class FragmentContainerActivity : BaseActivity() {

    private val TAG = "FragmentContainerActivity"
    private lateinit var binding: ActivityPaymentContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_container)
        setToolbar()
        val fragment = setArgs()
        FragmentHelper.addFragment(R.id.fragment_container, this, fragment, "TAG", false, true)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.tbPaymentContainer.toolbar)
        showBackButton(true)
    }

    private fun setArgs(): Fragment {
        val extras = intent.extras
        val fragmentType = extras?.get(BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE)
        var fragment = Fragment()
        when (fragmentType) {
            BundleConstant.BUNDLE_ADDRESS_BOOK_SCREEN -> fragment = setUpAddressfragment()
            BundleConstant.BUNDLE_SHIPPING_METHOD_SCREEN -> fragment = setupShippingMethodFragment()
            BundleConstant.BUNDLE_PAYMENT_METHOD_SCREEN -> fragment = setupPaymentMethodFragment()
            BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN -> fragment =
                setUpPaymentStatusFragment(extras.getInt(BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN))
            BundleConstant.BUNDLE_PAYMENT_PROCESSING_SCREEN -> fragment =
                setupPaymentProcessingFragment()
            BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN -> fragment = setupPendingPaymentFragment()
            BundleConstant.BUNDLE_ORDER_DETAIL_SCREEN -> fragment = setUpOrderDetailScreen()
            BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN_VA -> fragment =
                setupVirtualAccountPendingPaymentfragment()
            BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION -> fragment =
                setupTransferInstructionFragment()
        }
        return fragment
    }

    private fun setupTransferInstructionFragment(): Fragment {
        val transferInstructionFragment = TransferInstructionFragment()
        val bundle = Bundle()
        val instruction: TransferInstruction =
            intent.extras?.getParcelable(BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION)!!
        bundle.putParcelable(BundleConstant.BUNDLE_KEY_TRANSFER_INSTRUCTION, instruction)
        transferInstructionFragment.arguments = bundle
        return transferInstructionFragment
    }

    private fun setupVirtualAccountPendingPaymentfragment(): Fragment {
        val paymentVirtualAccountFragment = PaymentVirtualAccountFragment()
        val bundle = Bundle()
        val pendingVAPaymentData: Result =
            intent.extras?.getParcelable(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN_VA)!!
        val amount = intent?.extras?.getString(BundleConstant.BUNDLE_KEY_ORDER_AMOUNT)
        bundle.putParcelable(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN_VA, pendingVAPaymentData)
        bundle.putString(BundleConstant.BUNDLE_KEY_ORDER_AMOUNT, amount)
        paymentVirtualAccountFragment.arguments = bundle
        return paymentVirtualAccountFragment
    }

    private fun setUpOrderDetailScreen(): OrderFragment {
        val orderId = intent.extras?.getString(BundleConstant.BUNDLE_ORDER_DETAIL_SCREEN)
        val orderFragment = OrderFragment.newInstance(orderId, getString(R.string.order_details))
        return orderFragment
    }

    private fun setUpAddressfragment(): CheckoutAddressBookFragment {
        val orderId = intent.extras?.getInt(BundleConstant.BUNDLE_KEY_ORDER_ID) as Int
        return CheckoutAddressBookFragment.newInstance(orderId)
    }


    private fun setupCouponsListFragment(): CouponsListfragment {
        return CouponsListfragment()
    }

    private fun setupShippingMethodFragment(): ShippingMethodFragment {
        val shippingMethodFragment = ShippingMethodFragment()
        val bundle = Bundle()
        val orderId = intent.extras?.getInt(BundleConstant.BUNDLE_KEY_ORDER_ID)
        orderId?.let { bundle.putInt(BundleConstant.BUNDLE_KEY_ORDER_ID, it) }
        shippingMethodFragment.arguments = bundle
        return shippingMethodFragment
    }

    private fun setupPaymentMethodFragment(): PaymentAcquirerFragment {
        val paymentAcquirerFragment = PaymentAcquirerFragment()
        return paymentAcquirerFragment
    }

    private fun setupPaymentVAInfoFragment1(): PaymentVirtualAccountFragment {
        val paymentVirtualAccountFragment = PaymentVirtualAccountFragment()
        val bundle = Bundle()
        val orderId = intent.extras?.getInt(BundleConstant.BUNDLE_KEY_ORDER_ID)
        orderId?.let { bundle.putInt(BundleConstant.BUNDLE_KEY_ORDER_ID, it) }
        paymentVirtualAccountFragment.arguments = bundle
        return paymentVirtualAccountFragment
    }
    private fun setupPaymentProcessingFragment(): PaymentProcessingFragment {
        val paymentProcessingFragment = PaymentProcessingFragment()
        val bundle = Bundle()
        val createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest =
            intent.extras?.getParcelable(BundleConstant.BUNDLE_ORDER_COST_DATA)!!
        val orderId = intent.extras?.getString(BundleConstant.BUNDLE_KEY_ORDER_ID)
        bundle.putParcelable(
            BundleConstant.BUNDLE_ORDER_COST_DATA,
            createVirtualAccountPaymentRequest
        )
        bundle.putString(BundleConstant.BUNDLE_KEY_ORDER_ID, orderId.toString())
        paymentProcessingFragment.arguments = bundle
        return paymentProcessingFragment
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun getScreenTitle(): String {
        return this.javaClass.simpleName
    }

    private fun setUpPaymentStatusFragment(paymentStatus: Int): PaymentStatusFragment {
        val paymentStatusFragment = PaymentStatusFragment()
        val bundle = Bundle()
        bundle.putInt(BundleConstant.BUNDLE_PAYMENT_MESSAGE, paymentStatus)
        paymentStatusFragment.arguments = bundle
        return paymentStatusFragment
    }

    private fun setupPendingPaymentFragment(): PaymentPendingFragment {
        val pendingPaymentFragment = PaymentPendingFragment()
        val bundle = Bundle()
        val pendingPaymentData: PendingPaymentData =
            intent.extras?.getParcelable(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN)!!
        bundle.putParcelable(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN, pendingPaymentData)
        pendingPaymentFragment.arguments = bundle
        return pendingPaymentFragment
    }

    fun setToolbarText(string: String) {
        getSupportActionBar()?.setTitle(string)
    }

    private fun setUpIncompletePaymentFragment(): PaymentIncompleteFragment {
        val paymentIncompleteFragment = PaymentIncompleteFragment()
        return paymentIncompleteFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}