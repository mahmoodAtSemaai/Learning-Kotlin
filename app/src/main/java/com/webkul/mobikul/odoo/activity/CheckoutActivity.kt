package com.webkul.mobikul.odoo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.checkout.CheckoutProductsAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant.*
import com.webkul.mobikul.odoo.databinding.ActivityNewCheckoutBinding
import com.webkul.mobikul.odoo.fragment.PaymentStatusFragment
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.checkout.*
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import com.webkul.mobikul.odoo.model.payments.OrderPaymentData
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.webkul.mobikul.odoo.helper.AppSharedPref


class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityNewCheckoutBinding
    private lateinit var dialog: SweetAlertDialog
    private lateinit var orderDataResponse: OrderDataResponse
    private var selectedPaymentMethod: SelectedPaymentMethod? = null
    private val TOP_Y = 0
    private val START_X = 0
    private var isUserWantToRedeemPoints: Boolean = false

    private val startActivityForShippingAddressResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                hideError(binding.layoutAddressInfo.root, false)
                getOrderData(
                    orderDataResponse.orderId,
                    refreshRecyclerView = false,
                    refreshPaymentDetails = false
                )
            }
        }

    private val startActivityForShippingMethodResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                hideError(binding.layoutShippingMethod.clCheckoutOptions, true)
                getOrderData(
                    orderDataResponse.orderId,
                    refreshRecyclerView = false,
                    refreshPaymentDetails = true
                )
            }
        }

    private val startActivityForPaymentMethodResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedPaymentMethod: SelectedPaymentMethod =
                    data?.extras?.getParcelable(BUNDLE_SELECTED_PAYMENT_METHOD)!!
                this.selectedPaymentMethod = selectedPaymentMethod
                binding.layoutPaymentMethod.tvMethodDescription.text =
                    selectedPaymentMethod.paymentMethodText
                binding.paymentDetailContainer.tvPaymentMethod.text =
                    selectedPaymentMethod.paymentMethodText
                hideError(binding.layoutPaymentMethod.clCheckoutOptions, true)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_checkout)
        binding.root.visibility = View.INVISIBLE
        setToolbar()
        createDialog()
        setLayoutData()
        setButtonClickListener()
        getArguments()
        getIsUserWantToRedeemPoints()
    }

    private fun createDialog() {
        dialog = AlertDialogHelper.getAlertDialog(
            this, SweetAlertDialog.PROGRESS_TYPE,
            getString(R.string.payment_options_loading_text), "", false, false
        )
    }

    private fun hideError(view: View, errorPresent: Boolean) {
        binding.tvErrorMessage.visibility = View.GONE
        if (errorPresent)
            view.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.shape_rectangular_white_bg_gray_border_1dp,
                null
            )
        else
            view.setBackgroundResource(R.color.white)
        checkIfParamsAreAvailable()
    }

    private fun checkIfParamsAreAvailable() {
        if (!orderDataResponse.shippingAddressId.districtName.isEmpty()
            && orderDataResponse.delivery?.name?.isEmpty() == false
            && selectedPaymentMethod != null
            && selectedPaymentMethod?.paymentAcquirerId?.isEmpty() == false
        ) {
            binding.btnConfirmOrder.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorAccent
                )
            )
        } else {
            binding.btnConfirmOrder.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.grey_600
                )
            )
        }
    }

    private fun getArguments() {
        val orderId = intent.getIntExtra(BUNDLE_KEY_ORDER_ID, -1)
        getOrderData(orderId, true, true)
    }

    private fun getOrderData(
        orderId: Int,
        refreshRecyclerView: Boolean,
        refreshPaymentDetails: Boolean
    ) {
        ApiConnection.getOrderData(this, orderId, isUserWantToRedeemPoints).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<OrderDataResponse?>(this@CheckoutActivity) {
                override fun onSubscribe(d: Disposable) {
                    dialog.titleText = getString(R.string.fetching_order_details)
                    dialog.show()
                }

                override fun onNext(orderDataResponse: OrderDataResponse) {
                    super.onNext(orderDataResponse)
                    handleOrderDataResponse(
                        orderDataResponse,
                        refreshRecyclerView,
                        refreshPaymentDetails
                    )
                    dialog.hide()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    binding.root.visibility = View.INVISIBLE
                    showErrorDialog()
                }
            })

    }

    private fun showErrorDialog() {
        dialog = AlertDialogHelper.getAlertDialog(
            this, SweetAlertDialog.ERROR_TYPE,
            getString(R.string.error_request_failed), "", false, false
        )
        dialog.confirmText = getString(R.string.ok)
        dialog.setConfirmClickListener {
            dialog.hide()
            finish()
        }
        dialog.show()
    }

    private fun handleOrderDataResponse(
        orderDataResponse: OrderDataResponse,
        refreshRecyclerView: Boolean,
        refreshPaymentDetails: Boolean
    ) {
        this@CheckoutActivity.orderDataResponse = orderDataResponse
        binding.data = orderDataResponse
        binding.layoutAddressInfo.addressData = orderDataResponse.shippingAddressId
        setShippingMethodData(orderDataResponse)
        if (refreshRecyclerView) {
            binding.rvCheckout.adapter =
                CheckoutProductsAdapter(this@CheckoutActivity, orderDataResponse.items)
            binding.root.visibility = View.VISIBLE
        }
        if (refreshPaymentDetails)
            setPaymentDetailContainer(orderDataResponse)
    }

    private fun setPaymentDetailContainer(orderDataResponse: OrderDataResponse) {
        lateinit var orderPaymentData: OrderPaymentData
        if (orderDataResponse.delivery == null)
            orderPaymentData = OrderPaymentData(
                getString(R.string.cod_text),
                orderDataResponse.amountTotal,
                "(${orderDataResponse.cartCount} ${getString(R.string.product)})",
                orderDataResponse.pointsRedeemed,
                "",
                "",
                orderDataResponse.grandTotal
            )
        else {
            val paymentMethodText = binding.layoutPaymentMethod.tvMethodDescription.text.toString()
            if (!paymentMethodText.equals(
                    getString(R.string.checkout_select_your_payment_method),
                    true
                )
            ) {
                orderPaymentData = OrderPaymentData(
                    paymentMethodText,
                    orderDataResponse.amountTotal,
                    "(${orderDataResponse.cartCount} ${getString(R.string.product)})",
                    orderDataResponse.pointsRedeemed,
                    orderDataResponse.delivery.total,
                    "",
                    orderDataResponse.grandTotal
                )
            } else
                orderPaymentData = OrderPaymentData(
                    getString(R.string.cod_text),
                    orderDataResponse.amountTotal,
                    "(${orderDataResponse.cartCount} ${getString(R.string.product)})",
                    orderDataResponse.pointsRedeemed,
                    orderDataResponse.delivery.total,
                    "",
                    orderDataResponse.grandTotal
                )
        }
        binding.paymentDetailContainer.data = orderPaymentData
    }

    private fun setShippingMethodData(orderDataResponse: OrderDataResponse) {
        binding.layoutShippingMethod.tvMethodDescription.text =
            if (orderDataResponse.delivery != null) orderDataResponse.delivery.name else getString(R.string.checkout_select_a_shipping_method)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.tbCheckout.toolbar)
        getSupportActionBar()?.setTitle(getString(R.string.checkout))
        showBackButton(true)
    }

    private fun setButtonClickListener() {
        binding.addressHeading.root.setOnClickListener {
            startActivityForShippingAddressResult.launch(
                Intent(this, FragmentContainerActivity::class.java)
                    .putExtra(BUNDLE_KEY_FRAGMENT_TYPE, BUNDLE_ADDRESS_BOOK_SCREEN)
                    .putExtra(BUNDLE_KEY_ORDER_ID, orderDataResponse.orderId)
            )
        }

        binding.layoutShippingMethod.root.setOnClickListener {
            if (orderDataResponse.shippingAddressId.districtName.isEmpty() == false) {
                startActivityForShippingMethodResult.launch(
                    Intent(this, FragmentContainerActivity::class.java)
                        .putExtra(BUNDLE_KEY_FRAGMENT_TYPE, BUNDLE_SHIPPING_METHOD_SCREEN)
                        .putExtra(BUNDLE_KEY_ORDER_ID, orderDataResponse.orderId)
                )
            } else {
                validateShippingAddress()
            }
        }

        binding.layoutPaymentMethod.root.setOnClickListener {
            startActivityForPaymentMethodResult.launch(
                Intent(this, FragmentContainerActivity::class.java)
                    .putExtra(BUNDLE_KEY_FRAGMENT_TYPE, BUNDLE_PAYMENT_METHOD_SCREEN)
            )
        }


        binding.btnConfirmOrder.setOnClickListener {
            validateShippingAddress()
        }
    }

    private fun validateShippingAddress() {
        if (orderDataResponse.shippingAddressId.districtName.isEmpty()) {
            showError(
                binding.layoutAddressInfo.root,
                getString(R.string.unserviceable_address_text)
            )
            binding.nsvCheckout.smoothScrollTo(START_X, TOP_Y)
        } else {
            setVisibility(binding.tvErrorMessage, View.GONE)
            validateShippingMethod()
        }
    }


    private fun validateShippingMethod() {
        if (orderDataResponse.delivery == null || orderDataResponse.delivery?.name?.isEmpty() == true) {
            showError(
                binding.layoutShippingMethod.clCheckoutOptions,
                getString(R.string.select_your_shipping_method)
            )
            binding.nsvCheckout.smoothScrollTo(
                START_X,
                binding.layoutShippingMethod.clCheckoutOptions.top
            )
        } else {
            setVisibility(binding.tvErrorMessage, View.GONE)
            validatePaymentMethod()
        }
    }

    private fun validatePaymentMethod() {
        if (selectedPaymentMethod == null || selectedPaymentMethod?.paymentAcquirerId?.isEmpty() == true) {
            showError(
                binding.layoutPaymentMethod.clCheckoutOptions,
                getString(R.string.error_no_payment_method_chosen)
            )
            binding.nsvCheckout.smoothScrollTo(START_X, binding.paymentDetailContainer.root.bottom)
        } else {
            setVisibility(binding.tvErrorMessage, View.GONE)
            callOrderReviewDataApi()
        }
    }

    private fun setVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    private fun callOrderReviewDataApi() {
        ApiConnection.getOrderReviewData(
            this,
            OrderReviewRequest(
                orderDataResponse.shippingAddressId.id.toString(),
                orderDataResponse.delivery?.shippingId.toString(),
                selectedPaymentMethod?.paymentAcquirerId,
                isUserWantToRedeemPoints
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<OrderReviewResponse?>(this) {
                override fun onSubscribe(d: Disposable) {
                    dialog.titleText = getString(R.string.placing_your_order)
                    dialog.show()
                }

                override fun onNext(orderReviewResponse: OrderReviewResponse) {
                    placeOrder(orderReviewResponse.transactionId)
                }

                override fun onError(t: Throwable) {
                    dialog.hide()
                    SnackbarHelper.getSnackbar(
                        this@CheckoutActivity,
                        getString(R.string.error_request_failed),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    )
                }
            })
    }

    private fun showError(layout: View, errorMessage: String) {
        layout.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rectangle_red_border, null)
        binding.tvErrorMessage.text = errorMessage
        binding.tvErrorMessage.visibility = if (errorMessage.isEmpty()) View.GONE else View.VISIBLE
    }


    private fun placeOrder(transactionId: Int) {
        if (selectedPaymentMethod?.isCOD == true)
            placeCODOrder(transactionId)
        else
            placeVirtualAccountOrder(transactionId)
    }

    private fun placeVirtualAccountOrder(transactionId: Int) {
        ApiConnection.placeOrder(this, PlaceOrderRequest(transactionId, isUserWantToRedeemPoints))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<OrderPlaceResponse?>(this) {

                override fun onNext(t: OrderPlaceResponse) {
                    super.onNext(t)
                    dialog.hide()
                    redirectToPaymentProcessingFragment(transactionId)
                }

                override fun onError(t: Throwable) {
                    dialog.hide()
                    SnackbarHelper.getSnackbar(
                        this@CheckoutActivity,
                        getString(R.string.error_request_failed),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    )
                }
            })
    }

    private fun redirectToPaymentProcessingFragment(transactionId: Int) {
        startActivity(
            Intent(this@CheckoutActivity, FragmentContainerActivity::class.java)
                .putExtra(BUNDLE_KEY_FRAGMENT_TYPE, BUNDLE_PAYMENT_PROCESSING_SCREEN)
                .putExtra(
                    BUNDLE_ORDER_COST_DATA, CreateVirtualAccountPaymentRequest(
                        getString(R.string.currency_idr),
                        selectedPaymentMethod?.paymentAcquirerProviderId.toString(),
                        orderDataResponse.orderId.toString(),
                        transactionId.toString()
                    )
                )
                .putExtra(BUNDLE_KEY_ORDER_ID, orderDataResponse.orderId.toString())
        )
        finish()
    }


    private fun placeCODOrder(transactionId: Int) {
        ApiConnection.placeOrder(this, PlaceOrderRequest(transactionId, isUserWantToRedeemPoints))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<OrderPlaceResponse?>(this) {
                override fun onNext(t: OrderPlaceResponse) {
                    super.onNext(t)
                    dialog.hide()
                    dialog.dismiss()
                    redirectToOrderPlacedFragment()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    dialog.hide()
                    SnackbarHelper.getSnackbar(
                        this@CheckoutActivity, getString(R.string.error_request_failed),
                        Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING
                    )
                }
            })
    }


    private fun redirectToOrderPlacedFragment() {
        startActivity(
            Intent(this@CheckoutActivity, FragmentContainerActivity::class.java)
                .putExtra(BUNDLE_KEY_FRAGMENT_TYPE, BUNDLE_PAYMENT_STATUS_SCREEN)
                .putExtra(
                    BUNDLE_PAYMENT_STATUS_SCREEN,
                    PaymentStatusFragment.SHOW_PAYMENT_COD_MESSAGE
                )
        )
        finish()
    }

    private fun setLayoutData() {
        setUpShippingMethodContainer(getString(R.string.checkout_select_a_shipping_method))
        setUpPaymentMethodContainer(getString(R.string.checkout_select_your_payment_method))
        setUpAddressContainer()
    }

    private fun setUpAddressContainer() {
        binding.addressHeading.apply {
            ivMethodIcon.setImageResource(R.drawable.ic_location_active)
            data = ShippingMethod(getString(R.string.address), "")
        }
    }

    private fun setUpShippingMethodContainer(shippingMethodtext: String) {
        binding.layoutShippingMethod.apply {
            data = ShippingMethod(getString(R.string.shipping_method), shippingMethodtext)
            ivMethodIcon.setImageResource(R.drawable.ic_shipping_active)
        }
    }

    private fun setUpPaymentMethodContainer(paymentMethodText: String) {
        binding.layoutPaymentMethod.apply {
            data = ShippingMethod(getString(R.string.payment_method), paymentMethodText)
            ivMethodIcon.setImageResource(R.drawable.ic_payment_active)
        }
    }

    private fun getIsUserWantToRedeemPoints() {
        isUserWantToRedeemPoints = AppSharedPref.getIsCustomerWantToRedeemPoints(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun getScreenTitle(): String {
        return this.javaClass.simpleName
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppSharedPref.setIsCustomerWantToRedeemPoints(this, false);
    }


}