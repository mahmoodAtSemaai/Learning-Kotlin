package com.webkul.mobikul.odoo.ui.checkout.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.close
import com.webkul.mobikul.odoo.core.extension.showDefaultWarningDialog
import com.webkul.mobikul.odoo.core.extension.showProgressDialogWithTitle
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BaseActivity
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.DeleteAddressEntity
import com.webkul.mobikul.odoo.databinding.FragmentCheckoutAddressBookV1Binding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutDashboardFragmentV1
import com.webkul.mobikul.odoo.ui.checkout.dashboard.CheckoutIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CheckoutAddressBookFragmentV1 @Inject constructor() : BindingBaseFragment<FragmentCheckoutAddressBookV1Binding>(),
    IView<CheckoutAddressIntent, CheckoutAddressState, CheckoutAddressEffect>,
    CheckoutAddressRecyclerViewAdapterV1.ShippingAddressSelectedListener,
    CheckoutAddressRecyclerViewHandlerV1.OnAddressClickListener {

    override val layoutId = R.layout.fragment_checkout_address_book_v1
    private val viewModel: CheckoutAddressViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
        setFragmentResultListeners()
        triggerIntent(CheckoutAddressIntent.SetInitialLayout)
        triggerIntent(CheckoutAddressIntent.SetBundleData(arguments))
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect{
                render(it)
            }
        }
    }

    private fun setFragmentResultListeners() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            UPDATE_ADDRESS_RESULT,
            this
        ) { requestKey, bundle ->
            triggerIntent(CheckoutAddressIntent.FetchAddress)
        }
    }

    private fun setToolbar() {
        binding.tbAddress.tvToolBar.text = getString(R.string.address)
    }

    private fun setOnClickListeners() {

        binding.btnConfirmAddress.setOnClickListener {
            triggerIntent(
                CheckoutAddressIntent.UpdateAddress(
                    viewModel.cartId,
                    viewModel.selectedAddress
                )
            )
        }

        binding.tvAddNewAddress.setOnClickListener { v ->
            triggerIntent(CheckoutAddressIntent.NavigateToAddAddress)
        }

        binding.tbAddress.toolbar.setNavigationOnClickListener {
            this.close()
        }


    }

    companion object {

        const val CART_ID = "CART_ID"
        const val UPDATE_ADDRESS_RESULT = "UPDATE_ADDRESS_RESULT"

        @JvmStatic
        fun newInstance(cartId: Int) =
            CheckoutAddressBookFragmentV1().apply {
                arguments = Bundle().apply {
                    putInt(CART_ID, cartId)
                }
            }
    }

    override fun render(state: CheckoutAddressState) {
        when (state) {
            is CheckoutAddressState.DeleteAddress -> onAddressDeleted(state.deleteAddressEntity)
            is CheckoutAddressState.Error -> onError(state.message, state.failureStatus)
            is CheckoutAddressState.FetchAddress -> onAddressFetched(state.addressEntity)
            is CheckoutAddressState.Idle -> {}
            is CheckoutAddressState.Loading -> showProgressDialog()
            is CheckoutAddressState.ProgressDialog -> onShowProgressDialog(state.message)
            is CheckoutAddressState.UpdateAddress -> onAddressUpdatedForOrder()
            is CheckoutAddressState.WarningDialog -> showWarningDialog(state.title!!, state.message!!)
            is CheckoutAddressState.AlertDialog -> showAlertDialog(state.title!!, state.message!!)
            is CheckoutAddressState.SetInitialLayout -> setToolbar()
        }
    }

    override fun triggerIntent(intent: CheckoutAddressIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }


    private fun onAddressDeleted(deleteAddressEntity: DeleteAddressEntity) {
        dismissProgressDialog()

        AlertDialogHelper.showDefaultSuccessDialog(
            requireContext(),
            requireContext().getString(R.string.message_deleted),
            deleteAddressEntity.message
        )

        triggerIntent(CheckoutAddressIntent.FetchAddress)
    }

    private fun onError( message: String?, failureStatus: FailureStatus) {
        dismissProgressDialog()

        showErrorState(failureStatus, message)
    }

    private fun onAddressFetched(addressEntity: AddressEntity) {
        dismissProgressDialog()

        binding.rvAddress.adapter = CheckoutAddressRecyclerViewAdapterV1(
            requireContext(), addressEntity.addresses,
            this@CheckoutAddressBookFragmentV1, this@CheckoutAddressBookFragmentV1
        )
    }

    private fun navigateToAddAddress() {

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            UpdateAddressFragmentV1.newInstance(false, null),
            null
        )
    }

    private fun navigateToEditAddress(addressData: AddressData?) {

        (requireActivity() as BaseActivity).addFragmentWithBackStack(
            R.id.fcv_container,
            UpdateAddressFragmentV1.newInstance(true, addressData?.url.toString()),
            null
        )
    }


    private fun onShowProgressDialog(message: String?) {
        requireContext().showProgressDialogWithTitle(message!!)
    }

    private fun onAddressUpdatedForOrder() {
        dismissProgressDialog()

        requireActivity().supportFragmentManager.setFragmentResult(
            CheckoutDashboardFragmentV1.SHIPPING_ADDRESS_RESULT,
            Bundle().also {
                it.putBoolean(CheckoutDashboardFragmentV1.RESULT, true)
            }
        )
       this.close()
    }

    private fun showWarningDialog(title: String, message: String) {
        dismissProgressDialog()
        requireContext().showDefaultWarningDialog(title, message)
    }


    private fun showAlertDialog(title: String, message: String) {
        dismissProgressDialog()

        AlertDialogHelper.getAlertDialog(
            requireContext(),
            SweetAlertDialog.WARNING_TYPE,
            title,
            message,
            false,
            true
        ).also { dialog ->
            dialog.setConfirmText(getString(R.string.ok))
                .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog? -> dialog.hide() }
            dialog.show()
        }


    }

    override fun updateShippingAddress(addressData: AddressData) {
        viewModel.selectedAddress = addressData
    }

    override fun onAddressDeleteClick(addressData: AddressData?) {

            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(requireContext().getString(R.string.msg_are_you_sure))
                .setContentText(requireContext().getString(R.string.msg_want_to_delete_this_address))
                .setConfirmText(requireContext().getString(R.string.message_yes_delete_it))
                .setCancelText(requireContext().getString(R.string.cancel))
                .setConfirmClickListener { sDialog ->

                    triggerIntent(CheckoutAddressIntent.DeleteAddress(addressData!!))
                    sDialog.dismiss()
                }
                .showCancelButton(true)
                .show()

    }

    override fun onAddressEditClick(addressData: AddressData?) {
        triggerIntent(CheckoutAddressIntent.NavigateToEditAddress(addressData!!))
    }

    override fun render(effect: CheckoutAddressEffect) {
        when (effect) {
            is CheckoutAddressEffect.NavigateToAddAddress -> navigateToAddAddress()
            is CheckoutAddressEffect.NavigateToEditAddress -> navigateToEditAddress(effect.addressData)

        }
    }
}