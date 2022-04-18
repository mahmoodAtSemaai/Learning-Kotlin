package com.webkul.mobikul.odoo.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
//import com.webkul.mobikul.odoo.databinding.ActivityNewDrawerBinding
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewDrawerActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_drawer)

        val homePageResponse =
            intent.getParcelableExtra<HomePageResponse>(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)

        val navController =
            Navigation.findNavController(this@NewDrawerActivity, R.id.drawer_fragment)
        val bundle = Bundle()
        bundle.putParcelable("home_response_data", homePageResponse)
        navController.setGraph(R.navigation.drawer_layout_navigation, bundle)
    }

    override fun onPause() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onPause()
    }

    override fun getScreenTitle(): String = TAG

}
