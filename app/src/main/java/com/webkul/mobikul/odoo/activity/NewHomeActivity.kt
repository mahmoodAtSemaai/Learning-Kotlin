package com.webkul.mobikul.odoo.activity

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ActivityNewHomeBinding
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier.HomeActivityFragments
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewHomeActivity : BaseActivity() {
    private lateinit var binding: ActivityNewHomeBinding
    private val RC_ACCESS_FINE_LOCATION_NEW_ADDRESS = 1001
    private val RC_CHECK_LOCATION_SETTINGS = 1003
    private val RC_PICK_IMAGE = 1004
    private val RC_CAMERA = 1005
    private val RC_SIGN_IN_SIGN_UP = 1006
    private val TAG = "NewHomeActivity"
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private val mBackPressedTime: Long = 0
    private var currentFragmentDisplayed = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_home)
        setupUIController()
    }

    override fun getScreenTitle(): String = TAG

    private fun setupUIController() {
        val navController = Navigation.findNavController(this, R.id.home_nav_host)
        val bottomNavigationView: BottomNavigationView = binding.homeBottomNav
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        //NavigationUI.setupWithNavController(binding.homeToolbar, navController)
    }

    fun getHomePageResponse(): HomePageResponse? =
        intent.getParcelableExtra<HomePageResponse>(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFragmentNotifier(currentFragment: HomeActivityFragments?) {
        when (currentFragment) {
            HomeActivityFragments.HOME_FRAGMENT -> currentFragmentDisplayed =
                getString(R.string.home)
            HomeActivityFragments.NOTIFICATION_FRAGMENT -> currentFragmentDisplayed =
                getString(R.string.notification)
            HomeActivityFragments.ACCOUNT_FRAGMENT -> currentFragmentDisplayed =
                getString(R.string.account)
        }
    }
}