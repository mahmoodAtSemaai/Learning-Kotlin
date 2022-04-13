package com.webkul.mobikul.odoo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentPaymentStatusBinding
import com.webkul.mobikul.odoo.helper.IntentHelper
import com.webkul.mobikul.odoo.model.payments.PaymentStatusDescription

class PaymentStatusFragment : Fragment() {

    private lateinit var binding: FragmentPaymentStatusBinding
    private val TAG = "PaymentStatusFragment"

    companion object {
        const val SHOW_PAYMENT_COMPLETE_MESSAGE = 1
        const val SHOW_PAYMENT_FAILURE_MESSAGE = 2
        const val SHOW_PAYMENT_COD_MESSAGE = 3
        const val SHOPEE_PAY_ACTIVATED_MESSAGE = 4
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment_status, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getArgs()
    }

    private fun getArgs() {
        if (arguments?.containsKey(BundleConstant.BUNDLE_PAYMENT_MESSAGE) == true) {
            val paymentSuccessful = arguments?.getInt(BundleConstant.BUNDLE_PAYMENT_MESSAGE)
            when (paymentSuccessful) {
                SHOW_PAYMENT_COMPLETE_MESSAGE -> {
                    setUpUI(
                        PaymentStatusDescription(
                            getString(R.string.payment_n_status),
                            getString(R.string.continue_shopping),
                            getString(R.string.paid),
                            getString(R.string.payment_success_text),
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_green_bg, null),
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_green_tick, null)
                        )
                    )
                }
                SHOW_PAYMENT_FAILURE_MESSAGE -> {
                    setUpUI(
                        PaymentStatusDescription(
                            getString(R.string.payment_n_status),
                            getString(R.string.continue_shopping),
                            getString(R.string.payment_failure),
                            getString(R.string.payment_failure_text),
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_grey_bg, null),
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_cross, null)
                        )
                    )
                }
                SHOW_PAYMENT_COD_MESSAGE -> {
                    setUpUI(
                        PaymentStatusDescription(
                            getString(R.string.payment_n_status),
                            getString(R.string.continue_shopping),
                            getString(R.string.your_order_will_be_delivered_soon),
                            "",
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_green_bg, null),
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_cod, null)
                        )
                    )
                }
                SHOPEE_PAY_ACTIVATED_MESSAGE -> {
                    setUpUI(
                        PaymentStatusDescription(
                            getString(R.string.activate_shopee_pay_text),
                            getString(R.string.continue_),
                            getString(R.string.shopee_activated_text),
                            "",
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_orange_bg, null),
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_orange_tick, null)
                        )
                    )
                }
            }
        }
    }

    private fun setUpUI(paymentStatusDescription: PaymentStatusDescription) {
        binding.apply {
            (requireActivity() as FragmentContainerActivity).setToolbarText(paymentStatusDescription.activityToolbarText)
            btnContinue.setText(paymentStatusDescription.buttonText)
            tvPaymentStatusHeading.setText(paymentStatusDescription.headingText)
            tvPaymentStatusDesc.setText(paymentStatusDescription.descriptionText)
            ivPaymentStatusBg.background = paymentStatusDescription.drawable
            ivPaymentStatusIcon.setImageDrawable(paymentStatusDescription.icon)
        }
        setClickListners()
    }

    private fun setClickListners() {
        binding.btnContinue.setOnClickListener {
            IntentHelper.continueShopping(requireContext())
        }
    }

}