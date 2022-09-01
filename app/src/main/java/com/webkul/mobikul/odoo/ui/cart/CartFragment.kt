package com.webkul.mobikul.odoo.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CheckoutActivity
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.*
import com.webkul.mobikul.odoo.custom.CustomToast
import com.webkul.mobikul.odoo.data.entity.SellerEntity
import com.webkul.mobikul.odoo.data.request.*
import com.webkul.mobikul.odoo.data.response.models.*
import com.webkul.mobikul.odoo.databinding.FragmentCartBinding
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.OdooApplication
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.ReferralResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CartFragment : Fragment() ,
    DeleteCartItemDialogFragment.DeleteProduct,
    CartProductAdapter.ShowDialogFragmentListener,
    SellerAdapter.SelectSellerItems {


    private lateinit var binding : FragmentCartBinding

    var sellerList = ArrayList<SellerEntity>()
    private lateinit var deleteCartItemDialog: DeleteCartItemDialogFragment
    private var isButtonEnabled = false
    private lateinit var progressDialog : SweetAlertDialog
    val cartId by lazy {
        AppSharedPref.getCartId(requireContext())
    }
    var selectedProducts = ArrayList<Int>()
    private var orderId : Int = 0
    var totalCartItems = 0
    var loyaltyPoints = 0
    var lastUpdatedTotalPrice = ""
    val errorPopUpDuration = 1500L


    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.makeInvisible()
        progressDialog = AlertDialogHelper.getAlertDialog(requireContext(),
            SweetAlertDialog.PROGRESS_TYPE, getString(R.string.adding_item_to_wish_list),"",false,false)
        lastUpdatedTotalPrice = getString(R.string.rp) + getString(R.string.price_zero)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            setButtonState(false)
            cartCost.cbSelectAll.setOnClickListener {
                val isChecked = (it as AppCompatCheckBox).isChecked
                if (isChecked) {
                    selectAllCartProducts()
                }else{
                    unselectAllCartProducts()
                }
            }
            tvContinueShoppingText.setOnClickListener {
                redirectToHomeScreen()
            }
            tvEmptyCartText.setOnClickListener {
                deleteAllCartItems()
            }
            cartCost.btnRedirectToCheckout.setOnClickListener {
                if(isButtonEnabled) {
                    redirectToCheckout()
                }
                else
                    showCheckoutError()
            }
            cartCost.scSemaaiPoints.setOnClickListener {
                val isChecked = (it as SwitchCompat).isChecked
                if(selectedProducts.size != 0) {
                    if(isChecked)
                        getDiscountPrice()
                    else {
                        showPoints(loyaltyPoints)
                        binding.cartCost.tvCartTotal.text = lastUpdatedTotalPrice
                    }
                } else {
                    showCheckoutError()
                    it.isChecked = false
                }
            }

        }
    }

    private fun redirectToHomeScreen() {
        startActivity(
            Intent(requireContext(), NewHomeActivity::class.java)
                .putExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE,
                    (requireActivity() as NewCartActivity).getHomePageResponse()))
    }

    override fun onResume() {
        super.onResume()
        //binding.root.makeInvisible()
        progressDialog.titleText = getString(R.string.fetching_cart_items)
        progressDialog.show()
        getCart()
    }

    private fun getCart() {
        val cartId = AppSharedPref.getCartId(requireContext())
        ApiConnection.getCartData(requireContext(), cartId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<GetCartResponse>>(requireContext()){

                override fun onSubscribe(d: Disposable) {
                    progressDialog.titleText = getString(R.string.fetching_cart_items)
                    progressDialog.show()
                    sellerList.clear()
                    selectedProducts.clear()
                    binding.cartCost.apply {
                        tvCartTotal.text = "${getString(R.string.rp)}${getString(R.string.price_zero)}"
                        setButtonState(false)
                        binding.cartCost.tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"
                        cbSelectAll.isChecked = false
                        scSemaaiPoints.isChecked = false
                    }
                }

                override fun onNext(response: CartBaseResponse<GetCartResponse>) {
                    progressDialog.dismiss()
                    getSemaaiPoints()
                    orderId = response.result.orderId
                    AppSharedPref.setNewCartCount(requireContext(), response.result.newCartCount)
                    if(response.result.seller == null || response.result.seller.size == 0) {
                        (requireActivity() as NewCartActivity).setUpEmptyCartFragment()
                        return
                    }
                    else {
                        totalCartItems = 0
                        sellerList = response.result.seller
                        for(item in response.result.seller){
                            totalCartItems += item.products.size
                        }
                        binding.rvCart.adapter = SellerAdapter(requireContext(), sellerList, this@CartFragment) {
                            redirectToSellerScreen(it)
                        }
                    }
                    binding.root.makeVisible()
                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                    SnackbarHelper.getSnackbar(requireActivity(), getString(R.string.error_something_went_wrong),
                        Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }
            })
    }

    private fun getSemaaiPoints() {
        val customerId = AppSharedPref.getCustomerId(requireContext())
        ApiConnection.getLoyaltyPoints(requireContext(), customerId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<ReferralResponse?>(requireContext()) {
                override fun onNext(response: ReferralResponse) {
                    super.onNext(response)
                    handleLoyaltyPointsResponse(response)
                }
            })
    }

    fun handleLoyaltyPointsResponse(response: ReferralResponse) {
        if (response.status == ApplicationConstant.SUCCESS) {
            loyaltyPoints = response.redeemHistory
            showPoints(response.redeemHistory)
            binding.cartCost.scSemaaiPoints.isEnabled = response.redeemHistory != 0
        } else {
            SnackbarHelper.getSnackbar(requireActivity(), response.message,
                Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
        }
    }

    private fun showPoints(redeemHistory: Int) {
        binding.cartCost.apply {
            tvSemaaiPoints.text = "$redeemHistory"
        }
    }


    private fun unselectAllCartProducts() {
        selectedProducts.clear()
        sellerList.forEach { seller ->
            seller.isSellerChecked.set(false)
            seller.products.forEach { product ->
                product.isChecked.set(false)
            }
        }
        setButtonState(selectedProducts.size > 0)
        binding.cartCost.tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"
        binding.cartCost.tvCartTotal.text = "${getString(R.string.rp)}${getString(R.string.price_zero)}"
        binding.cartCost.scSemaaiPoints.isChecked = false
    }

    private fun getDiscountPrice() {
        selectedProducts.clear()
        sellerList.forEach { seller ->
            seller.products.forEach { product ->
                if(product.isChecked.get())
                    selectedProducts.add(product.lineId)
            }
        }
        if(selectedProducts.size == 0) {
            return
        }
        ApiConnection.getDiscountPrice(requireContext(), GetDiscountPriceRequest(orderId, selectedProducts))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<GetDiscountPriceResponse>>(requireContext()){
                override fun onNext(response: CartBaseResponse<GetDiscountPriceResponse>) {
                    binding.cartCost.tvCartTotal.text = response.result.discountedPrice
                    binding.cartCost.tvSemaaiPoints.text = "${response.result.semaaiPointsBalance}"
                    progressDialog.dismiss()
                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                    SnackbarHelper.getSnackbar(requireActivity(),
                        t.message, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }
            })
    }

    private fun selectAllCartProducts() {
        selectedProducts.clear()
        sellerList.forEach { seller ->
            seller.isSellerChecked.set(true)
            seller.products.forEach { product ->
                if(!product.isOutOfStock()) {
                    product.isChecked.set(true)
                    selectedProducts.add(product.lineId)
                }else{
                    product.isChecked.set(false)
                }
            }
        }
        setButtonState(selectedProducts.size > 0)
        binding.cartCost.tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"
        updateBulkCartItems(0) //seller position need to remove
    }

    private fun redirectToSellerScreen(sellerEntity: SellerEntity){
        startActivity(Intent(requireContext(), (context?.applicationContext as OdooApplication).sellerProfileActivity)
            .putExtra(BundleConstant.BUNDLE_KEY_SELLER_ID, sellerEntity.sellerId.toString()))
    }


    private fun deleteAllCartItems() {
        ApiConnection.deleteAllCartItems(requireContext(), AppSharedPref.getCartId(requireContext()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<DeleteAllCartItemResponse>>(requireContext()){
                override fun onSubscribe(d: Disposable) {
                    progressDialog.titleText = getString(R.string.removing_cart_items)
                    progressDialog.show()
                }
                override fun onNext(t: CartBaseResponse<DeleteAllCartItemResponse>) {
                    progressDialog.dismiss()
                    getCart()
                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                    SnackbarHelper.getSnackbar(requireActivity(), t.message, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }

                override fun onComplete() {
                }
            })

    }

    private fun showCheckoutError() {
        binding.apply {
            cartCost.tvNoProductSelected.visibility = View.VISIBLE
            cartCost.tvNoProductSelected.postDelayed(
                { cartCost.tvNoProductSelected.visibility = View.GONE },
                errorPopUpDuration
            )
        }
    }

    private fun setButtonState(state: Boolean) {
        binding.apply {
            cartCost.btnRedirectToCheckout.background =
                requireContext().getCompatDrawable(
                    if (!state) R.drawable.background_dark_gray_left_rounded_corner_8dp
                    else R.drawable.background_app_bar_left_rounded_corner_8dp)

            cartCost.btnRedirectToCheckout.setTextColor(
                requireContext().getCompatColor(
                    if (!state) R.color.white
                    else R.color.colorPrimary
                )
            )
            isButtonEnabled = state
        }
    }


    private fun redirectToCheckout() {
        val checkoutItems = ArrayList<Int>()
        sellerList.forEach { seller ->
            seller.products.forEach { product ->
                if(product.isChecked.get())
                    checkoutItems.add(product.lineId)
            }
        }
        if(checkoutItems.size != 0)
        {
            startActivity(Intent(requireContext(), CheckoutActivity::class.java)
                .putExtra(BundleConstant.BUNDLE_KEY_ORDER_ID, orderId)
                .putIntegerArrayListExtra(BundleConstant.BUNDLE_KEY_LINE_IDS, checkoutItems)
                .putExtra(BundleConstant.BUNDLE_KEY_IS_POINTS_USED, binding.cartCost.scSemaaiPoints.isChecked))
        }
    }


    override fun onDeleteClicked(sellerPosition: Int, lineId: Int, productPosition: Int) {
        deleteCartItemDialog = DeleteCartItemDialogFragment.newInstance(this, sellerPosition, lineId, productPosition)
        deleteCartItemDialog.show((requireActivity() as NewCartActivity).supportFragmentManager, deleteCartItemDialog::class.java.simpleName)
    }

    override fun showQuantityExceedingError() {
        binding.apply {
            clDialogBox.visibility = View.VISIBLE
            clDialogBox.postDelayed(
                { binding.clDialogBox.visibility = View.GONE },
                errorPopUpDuration
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onProductSelected(sellerPosition: Int) {
        selectedProducts.clear()
        var totalProducts = 0
        var totalSeller = 0
        var selectedSeller = 0
        sellerList.forEach { seller ->
            totalSeller++
            if (seller.isSellerChecked.get())
                selectedSeller++

            seller.products.forEach { product ->
                if(product.isChecked.get())
                    selectedProducts.add(product.lineId)
                totalProducts += 1
            }
        }
        binding.cartCost.tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"
        setButtonState(selectedProducts.size > 0)
        binding.cartCost.cbSelectAll.isChecked = totalSeller == selectedSeller
        if(selectedProducts.size > 0){
            getSelectedProductCost(selectedProducts)
        }else{
            binding.cartCost.tvCartTotal.text = "${getString(R.string.rp)}${getString(R.string.price_zero)}"
            binding.cartCost.scSemaaiPoints.isChecked = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getSelectedProductCost(selectedProducts: ArrayList<Int>) {
        ApiConnection.getSelectedItemsPrice(requireContext(), cartId, selectedProducts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<GetSelectedItemsPriceResponse>>(requireContext()){
                override fun onNext(response: CartBaseResponse<GetSelectedItemsPriceResponse>) {
                    binding.cartCost.tvCartTotal.text = response.result.grandTotal
                    lastUpdatedTotalPrice = response.result.grandTotal
                    if(binding.cartCost.scSemaaiPoints.isChecked){
                        getDiscountPrice()
                    }
                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                    SnackbarHelper.getSnackbar(requireActivity(), t.message, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }
            })
    }

    override fun addItemToWishList(productId: Int) {
        ApiConnection.addItemToWishListV1(requireContext(), AddToWishListRequest(productId)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<WishListUpdatedResponse?>(requireContext()){

                override fun onSubscribe(d: Disposable) {
                    progressDialog.titleText = getString(R.string.adding_item_to_wish_list)
                    progressDialog.show()
                }

                override fun onNext(response: WishListUpdatedResponse) {

                    progressDialog.dismiss()
                    CustomToast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show()

                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                }
            })
    }

    override fun allProductSelected(position: Int, isAllChecked: Boolean) {
        sellerList[position].isSellerChecked.set(isAllChecked)
        if(isAllChecked) {
            sellerList[position].products.forEach { product ->
                product.isChecked.set(true)
            }
        }
    }

    private fun updateBulkCartItems(sellerPosition: Int){
        selectedProducts.clear() //clear line ids
        var list = ArrayList<CartProductItemRequest>()

        var totalSeller = 0
        var selectedSeller = 0
        sellerList.forEach { seller ->
            totalSeller++

            if (seller.isSellerChecked.get())
                selectedSeller++

            seller.products.forEach { product ->
                if(product.isChecked.get() && !product.isOutOfStock()) {
                    selectedProducts.add(product.lineId)//set all selected product line ids
                    list.add(CartProductItemRequest(productId = product.productId, addQty = 0, setQty = product.quantity))
                }
            }
        }

        setButtonState(selectedProducts.size > 0)
        binding.cartCost.tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"
        binding.cartCost.tvCartTotal.text = "${getString(R.string.rp)}${getString(R.string.price_zero)}"

        if (selectedProducts.size<=0) {
            onProductSelected(sellerPosition) //handles checkbox selection
            return
        }

        binding.cartCost.apply {
            cpiCart.makeVisible()
            btnRedirectToCheckout.isEnabled = false
            btnRedirectToCheckout.setTextColor(requireContext().getCompatColor(R.color.background_appbar_color))
            tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"
            cbSelectAll.isChecked = totalSeller == selectedSeller

        }


        ApiConnection.updateBulkCartItemsV1(requireContext(), cartId, CartProductsRequest(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<CartBaseResponse<CartProductsResponse>>(requireContext()){
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        progressDialog.titleText = getString(R.string.updating_bag)
                        progressDialog.show()
                    }

                    override fun onNext(response: CartBaseResponse<CartProductsResponse>) {
                        binding.cartCost.apply {
                            btnRedirectToCheckout.isEnabled = true
                            cpiCart.makeGone()
                            btnRedirectToCheckout.setTextColor(requireContext().getCompatColor(R.color.colorPrimary))
                            setButtonState(selectedProducts.size > 0)
                        }
                        AppSharedPref.setNewCartCount(requireContext(), response.result.cartCount)

                        progressDialog.dismiss()
                        onProductSelected(sellerPosition = sellerPosition)
                    }

                    override fun onError(t: Throwable) {
                        progressDialog.dismiss()
                        binding.cartCost.apply {
                            btnRedirectToCheckout.isEnabled = true
                            cpiCart.makeInvisible()
                        }
                        SnackbarHelper.getSnackbar(requireActivity(), t.message, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING)
                    }

                    override fun onComplete() {
                        super.onComplete()
                    }
                })
    }

    override fun updateCartItemQuantity(sellerPosition: Int, productPosition: Int, lineId: Int, quantity: Int, change : Int, refreshSelectedProductPrice : Boolean) {
        val updateCartRequest = UpdateCartRequest(0, 0)
        if (change == 0)
            updateCartRequest.setQty = quantity
        else
            updateCartRequest.addQty = change
        if(selectedProducts.contains(lineId).not())
            selectedProducts.add(lineId)
        setButtonState(true)
        binding.cartCost.apply {
            cpiCart.makeVisible()
            btnRedirectToCheckout.isEnabled = false
            btnRedirectToCheckout.setTextColor(requireContext().getCompatColor(R.color.background_appbar_color))
            tvSelectedProductCount.text = "${selectedProducts.size} ${getString(R.string.product)}"

            var totalSeller = 0
            var selectedSeller = 0
            sellerList.forEach { sellerEntity->
                totalSeller++
                if (sellerEntity.isSellerChecked.get())
                    selectedSeller++
            }
            cbSelectAll.isChecked = selectedSeller == totalSeller
        }
        ApiConnection.updateCartV1(requireContext(), cartId, lineId, updateCartRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<UpdateCartItemResponse>>(requireContext()){

                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    progressDialog.titleText = getString(R.string.updating_bag)
                    progressDialog.show()
                }

                override fun onNext(response: CartBaseResponse<UpdateCartItemResponse>) {
                    binding.cartCost.apply {
                        btnRedirectToCheckout.isEnabled = true
                        cpiCart.makeGone()
                        btnRedirectToCheckout.setTextColor(requireContext().getCompatColor(R.color.colorPrimary))
                        setButtonState(selectedProducts.size > 0)
                    }
                    AppSharedPref.setNewCartCount(requireContext(), response.result.newCartCount)

                    if(refreshSelectedProductPrice){
                        getSelectedProductCost(selectedProducts)
                    }
                    progressDialog.dismiss()
                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                    binding.cartCost.apply {
                        btnRedirectToCheckout.isEnabled = true
                        cpiCart.makeInvisible()
                    }
                    SnackbarHelper.getSnackbar(requireActivity(), t.message, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING)
                }
            })
    }

    override fun onProductDeleted(sellerPosition: Int, lineId: Int, productPosition: Int) {
        deleteCartItemDialog.dismiss()
        val cartId = AppSharedPref.getCartId(requireContext())
        ApiConnection.deleteItemFromCartV1(requireContext(), cartId, lineId, SetQuantityRequest(0,0))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<DeleteCartItemResponse>>(requireContext()){
                override fun onSubscribe(d: Disposable) {
                    progressDialog.titleText = getString(R.string.removing_cart_items)
                    progressDialog.show()
                }

                override fun onNext(response: CartBaseResponse<DeleteCartItemResponse>) {
                    getCart()
                }

                override fun onError(t: Throwable) {
                    progressDialog.dismiss()
                    SnackbarHelper.getSnackbar(requireActivity(), t.message, Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }

                override fun onComplete() {
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }

    override fun selectAllProductItems(sellerPosition: Int) {  //seller is selected
        updateBulkCartItems(sellerPosition = sellerPosition)
    }


}