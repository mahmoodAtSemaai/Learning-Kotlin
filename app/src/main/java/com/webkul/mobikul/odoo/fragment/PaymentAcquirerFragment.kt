package com.webkul.mobikul.odoo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.adapter.checkout.VirtualAccountAdapter
import com.webkul.mobikul.odoo.adapter.checkout.VirtualAccountAdapter.onVirtualAccountOptionSelected
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentNewPaymentAcquirerBinding
import com.webkul.mobikul.odoo.helper.ColorHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.payments.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PaymentAcquirerFragment : Fragment(), onVirtualAccountOptionSelected {

    private val TAG = "PaymentAcquirerFragment"
    private lateinit var binding: FragmentNewPaymentAcquirerBinding
    private var isCODSelected = false
    private var bankVirtualAccountAdapterListener: onVirtualAccountOptionSelected? = null
    private val COMPANY_ID = 1
    private var COD_ID: Int = -1
    private var paymentMethodProvider: MutableList<PaymentAcquirerMethod> = arrayListOf()
    private var selectedPaymentMethod: SelectedPaymentMethod? = null

    private lateinit var dialog: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_payment_acquirer,
            container,
            false
        )
        dialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        bankVirtualAccountAdapterListener = this
        setUpViews()
        return binding.root
    }

    private fun setUpViews() {
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.payment_method))
        setVisibility(binding.layoutGroup, View.INVISIBLE)
        setUpCODHeading()
        setUpBankTransferHeading()
        setUpDialog()
        getPaymentAcquirers()
        setUpOnClickListeners()
    }

    private fun setUpDialog() {
        dialog.titleText = getString(R.string.payment_options_loading_text)
        dialog.progressHelper.barColor = ColorHelper.getColor(context, R.attr.colorAccent)
        dialog.setCancelable(false)
    }

    private fun getPaymentAcquirers() {
        ApiConnection.getPaymentAcquirers(requireContext(), COMPANY_ID).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<PaymentAcquirersResponse?>(requireContext()) {
                override fun onSubscribe(d: Disposable) {
                    dialog.titleText = getString(R.string.payment_options_loading_text)
                    dialog.show()
                }

                override fun onNext(paymentAcquirersResponse: PaymentAcquirersResponse) {
                    super.onNext(paymentAcquirersResponse)
                    for (item in paymentAcquirersResponse.result.acquirers) {
                        if (!item.isMobikulAvailable) {
                            getPaymentAcquirerMethods(item.id)
                        } else {
                            COD_ID = item.id
                        }
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    dialog.hide()
                    SnackbarHelper.getSnackbar(
                        requireActivity(), getString(R.string.error),
                        Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                }
            })
    }

    private fun getPaymentAcquirerMethods(paymentVendorId: Int) {
        ApiConnection.getPaymentAcquirersMethods(requireContext(), paymentVendorId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<PaymentAcquirerMethodResponse>(requireContext()) {

                override fun onNext(paymentAcquirerMethodResponse: PaymentAcquirerMethodResponse) {
                    super.onNext(paymentAcquirerMethodResponse)
                    for (item in paymentAcquirerMethodResponse.result.paymentAcquirerMethods) {
                        if (item.type.equals(getString(R.string.va_text), true))
                            fetchBanks(item.id, paymentVendorId)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    SnackbarHelper.getSnackbar(
                        requireActivity(), getString(R.string.error),
                        Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                    dialog.hide()
                }
            })
    }

    private fun fetchBanks(paymentMethodTypeId: Int, paymentVendorId: Int) {
        ApiConnection.getPaymentAcquirerMethodProviders(requireContext(), paymentMethodTypeId, paymentVendorId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :
                CustomObserver<PaymentAcquirerMethodProviderResponse?>(requireContext()) {
                override fun onNext(paymentAcquirerMethodProviderResponse: PaymentAcquirerMethodProviderResponse) {
                    super.onNext(paymentAcquirerMethodProviderResponse)
                    for (item in paymentAcquirerMethodProviderResponse.result.paymentMethodProvider) {
                        item.vendorID = paymentVendorId
                    }
                    paymentMethodProvider.addAll(paymentAcquirerMethodProviderResponse.result.paymentMethodProvider)
                    setUpRecyclerView(paymentMethodProvider)
                    dialog.hide()
                    setVisibility(binding.layoutGroup, View.VISIBLE)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    SnackbarHelper.getSnackbar(
                        requireActivity(), getString(R.string.error),
                        Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                    dialog.hide()
                }
            })
    }

    private fun setUpRecyclerView(paymentMethodProvider: MutableList<PaymentAcquirerMethod>) {
        binding.rvPaymentMethodVaOptions.adapter = VirtualAccountAdapter(
            requireContext(),
            paymentMethodProvider,
            bankVirtualAccountAdapterListener!!
        )
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvPaymentMethodVaOptions.addItemDecoration(dividerItemDecoration)
    }


    private fun setUpOnClickListeners() {
        binding.btnConfirmPaymentMethod.setOnClickListener { v ->
            if (isCODSelected) {
                returnResultToCheckout(
                    SelectedPaymentMethod(
                        getString(R.string.cod_text), COD_ID.toString(), "", "", true
                    )
                )
            } else {
                if (selectedPaymentMethod == null)
                    SnackbarHelper.getSnackbar(
                        requireActivity(),
                        getString(R.string.checkout_select_your_payment_method),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                else {
                    returnResultToCheckout(
                        SelectedPaymentMethod(
                            selectedPaymentMethod?.paymentMethodText,
                            selectedPaymentMethod?.paymentAcquirerId,
                            selectedPaymentMethod?.paymentAcquirerProviderId,
                            selectedPaymentMethod?.paymentAcquirerProviderType,
                            false
                        )
                    )
                }
            }
        }
    }

    private fun returnResultToCheckout(selectedPaymentMethod: SelectedPaymentMethod) {
        val intent = Intent()
        intent.putExtra(BundleConstant.BUNDLE_SELECTED_PAYMENT_METHOD, selectedPaymentMethod)
        setResultAndFinish(intent)
    }

    private fun setResultAndFinish(intent: Intent) {
        requireActivity().setResult(Activity.RESULT_OK, intent)
        dialog.dismiss()
        requireActivity().finish()
    }

    private fun setUpCODHeading() {
        binding.layoutCod.tvPaymentMethod.setText(getString(R.string.cod_text))
        binding.layoutCod.root.setOnClickListener { v ->
            if (!isCODSelected) {
                binding.layoutCod.ivOption.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.circular_radio_button_selected_state,
                        null
                    )
                )
                if (selectedPaymentMethod == null) {
                    selectedPaymentMethod =
                        SelectedPaymentMethod("", "", "", "", true)
                }
                selectedPaymentMethod?.paymentMethodText = getString(R.string.cod_text)
                selectedPaymentMethod?.paymentAcquirerId = COD_ID.toString()
                selectedPaymentMethod?.paymentAcquirerProviderType = ""
                selectedPaymentMethod?.paymentAcquirerProviderId = ""
                selectedPaymentMethod?.isCOD = true
                isCODSelected = true
                setUpRecyclerView(paymentMethodProvider)
            }
        }
    }

    private fun setUpBankTransferHeading() {
        binding.layoutBankTransfer.ivPaymentMethod.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_bank_transfer,
                null
            )
        )
        binding.layoutBankTransfer.tvPaymentMethod.setText(getString(R.string.bank_transfer_va))
        binding.layoutBankTransfer.ivOption.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_up_arrow_grey,
                null
            )
        )
        binding.layoutBankTransfer.root.setOnClickListener { v ->
            if (binding.rvPaymentMethodVaOptions.getVisibility() == View.VISIBLE) {
                binding.rvPaymentMethodVaOptions.setVisibility(View.GONE)
                binding.layoutBankTransfer.ivOption.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_down_arrow_grey,
                        null
                    )
                )
            } else {
                binding.rvPaymentMethodVaOptions.setVisibility(View.VISIBLE)
                binding.layoutBankTransfer.ivOption.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_up_arrow_grey,
                        null
                    )
                )
            }
        }
    }

    fun setVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    override fun updatePaymentOption(paymentAcquirerMethod: PaymentAcquirerMethod) {
        binding.layoutCod.ivOption.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.circular_radio_button_unselected_state,
                null
            )
        )
        isCODSelected = false
        selectedPaymentMethod = SelectedPaymentMethod(
            paymentAcquirerMethod.name,
            paymentAcquirerMethod.vendorID.toString(),
            paymentAcquirerMethod.id.toString(),
            paymentAcquirerMethod.type,
            false
        )
    }

}