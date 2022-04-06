package com.webkul.mobikul.odoo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity
import com.webkul.mobikul.odoo.activity.UpdateAddressActivity
import com.webkul.mobikul.odoo.adapter.checkout.CheckoutAddressRecyclerViewAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.FragmentCheckoutAddressBookBinding
import com.webkul.mobikul.odoo.handler.customer.CheckoutAddressRecyclerViewHandler
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.IntentHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequestModel
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CheckoutAddressBookFragment : BaseFragment(),
    CheckoutAddressRecyclerViewAdapter.ShippingAddressSelectedListener,
    CheckoutAddressRecyclerViewHandler.OnAddressDeletedListener {

    override val TAG = "AddressBookFragment"
    private lateinit var binding: FragmentCheckoutAddressBookBinding
    private var orderId: Int? = null
    private var addressData: AddressData? = null
    private lateinit var dialog: SweetAlertDialog

    companion object {
        fun newInstance(orderId: Int): CheckoutAddressBookFragment {
            val bundle = Bundle()
            bundle.putInt(BundleConstant.BUNDLE_KEY_ORDER_ID, orderId)
            val checkoutAddressBookFragment = CheckoutAddressBookFragment()
            checkoutAddressBookFragment.arguments = bundle
            return checkoutAddressBookFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_checkout_address_book,
            container,
            false
        )
        getArgument()
        setButtonClickListner()
        return binding.getRoot()
    }

    private fun getArgument() {
        if (arguments?.containsKey(BundleConstant.BUNDLE_KEY_ORDER_ID)!!)
            orderId = arguments?.getInt(BundleConstant.BUNDLE_KEY_ORDER_ID)
    }

    override fun onResume() {
        super.onResume()
        setVisibility(binding.root, View.INVISIBLE)
        callApi()
    }

    fun callApi() {
        ApiConnection.getAddressBookData(context, BaseLazyRequest(0, 0))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<MyAddressesResponse?>(requireContext()) {
                override fun onNext(myAddressesResponse: MyAddressesResponse) {
                    super.onNext(myAddressesResponse)
                    if (myAddressesResponse.isAccessDenied) {
                        IntentHelper.redirectToSignUpActivity(requireContext())
                    } else {
                        setVisibility(binding.root, View.VISIBLE)
                        handleAddressResponse(myAddressesResponse)
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    AlertDialogHelper.showDefaultProgressDialog(requireContext())
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                }

                override fun onComplete() {}
            })
    }

    private fun handleAddressResponse(myAddressesResponse: MyAddressesResponse) {
        myAddressesResponse.setContext(requireContext())
        binding.data = myAddressesResponse
        binding.rvAddress.adapter = CheckoutAddressRecyclerViewAdapter(
            requireContext(), myAddressesResponse.addresses,
            this@CheckoutAddressBookFragment, this@CheckoutAddressBookFragment
        )
    }

    private fun setButtonClickListner() {
        (requireActivity() as FragmentContainerActivity).setToolbarText(getString(R.string.address))
        binding.tvAddNewAddress.setOnClickListener { v ->
            startActivity(Intent(requireActivity(), UpdateAddressActivity::class.java))
        }
        binding.btnConfirmAddress.setOnClickListener { v -> validateitemSelection() }
    }

    private fun validateitemSelection() {
        if (addressData == null) {
            showError(getString(R.string.select_your_shipping_address))
        } else {
            if (addressData?.displayName?.isEmpty() == true) {
                showAlertDialog()
            } else {
                updateOrderShippingAddress()
            }
        }
    }

    private fun showError(string: String) {
        SnackbarHelper.getSnackbar(
            requireActivity(), string,
            Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING
        ).show()
    }

    private fun showAlertDialog() {
        dialog = AlertDialogHelper.getAlertDialog(
            requireContext(),
            SweetAlertDialog.WARNING_TYPE,
            getString(R.string.service_unavailable),
            getString(R.string.service_not_availabe_text),
            false,
            true
        )
        dialog.setConfirmText(getString(R.string.ok))
            .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog? -> dialog.hide() }
        dialog.show()
    }

    private fun updateOrderShippingAddress() {
        val updateOrderRequest =
            UpdateOrderRequest(UpdateOrderRequestModel(null, addressData!!.addressId.toInt()))
        ApiConnection.updateOrderData(context, orderId!!, updateOrderRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<BaseResponse?>(requireContext()) {
                override fun onNext(baseResponse: BaseResponse) {
                    super.onNext(baseResponse)
                    if (baseResponse.isAccessDenied) {
                        IntentHelper.redirectToSignUpActivity(requireContext())
                    } else {
                        setDataAndFinish()
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    AlertDialogHelper.showProgressDialogWithText(
                        requireContext(),
                        getString(R.string.updating_shipping_address_text)
                    )
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                }

                override fun onComplete() {}
            })
    }

    private fun setDataAndFinish() {
        val intent = Intent()
        intent.putExtra(BundleConstant.BUNDLE_SELECTED_ADDRESS, addressData)
        requireActivity().setResult(Activity.RESULT_OK, intent)
        requireActivity().finish()
    }

    override var title: String
        get() = this.javaClass.simpleName
        set(value) {}

    override fun onAdditionalAddressDeleted() {
        callApi()
    }

    override fun updateShippingAddress(addressData: AddressData) {
        this.addressData = addressData
    }

    fun setVisibility(view: View, state: Int) {
        view.visibility = state
    }

}