package com.webkul.mobikul.odoo.ui.signUpOnboarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.databinding.ActivityUserOnboardingBinding
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.ui.signUpOnboarding.effect.UserOnboardingEffect
import com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment.*
import com.webkul.mobikul.odoo.ui.signUpOnboarding.intent.UserOnboardingIntent
import com.webkul.mobikul.odoo.ui.signUpOnboarding.state.UserOnboardingState
import com.webkul.mobikul.odoo.ui.signUpOnboarding.viewModel.UserOnboardingViewModel
import com.webkul.mobikul.odoo.utils.CameraUtils
import com.webkul.mobikul.odoo.utils.CameraV1Utils
import com.webkul.mobikul.odoo.utils.GpsUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserOnboardingActivity @Inject constructor() :
    BindingBaseActivity<ActivityUserOnboardingBinding>(),
    IView<UserOnboardingIntent, UserOnboardingState, UserOnboardingEffect>, NoInternetStateListner {

    override val contentViewId: Int = R.layout.activity_user_onboarding
    private val viewModel: UserOnboardingViewModel by viewModels()

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private val REQUEST_PERMISSION = 100
    val MY_PERMISSIONS_REQUEST_LOCATION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeManager.setLocale(false, this)
        setObservers()
        setOnboardingData()
        initToolbar()
    }

    private fun setOnboardingData() {
        triggerIntent(UserOnboardingIntent.GetUserIdCustomerId)
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    override fun render(state: UserOnboardingState) {
        when (state) {
            is UserOnboardingState.Idle -> {
            }
            is UserOnboardingState.Loading -> {
                showProgressDialog()
            }
            is UserOnboardingState.GetUserOnboardingStage -> {
                dismissProgressDialog()
                triggerIntent(UserOnboardingIntent.GetUserOnboardingStage)
            }
            is UserOnboardingState.UserStage -> {
                dismissProgressDialog()
            }
            is UserOnboardingState.GetHomePageData -> {
                dismissProgressDialog()
                triggerIntent(UserOnboardingIntent.GetHomeData)
            }
            is UserOnboardingState.Error -> {
                dismissProgressDialog()
                if(state.failureStatus == FailureStatus.NO_INTERNET){
                    noInternetState()
                }else{
                    showErrorState(state.failureStatus, state.message)
                }
            }
            is UserOnboardingState.ExistingUser -> {
                dismissProgressDialog()
                triggerIntent(UserOnboardingIntent.GetHomeData)
            }
            is UserOnboardingState.SetToolbar -> {
                setToolbar()
            }
            is UserOnboardingState.OnboardingStage -> {
                dismissProgressDialog()
                getAllStageInfo()
            }
            is UserOnboardingState.GetIncompleteStage -> {
                dismissProgressDialog()
                getIncompleteStage()
            }
            is UserOnboardingState.LaunchNextStage -> {
                dismissProgressDialog()
                launchNextStage()
            }
            is UserOnboardingState.Splash -> {
                state.splashScreenResponse.updateSharedPref(this)
                triggerIntent(UserOnboardingIntent.GetHomeData)
            }
        }
    }

    private fun noInternetState() {
        triggerIntent(UserOnboardingIntent.Refresh)
    }

    override fun render(effect: UserOnboardingEffect) {
        when (effect) {
            is UserOnboardingEffect.NavigateToCustomerGroup -> navigateToCustomerGroup()
            is UserOnboardingEffect.NavigateToDetailsScreen -> navigateToDetailsScreen()
            is UserOnboardingEffect.NavigateToAddressScreen -> navigateToAddressScreen(effect.isAddressStagePending)
            is UserOnboardingEffect.NavigateToHomeScreen -> navigateToHomeScreen()
            is UserOnboardingEffect.RelaunchActivity -> navigateToNoInternerFragment()
        }
    }

    override fun triggerIntent(intent: UserOnboardingIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun getAllStageInfo() {
        triggerIntent(UserOnboardingIntent.GetOnboardingStage)
    }

    private fun getIncompleteStage() {
        triggerIntent(UserOnboardingIntent.GetIncompleteStage)
    }


    private fun launchNextStage() {
        triggerIntent(UserOnboardingIntent.LaunchNextStage)
    }

    private fun navigateToHomeScreen() {
        startActivity(
            Intent(applicationContext, NewHomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }

    private fun navigateToAddressScreen(isAddressStagePending: Boolean) {
        addFragmentWithBackStack(
            R.id.fl_onboarding_container,
            UserAddressFragment.newInstance(isAddressStagePending),
            UserAddressFragment::class.java.simpleName
        )
    }

    private fun navigateToDetailsScreen() {
        addFragmentWithBackStack(
            R.id.fl_onboarding_container,
            UserDetailsFragment.newInstance(),
            UserDetailsFragment::class.java.simpleName
        )
    }

    private fun navigateToCustomerGroup() {
        addFragmentWithBackStack(
            R.id.fl_onboarding_container,
            CustomerGroupFragment.newInstance(),
            CustomerGroupFragment::class.java.simpleName
        )
    }

    private fun navigateToNoInternerFragment() {
        addFragmentWithBackStack(R.id.fl_onboarding_container,
            NoInternetFragment.newInstance(this),
            NoInternetFragment::class.java.simpleName)
    }

    private fun initToolbar() {
        triggerIntent(UserOnboardingIntent.SetToolbar)
    }

    private fun setToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_onboarding_container)
        val cameraUtils = CameraUtils()
        val cameraV1Utils = CameraV1Utils()
        if (fragment != null) {
            //cameraUtils.onActivityResult(requestCode,resultCode,data,this,fragment)
            cameraV1Utils.onActivityResult(requestCode,resultCode,data,this,fragment)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSION){
            val cameraUtils = CameraUtils()
            cameraUtils.showPictureDialog(this)
        }
        else if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION){
            val fragment = supportFragmentManager.findFragmentById(R.id.fl_onboarding_container)
            val utils = GpsUtils(fragment as UserAddressFragment)
            utils.onRequestPermissionsResult(requestCode,permissions,grantResults,this,fragment)
        }else {
            val fragment = supportFragmentManager.findFragmentById(R.id.fl_onboarding_container)
            val cameraV1Utils = CameraV1Utils()
            if (fragment != null) {
                cameraV1Utils.onRequestPermissionsResult(requestCode,permissions,grantResults,this,fragment)
            }
        }
    }

    override fun noInternetListner() {
        setOnboardingData()
    }
}