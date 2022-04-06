package com.webkul.mobikul.odoo.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity
import com.webkul.mobikul.odoo.adapter.checkout.ShippingMethodAdapter
import com.webkul.mobikul.odoo.adapter.checkout.ShippingMethodAdapter.ShippingMethodSelectionListener
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentNewShippingMethodBinding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.checkout.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ShippingMethodFragment : Fragment(), ShippingMethodSelectionListener {

    lateinit var binding: FragmentNewShippingMethodBinding
    private val TAG_EXTRA_INFO = 1
    private var shippingMethod: ActiveShippingMethod? = null
    private var isShippingMethodSelected = false
    private var shippingMethodSelectionListener: ShippingMethodSelectionListener? = null
    private lateinit var dialog: SweetAlertDialog
    private val TAG = "ShippingMethodFragment"
    private var orderId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_shipping_method,
            container,
            false
        )
        shippingMethodSelectionListener = this
        getArgument()
        setupDialog()
        fetchShippingMethods()
        setListeners()
        return binding.root
    }

    private fun setupDialog() {
        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(), SweetAlertDialog.PROGRESS_TYPE,
            getString(R.string.shipping_options_loading_text), "", false, false
        )
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.shipping_method))
    }

    private fun getArgument() {
        orderId = arguments?.get(BundleConstant.BUNDLE_KEY_ORDER_ID) as Int
    }

    private fun fetchShippingMethods() {
        ApiConnection.getActiveShippings(requireContext()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getShippingMethodResponseObserver())
    }

    private fun getShippingMethodResponseObserver(): Observer<in ShippingMethodResponse?> {
        return object : CustomObserver<ShippingMethodResponse?>(requireContext()) {
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                dialog.show()
            }

            override fun onNext(shippingMethodResponse: ShippingMethodResponse) {
                super.onNext(shippingMethodResponse)
                if (shippingMethodResponse.isAccessDenied) {
                    redirectToSignInSignUp()
                } else {
                    handleShippingMethodResponse(shippingMethodResponse)
                }
            }

            override fun onComplete() {
                super.onComplete()
                dialog.hide()
            }
        }
    }

    private fun setListeners() {
        binding.btnConfirmShippingMethod.setOnClickListener {
            if (!isShippingMethodSelected) {
                SnackbarHelper.getSnackbar(
                    requireActivity(), getString(R.string.checkout_select_a_shipping_method),
                    Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING
                ).show()
            } else {
                updateShippingMethodInOrder(orderId, shippingMethod!!.id)
            }
        }
    }

    fun updateShippingMethodInOrder(orderId: Int, id: Int) {
        val updateOrderRequest = UpdateOrderRequest(UpdateOrderRequestModel(id, null))
        ApiConnection.updateOrderData(requireContext(), orderId, updateOrderRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<BaseResponse?>(requireActivity()) {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    dialog.titleText = getString(R.string.updating_shipping_method_text)
                    dialog.show()
                }

                override fun onNext(response: BaseResponse) {
                    super.onNext(response)
                    handleUpdateResponse()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    SnackbarHelper.getSnackbar(
                        requireActivity(),
                        getString(R.string.error_something_went_wrong),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    )
                }
            })
    }

    private fun handleUpdateResponse() {
        dialog.apply {
            hide()
            dismiss()
        }
        val intent = Intent()
        intent.putExtra(BundleConstant.BUNDLE_SELECTED_SHIPPING_METHOD, shippingMethod?.name)
        requireActivity().setResult(Activity.RESULT_OK, intent)
        requireActivity().finish()
    }


    private fun redirectToSignInSignUp() {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(
            requireContext(),
            getString(R.string.error_login_failure),
            getString(R.string.access_denied_message)
        ) { sweetAlertDialog: SweetAlertDialog ->
            sweetAlertDialog.dismiss()
            AppSharedPref.clearCustomerData(requireContext())
            val signInSignUpIntent = Intent(requireContext(), SignInSignUpActivity::class.java)
            signInSignUpIntent.putExtra(
                BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                requireActivity().javaClass.getSimpleName()
            )
            requireContext().startActivity(signInSignUpIntent)
            requireActivity().finish()
        }
    }

    private fun handleShippingMethodResponse(shippingMethodResponse: ShippingMethodResponse) {
        binding.rvShippingMethod.adapter = ShippingMethodAdapter(
            requireContext(),
            shippingMethodResponse.shippingMethods,
            shippingMethodSelectionListener!!
        )
    }


    override fun updateShippingMethod(activeShippingMethod: ActiveShippingMethod) {
        this.shippingMethod = activeShippingMethod
        isShippingMethodSelected = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null)
            dialog.hide()
    }
}