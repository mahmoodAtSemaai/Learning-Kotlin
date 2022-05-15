package com.webkul.mobikul.odoo.fragment


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.home.NavDrawerCategoryStartAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.databinding.FragmentNewDrawerBinding
import com.webkul.mobikul.odoo.handler.home.HomeActivityHandler
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.ReferralResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NewDrawerFragment : BaseFragment() {


    lateinit var mBinding: FragmentNewDrawerBinding
    lateinit var navController: NavController


    override var title: String
        get() = "NewDrawerFragment"
        set(value) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_drawer, container, false)
        mBinding.handler = HomeActivityHandler(context)
        AppSharedPref.setLaunchcount(context, AppSharedPref.getLaunchCount(context) + 1)


        mBinding.drawerCloseBtn.setOnClickListener {
            (requireActivity()).onBackPressed()
        }

        return mBinding.root
    }
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(mBinding.root)


        //Receiving data from NewDrawerActivity through safe Args
        val args: NewDrawerFragmentArgs by navArgs()

        val data = args.homeResponseData
        if (data != null) {
            setUpDrawer(data)
        } else {
            Toast.makeText(context, R.string.error_something_went_wrong, Toast.LENGTH_SHORT).show()
        }

        languageSettings()
        
        openAccountFragment()

        getLoyaltyPoints()
    }


    fun openAccountFragment() {
        mBinding.profileImage.setOnClickListener {
            val action = NewDrawerFragmentDirections.actionNewDrawerFragmentToAccountFragment2()
            navController.navigate(action)
        }
        mBinding.username.setOnClickListener {
            val action = NewDrawerFragmentDirections.actionNewDrawerFragmentToAccountFragment2()
            navController.navigate(action)
        }
    }

    private fun languageSettings() {

        val langPref = AppSharedPref.getLanguageCode(context)

        mBinding.englishOption.setOnClickListener {
            if (langPref != getString(R.string.english_lang)) {
                HomeActivityHandler(context).onClickLanguageIcon(getString(R.string.english_lang))
            }
        }

        mBinding.indOption.setOnClickListener {
            if (langPref != getString(R.string.ind_lang)) {
                HomeActivityHandler(context).onClickLanguageIcon(getString(R.string.ind_lang))
            }
        }

        if (langPref.equals(getString(R.string.english_lang))) {
            mBinding.englishOption.setBackgroundColor(resources.getColor(R.color.background_orange))
            mBinding.englishOption.setTextColor(resources.getColor(R.color.white))
            mBinding.indOption.setBackgroundColor(resources.getColor(R.color.gray))
            mBinding.indOption.setTextColor(resources.getColor(R.color.black))
        } else {
            mBinding.englishOption.setBackgroundColor(resources.getColor(R.color.gray))
            mBinding.englishOption.setTextColor(resources.getColor(R.color.black))
            mBinding.indOption.setBackgroundColor(resources.getColor(R.color.background_orange))
            mBinding.indOption.setTextColor(resources.getColor(R.color.white))
        }

    }

    fun getLoyaltyPoints() {
        hitApiForLoyaltyPoints(AppSharedPref.getCustomerId(context))
    }

    fun hitApiForLoyaltyPoints(userId: String?) {
        ApiConnection.getLoyaltyPoints(context, userId).subscribeOn(Schedulers.io())
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
            showPoints(response.redeemHistory)
        } else {
            SnackbarHelper.getSnackbar(
                (context as Activity?)!!,
                response.message,
                Snackbar.LENGTH_LONG,
                SnackbarHelper.SnackbarType.TYPE_WARNING
            ).show()
        }
    }

    fun showPoints(loyaltyPoints: Int) {
        mBinding.drawerLoyalityPoints.text=loyaltyPoints.toString()
    }



    private fun setUpDrawer(homePageResponse: HomePageResponse?) {
        if (homePageResponse != null) {
            mBinding.categoryRv.adapter =
                NavDrawerCategoryStartAdapter(
                    context,
                    homePageResponse.categories[0].children,
                    ""
                )
        }
        if (homePageResponse?.languageMap!!.size > 0) {
            mBinding.languageData =
                homePageResponse.languageMap
        } else {
            val sqlLiteDbHelper = SqlLiteDbHelper(context)
            if (sqlLiteDbHelper.homeScreenData != null) {
                if (sqlLiteDbHelper.homeScreenData.languageMap != null) {
                    mBinding.languageData =
                        sqlLiteDbHelper.homeScreenData.languageMap
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.customerName =AppSharedPref.getCustomerName(context)
        getLoyaltyPoints()
    }
}