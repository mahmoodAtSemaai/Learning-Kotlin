package com.webkul.mobikul.odoo.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.cart.BagItemsRecyclerAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE
import com.webkul.mobikul.odoo.databinding.ActivityNewHomeBinding
import com.webkul.mobikul.odoo.fragment.EmptyFragment
import com.webkul.mobikul.odoo.handler.bag.BagActivityHandler
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier.HomeActivityFragments
import com.webkul.mobikul.odoo.helper.*
import com.webkul.mobikul.odoo.model.cart.BagResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    lateinit var navController : NavController
    private val mBackPressedTime: Long = 0
    private var currentFragmentDisplayed = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = this.window

        //changes the status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor= ContextCompat.getColor(this, R.color.background_appbar_color)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_home)
        setupUIController()

        //Open the Navigation Drawer
        openDrawer()
        getBagItemsCount()


         binding.searchView.setOnClickListener{
             binding.materialSearchView.visibility= View.VISIBLE
             binding.materialSearchView.openSearch()
         }

        binding.cartIcon.setOnClickListener{
            startActivity(Intent(this@NewHomeActivity , BagActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if(binding.materialSearchView.isVisible){
            binding.materialSearchView.visibility = View.GONE
            binding.materialSearchView.closeSearch()
        }
        else
            super.onBackPressed()
    }

    private fun openDrawer() {
        binding.drawerIcon.setOnClickListener {
            val intent = Intent(this@NewHomeActivity, NewDrawerActivity::class.java)
            intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, getHomePageResponse())
            startActivity(intent)
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }

    override fun getScreenTitle(): String = TAG

    private fun setupUIController() {
         navController = Navigation.findNavController(this, R.id.home_nav_host)
        val bottomNavigationView: BottomNavigationView = binding.homeBottomNav
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
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

    override fun onResume() {
        getBagItemsCount()
        super.onResume()
    }

    private fun getBagItemsCount() {
        val count = AppSharedPref.getCartCount(this@NewHomeActivity, 0)
        if (count != 0) {
            binding.badgeInfo.visibility = View.VISIBLE
            binding.badgeInfo.text = count.toString()
        } else {
            binding.badgeInfo.visibility = View.GONE
        }
    }
}